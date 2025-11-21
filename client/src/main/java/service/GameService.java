package service;

import chess.ChessGame;
import model.GameData;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    private ServerFacade server;
    public ChessGame selectedGame;
    public ChessGame.TeamColor color;

    public GameService(ServerFacade server) {
        this.server = server;
    }

    public boolean createGame(String name, String authToken) {
        return true;
    }

    public List<GameData> listGames(String authToken) {
        return new ArrayList<>();
    }

    public GameData joinGame(int gameID, String teamColor, String authToken) {
        ChessGame game = new ChessGame();
        this.selectedGame = game;
        this.color = teamColor == "WHITE" ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        return new GameData(1, null, null, "game1", game);
    }

    public void printGame() {
        System.out.println("Printing Game");
    }
}
