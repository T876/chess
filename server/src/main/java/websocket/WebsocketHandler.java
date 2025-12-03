package websocket;

import chess.ChessGame;
import chess.ChessPiece;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import dataaccess.interfaces.IAuthDAO;
import dataaccess.interfaces.IGameDAO;
import com.google.gson.Gson;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.commands.UserMoveCommand;
import websocket.messages.ServerErrorMessage;
import websocket.messages.ServerLoadGameMessage;
import websocket.messages.ServerNotificationMessage;

import java.io.IOException;
import java.util.Objects;


public class WebsocketHandler implements WsConnectHandler, WsCloseHandler, WsMessageHandler {
    GameConnectionStorage storage;
    IAuthDAO authDAO;
    IGameDAO gameDAO;
    Gson serializer;

    public WebsocketHandler(GameConnectionStorage storage, IAuthDAO authDAO, IGameDAO gameDAO) {
        this.storage = storage;
        this.authDAO = authDAO;
        this.serializer = new Gson();
        this.gameDAO = gameDAO;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = serializer.fromJson(ctx.message(), UserGameCommand.class);
            AuthData userData;
            String messageRaw = ctx.message();
            try {
                userData = authDAO.verifyAuthToken(command.getAuthToken());
            } catch (DataAccessException e) {
                throw new RuntimeException(e.getMessage());
            }

            switch (command.getCommandType()) {
                case CONNECT -> connectToGame(command, ctx.session, userData.username());
                case LEAVE -> disconnectFromGame(command, ctx.session, userData.username());
                case MAKE_MOVE -> makeMove(ctx.message(), ctx.session, userData.username());
                case RESIGN -> resign(command, ctx.session, userData.username());
            }
        } catch (Exception ex) {
            String error = serializer.toJson(new ServerErrorMessage(ex.getMessage()));
            try {
                ctx.session.getRemote().sendString(error);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    public void connectToGame(UserGameCommand command, Session session, String username) throws IOException {
        int gameID = command.getGameID();

        String messageBody = String.format("%s joined the game", username);

        ChessGame currentGame = this.gameDAO.getGameByID(gameID).game();
        this.storage.add(gameID, session);
        var message = serializer.toJson(new ServerNotificationMessage(messageBody));
        this.storage.broadcastToGame(gameID, message, session, false);
        var gameMessage = serializer.toJson(new ServerLoadGameMessage(currentGame));
        session.getRemote().sendString(gameMessage);
    }

    public void disconnectFromGame(UserGameCommand command, Session session, String username) throws DataAccessException, IOException {
        int gameID = command.getGameID();
        GameData currentGame = this.gameDAO.getGameByID(gameID);

        if (Objects.equals(currentGame.whiteUsername(), username)) {
            this.gameDAO.leaveGame("WHITE", command.getGameID(), username);
        } else if (Objects.equals(currentGame.blackUsername(), username)) {
            this.gameDAO.leaveGame("BLACK", command.getGameID(), username);
        }

        this.storage.remove(command.getGameID(), session);

        String messageString = String.format("%s has left the game", username);
        ServerNotificationMessage messageObj = new ServerNotificationMessage(messageString);
        String notificationMessage = serializer.toJson(messageObj);
        this.storage.broadcastToGame(command.getGameID(), notificationMessage, session, false);
    }

    public void makeMove(String commandRaw, Session session, String username) {
        UserMoveCommand command;
        try {
            command = serializer.fromJson(commandRaw, UserMoveCommand.class);
        } catch (Exception e) {
            throw new RuntimeException("Bad request. Please try again");
        }

        GameData currentGameData = this.gameDAO.getGameByID(command.getGameID());
        String blackUsername = currentGameData.blackUsername();
        String whiteUsername = currentGameData.whiteUsername();
        ChessGame game = currentGameData.game();
        ChessPiece startingPiece = game.getBoard().getPiece(command.getMove().getStartPosition());

        if (game.winner != null) {
            throw new RuntimeException("Moves cannot be made after the game is over");
        }

        if (Objects.equals(username, blackUsername) && startingPiece.getTeamColor() != ChessGame.TeamColor.BLACK) {
            throw new RuntimeException("You can only move your own pieces on your turn.");
        } if (Objects.equals(username, whiteUsername) && startingPiece.getTeamColor() != ChessGame.TeamColor.WHITE) {
            throw new RuntimeException("You can only move your own pieces on your turn.");
        }

        if (!Objects.equals(username, whiteUsername) && !Objects.equals(username, blackUsername)) {
            throw new RuntimeException("You cannot make a move as an observer");
        }

        try {
            game.makeMove(command.getMove());
        } catch ( InvalidMoveException e) {
            throw new RuntimeException("Move invalid. Remember, you can move your own pieces on your turn.");
        }

        this.gameDAO.updateGameState(game, command.getGameID());
        ServerLoadGameMessage message = new ServerLoadGameMessage(game);
        ServerNotificationMessage notificationMessage = new ServerNotificationMessage(String.format("%s made a move", username));
        String notificationJson = serializer.toJson(notificationMessage);
        String messageJson = serializer.toJson(message);

        try {
            this.storage.broadcastToGame(command.getGameID(), notificationJson, session, false);
            this.storage.broadcastToGame(command.getGameID(), messageJson, session, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void resign(UserGameCommand command, Session session, String username) {
        GameData currentGameData = this.gameDAO.getGameByID(command.getGameID());
        String blackUsername = currentGameData.blackUsername();
        String whiteUsername = currentGameData.whiteUsername();
        ChessGame game = currentGameData.game();

        if (game.winner != null) {
            throw new RuntimeException("Cannot resign when there is a winner");
        }

        if (Objects.equals(username, blackUsername)) {
            game.resign(ChessGame.TeamColor.BLACK);
        } else if (Objects.equals(username, whiteUsername)) {
            game.resign(ChessGame.TeamColor.WHITE);
        } else {
            throw new RuntimeException("Observers cannot resign");
        }

        this.gameDAO.updateGameState(game, command.getGameID());
        ServerNotificationMessage notificationMessage = new ServerNotificationMessage(String.format("%s resigned.", username));
        String notificationJson = serializer.toJson(notificationMessage);

        try {
            this.storage.broadcastToGame(command.getGameID(), notificationJson, session, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
