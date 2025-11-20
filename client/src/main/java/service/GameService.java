package service;

import chess.ChessGame;
import model.GameData;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    private ServerFacade server;
    public ChessGame selectedGame;

    public GameService(ServerFacade server) {
        this.server = server;
    }

    public boolean createGame(GameData data, String authToken) {
        return true;
    }

    public List<GameData> listGames(String authToken) {
        return new ArrayList<>();
    }

    public GameData joinGame(boolean observeOnly, String authToken) {
        return new GameData(1, null, null, "game1", new ChessGame());
    }
}
