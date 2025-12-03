package dataaccess.interfaces;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.GameData;

import java.sql.Connection;
import java.util.List;

public interface IGameDAO {
    // Create
    public int createGame(String name);

    // Read
    public List<GameData> getAllGames();
    public GameData getGameByID(int id);

    // Update
    public void joinGame(String color, int gameID, String username) throws DataAccessException;
    public void leaveGame(String color, int gameID, String username) throws DataAccessException;
    public void updateGameState(ChessGame newGameState, int gameID);

    // Delete
    public void clear();
}
