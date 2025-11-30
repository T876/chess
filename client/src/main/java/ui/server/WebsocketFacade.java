package ui.server;

import com.google.gson.Gson;
import websocket.commands.JoinGameCommand;

public class WebsocketFacade {
    WebsocketClient client;
    Gson serializer;

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
}
