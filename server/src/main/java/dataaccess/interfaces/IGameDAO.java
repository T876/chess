package dataaccess.interfaces;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import service.models.GameListResponse;

import java.util.List;

public interface IGameDAO {
    // Create
    public int createGame(String name);

    // Read
    public GameData getGame(String gameID) throws DataAccessException;
    public List<GameData> getAllGames();

    // Update
    public void joinGame(String color, int gameID, String username) throws DataAccessException;

    // Delete
    public void clear();
}
