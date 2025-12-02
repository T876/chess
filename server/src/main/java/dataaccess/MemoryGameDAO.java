package dataaccess;

import chess.ChessGame;
import dataaccess.interfaces.IGameDAO;
import jdk.jshell.spi.ExecutionControl;
import model.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryGameDAO implements IGameDAO {
    List<GameData> games = new ArrayList<>();

    public  MemoryGameDAO() {};

    public int createGame(String name) {
        int gameID = games.size() + 1;
        games.add(new GameData(
                gameID,
                null,
                null,
                name,
                new ChessGame()
        ));
        return gameID;
    };

    public List<GameData> getAllGames(){
       return games;
    };

    public void joinGame(String color, int gameID, String username) throws DataAccessException {
        GameData gameMatch = null;
        for (GameData game: games) {
            if (game.gameID() == gameID) {
                gameMatch = game;
            }
        }

        GameData newGame = null;

        if (Objects.equals(color, "WHITE")) {
            if (gameMatch.whiteUsername() == null) {
                newGame = new GameData(gameMatch.gameID(),
                        username,
                        gameMatch.blackUsername(),
                        gameMatch.gameName(),
                        gameMatch.game());
                games.remove(gameMatch);
                games.add(newGame);
                return;
            }
        } else if (Objects.equals(color, "BLACK")) {
            if (gameMatch.blackUsername() == null) {
                newGame = new GameData(gameMatch.gameID(),
                        gameMatch.whiteUsername(),
                        username,
                        gameMatch.gameName(),
                        gameMatch.game());
                games.remove(gameMatch);
                games.add(newGame);
                return;
            }
        }

        throw new DataAccessException("Error: already taken");
    };

    public void leaveGame(String color, int gameID, String username) throws DataAccessException { }

    public GameData getGameByID(int id) {
        throw new RuntimeException("Not implemented");
    }

    public void clear() {
        this.games = new ArrayList<>();
    };
}
