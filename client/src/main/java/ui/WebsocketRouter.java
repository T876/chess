package ui;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.websocket.MessageHandler;
import ui.server.WebsocketClient;
import ui.service.GameService;
import ui.service.UserService;
import websocket.messages.ServerErrorMessage;
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
                case ERROR -> this.handleError(
                        this.serializer.fromJson(messageRaw, ServerErrorMessage.class)
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
        this.gameService.selectedGame = message.getChessGame();
        System.out.println();
        System.out.println("New game state received. Rendering...");
        this.gameService.printGame();
        if (message.getChessGame().winner != null) {
            System.out.println("The winner of this game is: " + this.gameService.selectedGame.winner);
            System.out.println("Please type 'leave' to return to main menu");
        }
        System.out.print("[" + userService.authData.username() + "] >>> ");
    }

    private void handleError(ServerErrorMessage message) {
        System.out.println();
        System.out.println("Error:" + message.getErrorMessage());
        System.out.print("[" + userService.authData.username() + "] >>> ");
    }


}
