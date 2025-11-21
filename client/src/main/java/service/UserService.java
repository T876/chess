package service;

import model.AuthData;
import server.ServerFacade;

import java.util.Optional;
import java.util.UUID;

public class UserService {
    private ServerFacade server;
    public AuthData authData;

    public UserService(ServerFacade server) {
        this.server = server;
    }

    public void register(String username, String password, String email) {
        this.authData = new AuthData(UUID.randomUUID().toString(), username);
    }

    public void login(String username, String password) {
        this.authData = new AuthData(UUID.randomUUID().toString(), username);
    }

    public void logout() {
        this.authData = null;
    }
}
