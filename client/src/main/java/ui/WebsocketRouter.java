package ui;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.websocket.MessageHandler;
import ui.server.WebsocketClient;
import ui.service.GameService;
import ui.service.UserService;
import websocket.messages.ServerLoadGameMessage;
import websocket.messages.ServerMessage;
import websocket.messages.ServerNotificationMessage;

import java.util.Objects;

public class WebsocketRouter {
    private final GameService gameService;
    private final WebsocketClient client;
    private final Gson serializer;
    private final UserService userService;

    public WebsocketRouter(GameService gameService, UserService userService, WebsocketClient client) {
        this.gameService = gameService;
        this.userService = userService;
        this.serializer = new Gson();
        this.client = client;
    }

    public void startMessageListener() {
        client.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                routeServerMessage(message);
            }
        });
    }

    public void routeServerMessage(String messageRaw) {
        try {
            ServerMessage genericMessage = this.serializer.fromJson(messageRaw, ServerMessage.class);

            switch (genericMessage.getServerMessageType()) {
                case NOTIFICATION -> this.announceNotification(
                        this.serializer.fromJson(messageRaw, ServerNotificationMessage.class)
                );
                case LOAD_GAME -> this.updateGameState(
                        this.serializer.fromJson(messageRaw, ServerLoadGameMessage.class)
                );

            }

        } catch (Exception e) {
            // Do nothing here. This is on purpose since we don't need to let the client know
            // if the server sends a bad message. We just ignore it.
        }
    }

    private void announceNotification(ServerNotificationMessage notification) {
        System.out.println(); // For the previous input
        System.out.println(notification.message);
        System.out.print("[" + userService.authData.username() + "] >>> "); // Indicate that we are still listening for an input
    }

    private void updateGameState(ServerLoadGameMessage message) {
        if (!Objects.equals(this.gameService.selectedGame, message.getChessGame())) {
            this.gameService.selectedGame = message.getChessGame();
            System.out.println();
            System.out.println("Game has been updated. Redrawing...");
            this.gameService.printGame();
            System.out.print("[" + userService.authData.username() + "] >>> ");
        }
    }


}
