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
        String createUserString  = """
                INSERT INTO game (whiteUsername, blackUsername, gameName, chessGame) VALUES(?, ?, ?, ?);
                """;

        int gameID;
        if (stringIsSanitized(name)) {
            String chessGameString = serializer.toJson(new ChessGame());
            try (Connection c = DatabaseManager.getConnection()) {
                try (var query = c.prepareStatement(createUserString)){
                    query.setString(1, null);
                    query.setString(2, null);
                    query.setString(3, name);
                    query.setString(4, chessGameString);

                    query.executeUpdate();
                }

                gameID = queryGameByName(c, name);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Game creation failed");
        }

        return gameID;
    }

    int queryGameByName(Connection c, String name) throws DataAccessException {
        String getGameString = """
                SELECT id FROM game WHERE gameName=?
                """;

        int gameID = 0;
        try (var query = c.prepareStatement(getGameString)) {
            query.setString(1, name);

            try (var result = query.executeQuery()) {
                while (result.next()) {
                    gameID =  result.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

        return gameID;
    }

    private boolean stringIsSanitized(String data) {
        return data.matches("[a-zA-Z0-9]+");
    }

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
