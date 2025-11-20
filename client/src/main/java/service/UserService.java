package service;

import server.ServerFacade;

public class UserService {
    private ServerFacade server;

    public UserService(ServerFacade server) {
        this.server = server;
    }
}
