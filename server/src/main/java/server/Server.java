package server;

import io.javalin.*;
import server.handlers.DestructionHandler;
import server.handlers.GameHandler;
import server.handlers.UserHandler;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Handlers
        UserHandler userHandler = new UserHandler();
        GameHandler gameHandler = new GameHandler();
        DestructionHandler destructionHandler = new DestructionHandler();

        // ## Endpoints ##
        // User management endpoints
        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);

        // Game endpoints
        javalin.get("/game", gameHandler::listGames);
        javalin.post("/game", gameHandler::createGame);
        javalin.put("/game", gameHandler::joinGame);

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
