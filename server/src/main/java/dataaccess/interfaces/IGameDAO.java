package dataaccess.interfaces;

import chess.ChessGame;
import model.GameData;
import service.models.GameListResponse;

import java.util.List;

public interface IGameDAO {
    // Create
    public String createGame(String name);

    // Read
    public GameData getGame(String gameID);
    public List<GameData> getAllGames();

    // Update
    public void joinGame(ChessGame.TeamColor color, String gameID);

    // Delete
    public void clear();
}
