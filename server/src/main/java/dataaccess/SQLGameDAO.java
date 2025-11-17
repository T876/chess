package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.interfaces.IGameDAO;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGameDAO implements IGameDAO {
    Gson serializer;

    public SQLGameDAO() throws DataAccessException {
        this.setupTable();
        this.serializer = new Gson();
    };

    // Create
    public int createGame(String name){
        return 1;
    };

    // Read
    public List<GameData> getAllGames(){
        List<GameData> games = new ArrayList<>();
        String queryString = "SELECT id, whiteUsername, blackUsername, gameName, chessGame FROM game";
        try (Connection c = DatabaseManager.getConnection()) {
            try (var query = c.prepareStatement(queryString)){
                try(var result = query.executeQuery()) {
                    while(result.next()) {
                        String gameJson = result.getString("chessGame");
                        games.add(new GameData(
                                result.getInt("id"),
                                result.getString("whiteUsername"),
                                result.getString("blackUsername"),
                                result.getString("gameName"),
                                serializer.fromJson(gameJson, ChessGame.class)
                        ));
                    }
                };
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return games;
    };



    // Update
    public void joinGame(String color, int gameID, String username) throws DataAccessException {

    };

    // Delete
    public void clear() {
        String queryString = "TRUNCATE TABLE game";
        try (Connection c = DatabaseManager.getConnection()) {
            try (var query = c.prepareStatement(queryString)){
                query.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    };

    private void setupTable() throws DataAccessException {
        DatabaseManager.createDatabase();
        String createTableString = """
                CREATE TABLE IF NOT EXISTS game(
                    id INT NOT NULL AUTO_INCREMENT,
                    whiteUsername VARCHAR(255) DEFAULT NULL,
                    blackUsername VARCHAR(255) DEFAULT NULL,
                    gameName VARCHAR(255) NOT NULL,
                    chessGame longText NOT NULL,
                    PRIMARY KEY (id)
                );
                """;
        try (Connection c = DatabaseManager.getConnection()) {
            try (var query = c.prepareStatement(createTableString)) {
                query.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
