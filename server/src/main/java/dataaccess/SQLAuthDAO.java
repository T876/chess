package dataaccess;

import dataaccess.interfaces.IAuthDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
        return data.matches("[a-zA-Z]+");
    }

    public AuthData verifyAuthToken(String authToken) throws DataAccessException {
        return new AuthData("token", "username");
    };

    public void logout(String authToken) throws DataAccessException {};

    public void clear() {
        String queryString = "TRUNCATE TABLE auth";
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
