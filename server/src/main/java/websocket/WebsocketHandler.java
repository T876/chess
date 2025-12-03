package websocket;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.interfaces.IAuthDAO;
import dataaccess.interfaces.IGameDAO;
import io.javalin.websocket.*;
import com.google.gson.Gson;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.LeaveGameCommand;
import websocket.commands.UserGameCommand;
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
            try {
                userData = authDAO.verifyAuthToken(command.getAuthToken());
            } catch (DataAccessException e) {
                String message = serializer.toJson(e);
                ctx.send(message);
                return;
            }

            switch (command.getCommandType()) {
                case CONNECT -> connectToGame(command.getGameID(), userData.username(), ctx.session);
                case LEAVE -> disconnectFromGame(ctx.message(), ctx.session);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    public void connectToGame(int gameID, String username, Session session) throws IOException {
        this.storage.add(gameID, session);
        String messageBody = String.format("%s has joined the game", username);
        var message = serializer.toJson(new ServerNotificationMessage(messageBody));
        this.storage.broadcastToGame(gameID, message, session);
        ChessGame currentGame = this.gameDAO.getGameByID(gameID).game();
        var gameMessage = serializer.toJson(new ServerLoadGameMessage(currentGame));
        this.storage.broadcastToGame(gameID, gameMessage, session);
    }

    public void disconnectFromGame(String messageRaw, Session session) throws DataAccessException, IOException {
        LeaveGameCommand command = serializer.fromJson(messageRaw, LeaveGameCommand.class);
        AuthData userData = authDAO.verifyAuthToken(command.getAuthToken());
        if (Objects.equals(command.color, "WHITE")) {
            this.gameDAO.leaveGame("WHITE", command.getGameID(), userData.username());
        } else if (Objects.equals(command.color, "BLACK")) {
            this.gameDAO.leaveGame("BLACK", command.getGameID(), userData.username());
        }

        this.storage.remove(command.getGameID(), session);

        String messageString = String.format("%s has left the game", userData.username());
        ServerNotificationMessage messageObj = new ServerNotificationMessage(messageString);
        String notificationMessage = serializer.toJson(messageObj);
        this.storage.broadcastToGame(command.getGameID(), notificationMessage, session);
    }
}
