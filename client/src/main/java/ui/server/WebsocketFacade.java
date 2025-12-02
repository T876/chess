package ui.server;

import com.google.gson.Gson;
import jakarta.websocket.MessageHandler;
import ui.WebsocketRouter;
import websocket.commands.JoinGameCommand;
import websocket.commands.LeaveGameCommand;

public class WebsocketFacade {
    WebsocketClient client;
    Gson serializer;
    WebsocketRouter router;

    public WebsocketFacade (WebsocketClient client) {
        this.client = client;
        this.serializer = new Gson();
    }

    public void sendJoinGameCommand(String authToken, Integer gameID, String joinGameAs) {
        JoinGameCommand command = new JoinGameCommand(authToken, gameID, joinGameAs);
        try  {
            this.client.send(serializer.toJson(command));
        } catch (Exception e) {
            throw new RuntimeException("Error: Join failed. Please try again");
        }
    }

    public void sendLeaveGameCommand(String authToken, Integer gameID, String color) {
        LeaveGameCommand command = new LeaveGameCommand(color, authToken, gameID);
        try {
            this.client.send(serializer.toJson(command));
        } catch (Exception e) {
            throw new RuntimeException("Error: Leave game failed. Please try again");
        }
    }


}
