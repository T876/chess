package server;

import dataaccess.*;
import dataaccess.interfaces.IAuthDAO;
import dataaccess.interfaces.IGameDAO;
import dataaccess.interfaces.IUserDAO;
import io.javalin.*;
import server.handlers.DestructionHandler;
import server.handlers.GameHandler;
import server.handlers.UserHandler;
import service.DestructionService;
import service.GameService;
import service.UserService;
import websocket.GameConnectionStorage;
import websocket.WebsocketHandler;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        IAuthDAO authDAO;
        IUserDAO userDAO;
        IGameDAO gameDAO;

        GameConnectionStorage connectionStorage = new GameConnectionStorage();

        // Data Access
        try{
            authDAO = new SQLAuthDAO();
            userDAO = new SQLUserDAO();
            gameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Services
        UserService userService = new UserService(authDAO, userDAO);
        GameService gameService = new GameService(authDAO, gameDAO);
        DestructionService destructionService = new DestructionService(authDAO, userDAO, gameDAO);

        // Handlers
        UserHandler userHandler = new UserHandler(userService);
        GameHandler gameHandler = new GameHandler(gameService);
        DestructionHandler destructionHandler = new DestructionHandler(destructionService);
        WebsocketHandler websocketHandler = new WebsocketHandler(connectionStorage, authDAO, gameDAO);

        // ## Endpoints ##
        // User management endpoints
        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);

        // Game endpoints
        javalin.get("/game", gameHandler::listGames);
        javalin.post("/game", gameHandler::createGame);
        javalin.put("/game", gameHandler::joinGame).ws("/ws", ws -> {
            ws.onConnect(websocketHandler);
            ws.onMessage(websocketHandler);
            ws.onClose(websocketHandler);
        });

        // Destruction endpoint
        javalin.delete("/db", destructionHandler::clearApplication);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
