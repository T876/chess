package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.interfaces.IGameDAO;
import model.AuthData;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SQLGameDAO implements IGameDAO {
    Gson serializer;

    public SQLGameDAO() throws DataAccessException {
        this.setupTable();
        this.serializer = new Gson();
    };

    // Create
    public int createGame(String name){
        String createGameString  = """
                INSERT INTO game (whiteUsername, blackUsername, gameName, chessGame) VALUES(?, ?, ?, ?);
                """;

        int gameID;

        String chessGameString = serializer.toJson(new ChessGame());
        try (Connection c = DatabaseManager.getConnection()) {
            try (var query = c.prepareStatement(createGameString)){
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

        return gameID;
    }

    private int queryGameByName(Connection c, String name) throws DataAccessException {
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

    GameData queryGameByID(Connection c, int id) throws DataAccessException {
        String getGameString = """
                SELECT id, whiteUsername, blackUsername, gameName, chessGame FROM game WHERE id=?
                """;


        try (var query = c.prepareStatement(getGameString)) {
            query.setInt(1, id);

            String whiteUsername;
            String blackUsername;
            String gameName;
            String chessGameString;
            ChessGame chessGame;

            try (var result = query.executeQuery()) {
                if (result.next()) {

                    whiteUsername = result.getString("whiteUsername");
                    blackUsername = result.getString("blackUsername");
                    gameName = result.getString("gameName");
                    chessGameString = result.getString("chessGame");

                    chessGame = serializer.fromJson(chessGameString, ChessGame.class);

                    return new GameData(id, whiteUsername, blackUsername, gameName, chessGame);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("Game not found");
    }

    // Update
    public void joinGame(String color, int gameID, String username) throws DataAccessException {
        String joinGameString;

        if (Objects.equals(color, "WHITE")) {
            joinGameString = """
                UPDATE game SET whiteUsername=? WHERE id=?
                """;
        } else {
            joinGameString = """
                UPDATE game SET blackUsername=? WHERE id=?
                """;
        }

        try (Connection c = DatabaseManager.getConnection()) {
            GameData existingGame = queryGameByID(c, gameID);
            if (Objects.equals(color, "WHITE") && existingGame.whiteUsername() != null) {
                throw new DataAccessException("Error: already taken");
            } else if (existingGame.blackUsername() != null) {
                throw new DataAccessException("Error: already taken");
            }

            try (var query = c.prepareStatement(joinGameString)){
                query.setString(1, username);
                query.setInt(2, gameID);

                query.executeUpdate();
            }
        }catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        } catch  (Exception e) {
            throw new RuntimeException(e);
        }
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
