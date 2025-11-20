package service;

import server.ServerFacade;

public class GameService {
    private ServerFacade server;

    public GameService(ServerFacade server) {
        this.server = server;
    }
}
