package ui.server;

import chess.ChessMove;
import com.google.gson.Gson;
import ui.WebsocketRouter;
import websocket.commands.UserGameCommand;
import websocket.commands.UserMoveCommand;

public class WebsocketFacade {
    WebsocketClient client;
    Gson serializer;
    WebsocketRouter router;

    public WebsocketFacade (WebsocketClient client) {
        this.client = client;
        this.serializer = new Gson();
    }

    public void sendJoinGameCommand(String authToken, Integer gameID) {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        try  {
            this.client.send(serializer.toJson(command));
        } catch (Exception e) {
            throw new RuntimeException("Error: Join failed. Please try again");
        }
    }

    public void sendLeaveGameCommand(String authToken, Integer gameID) {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        try {
            this.client.send(serializer.toJson(command));
        } catch (Exception e) {
            throw new RuntimeException("Error: Leave game failed. Please try again");
        }
    }

    public void sendMoveCommand(String authToken, Integer gameID, ChessMove proposedMove) {
        UserMoveCommand command = new UserMoveCommand(authToken, gameID, proposedMove);
        try {
            this.client.send(serializer.toJson(command));
        } catch (Exception e) {
            throw new RuntimeException("Error: Move failed. Please try again");
        }
    }

    public void sendResignCommand(String authToken, int gameID) {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        try {
            this.client.send(serializer.toJson(command));
        } catch (Exception e) {
            throw new RuntimeException("Error: Unable to resign");
        }
    }


}
