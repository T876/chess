package ui.server;

import com.google.gson.Gson;
import ui.WebsocketRouter;
import websocket.commands.UserGameCommand;

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

    public void sendLeaveGameCommand(String authToken, Integer gameID, String color) {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        try {
            this.client.send(serializer.toJson(command));
        } catch (Exception e) {
            throw new RuntimeException("Error: Leave game failed. Please try again");
        }
    }


}
