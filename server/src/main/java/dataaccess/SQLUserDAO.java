package dataaccess;

import dataaccess.interfaces.IUserDAO;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLUserDAO implements IUserDAO {

    public SQLUserDAO() throws DataAccessException {
        this.setupTable();
    }

    public void createUser(UserData data) throws DataAccessException{ };

    public boolean verifyUser(String username, String password) throws DataAccessException {
        return true;
    };

    public void clear() { };

    private void setupTable() throws DataAccessException {
        DatabaseManager.createDatabase();
        String createTableString = """
                CREATE TABLE IF NOT EXISTS users(
                    id INT NOT NULL AUTO_INCREMENT,
                    username VARCHAR(255),
                    password VARCHAR(255),
                    email VARCHAR(255),
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
