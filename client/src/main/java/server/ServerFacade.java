package server;

import com.google.gson.Gson;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerFacade {
    private Gson serializer;

    public ServerFacade(Gson gson) {
        this.serializer = gson;
    }

    public AuthData register(String username, String password, String email) {
        return new AuthData(UUID.randomUUID().toString(), username);
    }

    public AuthData login(String username, String password) {
        return new AuthData(UUID.randomUUID().toString(), username);
    }

    public void logout(String authToken) { }

    public List<GameData> listGames(String authToken) {
        return new ArrayList<>();
    }

    public int createGame(String authToken, String name) {
        return 1;
    }

    public void joinGame(String authToken, int gameID) {}
}
