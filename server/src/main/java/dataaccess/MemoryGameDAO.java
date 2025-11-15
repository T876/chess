package dataaccess;

import chess.ChessGame;
import dataaccess.interfaces.IGameDAO;
import jdk.jshell.spi.ExecutionControl;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements IGameDAO {
    List<GameData> games = new ArrayList<>();

    public  MemoryGameDAO() {};

    public String createGame(String name) {
        return "";
    };

    public GameData getGame(String gameID) throws DataAccessException {
        throw new DataAccessException("Not implemented");
    };

    public List<GameData> getAllGames(){
       return games;
    };

    public void joinGame(ChessGame.TeamColor color, String gameID) throws DataAccessException {

    };

    public void clear() {

    };
}
