package ui.service;

import chess.ChessGame;
import model.GameData;
import model.GameInfo;
import ui.server.ServerFacade;

import java.util.List;

public class GameService {
    private ServerFacade server;
    public ChessGame selectedGame;
    public ChessGame.TeamColor color;

    public GameService(ServerFacade server) {
        this.server = server;
    }

    public int createGame(String name, String authToken) {
        return this.server.createGame(authToken, name);
    }

    public List<GameInfo> listGames(String authToken) {
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
