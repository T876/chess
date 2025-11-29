package websocket;

import dataaccess.DataAccessException;
import dataaccess.interfaces.IAuthDAO;
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
import websocket.commands.UserGameCommand;

import java.io.IOException;


public class WebsocketHandler implements WsConnectHandler, WsCloseHandler, WsMessageHandler {
    GameConnectionStorage storage;
    IAuthDAO authDAO;

    public WebsocketHandler(GameConnectionStorage storage, IAuthDAO authDAO) {
        this.storage = storage;
        this.authDAO = authDAO;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            AuthData userData;
            try {
                userData = authDAO.verifyAuthToken(command.getAuthToken());
            } catch (DataAccessException e) {
                ctx.send("Error: Unauthorized");
                return;
            }

            switch (command.getCommandType()) {
                case CONNECT -> connectToGame(command.getGameID(), userData.username(), ctx.session);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    public void connectToGame(int gameID, String username,  Session session) throws IOException {
        this.storage.add(gameID, session);
        var message = String.format("%s joined the game", username);
        this.storage.broadcastToGame(gameID, message, session);
    }
}
