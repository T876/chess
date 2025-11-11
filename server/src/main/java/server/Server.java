package server;

import io.javalin.*;
import server.handlers.UserHandler;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Handlers
        UserHandler userHandler = new UserHandler();

        // Endpoints
        javalin.post("/user", userHandler::register);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
