package dataaccess;

import dataaccess.interfaces.IAuthDAO;
import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLAuthDAO implements IAuthDAO {
    public SQLAuthDAO () throws DataAccessException {
        this.setupTable();
    }

    public AuthData makeAuthData(String username) {
        return new AuthData("token", "username");
    }

    public AuthData verifyAuthToken(String authToken) throws DataAccessException {
        return new AuthData("token", "username");
    };

    public void logout(String authToken) throws DataAccessException {};

    public void clear() {};

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
