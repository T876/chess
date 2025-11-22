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
        this.server.createGame(authToken, name);
        return true;
    }

    public List<GameData> listGames(String authToken) {
        return this.server.listGames(authToken);
    }

    public void joinGame(int gameID, String teamColor, String authToken) {
        this.server.joinGame(authToken, gameID);
        ChessGame game = new ChessGame();
        this.selectedGame = game;
        this.color = teamColor == "WHITE" ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
    }

    public void observeGame(int gameID, String authToken) {
        ChessGame game = new ChessGame();
        this.selectedGame = game;
    }

    public void printGame() {
        System.out.println("Printing Game");
    }
}
