package dataaccess;

import dataaccess.interfaces.IAuthDAO;
import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements IAuthDAO {
    public SQLAuthDAO () throws DataAccessException {
        this.setupTable();
    }

    public AuthData makeAuthData(String username) {
        String createUserString  = """
                INSERT INTO auth (username, authToken) VALUES(?, ?);
                """;
        String uuid =  UUID.randomUUID().toString();
        if (stringIsSanitized(username)) {
            try (Connection c = DatabaseManager.getConnection()) {
                try (var query = c.prepareStatement(createUserString)){
                    query.setString(1, username);
                    query.setString(2, uuid);

                    query.executeUpdate();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Session creation failed");
        }

        return new AuthData(uuid, username);
    }

    private boolean stringIsSanitized(String data) {
        return data.matches("[a-zA-Z0-9]+");
    }

    public AuthData verifyAuthToken(String authToken) throws DataAccessException {
        String username;
        try (var c = DatabaseManager.getConnection()) {
            username = queryAuthData(c, authToken);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        if (username == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        return new AuthData(authToken, username);
    };

    String queryAuthData(Connection c, String authToken) {
        String getUserString = """
                SELECT username, authToken FROM auth WHERE authToken=?
                """;

        try (var query = c.prepareStatement(getUserString)) {
            query.setString(1, authToken);
            try (var result = query.executeQuery()) {
                if (result.next()) {
                    return result.getString("username");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void logout(String authToken) throws DataAccessException {
        String queryString = "DELETE FROM auth WHERE authToken=?";
        try (Connection c = DatabaseManager.getConnection()) {
            try(var query = c.prepareStatement(queryString)) {
                query.setString(1, authToken);
                query.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    public void clear() {
        String queryString = "TRUNCATE TABLE auth";
        try (Connection c = DatabaseManager.getConnection()) {
            try (var query = c.prepareStatement(queryString)){
                query.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    private void setupTable() throws DataAccessException {
        DatabaseManager.createDatabase();
        String createTableString = """
                CREATE TABLE IF NOT EXISTS auth(
                    id INT NOT NULL AUTO_INCREMENT,
                    authToken VARCHAR(255),
                    username VARCHAR(255),
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
