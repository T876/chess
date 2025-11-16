package dataaccess;

import dataaccess.interfaces.IGameDAO;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class SQLGameDAO implements IGameDAO {

    public SQLGameDAO() { };

    // Create
    public int createGame(String name){
        return 1;
    };

    // Read
    public List<GameData> getAllGames(){
        return new ArrayList<>();
    };

    // Update
    public void joinGame(String color, int gameID, String username) throws DataAccessException {

    };

    // Delete
    public void clear() { };
}
