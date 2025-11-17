package dataaccess;

import dataaccess.interfaces.IUserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLUserDAO implements IUserDAO {

    public SQLUserDAO() throws DataAccessException {
        this.setupTable();
    }

    public void createUser(UserData data) throws DataAccessException{
        String createUserString  = """
                INSERT INTO users (username, password, email) VALUES(?, ?, ?);
                """;
        try (Connection c = DatabaseManager.getConnection()) {
            if (queryUsers(c, data.username()) != null) {
                throw new DataAccessException("Error: already taken");
            }
            try (var query = c.prepareStatement(createUserString)){
                query.setString(1, data.username());
                query.setString(2, BCrypt.hashpw(data.password(), BCrypt.gensalt()));
                query.setString(3, data.email());

                query.executeUpdate();
            }
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    String queryUsers(Connection c, String username) {
        String getUserString = """
                SELECT username, password FROM users WHERE username=?
                """;

        try (var query = c.prepareStatement(getUserString)) {
            query.setString(1, username);
            try (var result = query.executeQuery()) {
                if (result.next()) {
                    return result.getString("password");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    public boolean verifyUser(String username, String password) throws DataAccessException {
        String hashedPass;

        try(Connection c = DatabaseManager.getConnection()) {
            hashedPass = queryUsers(c, username);
            if (hashedPass == null) {
                throw new DataAccessException("Error: unauthorized");
            }

            if (BCrypt.checkpw(password, hashedPass)) {
                return true;
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

        throw new DataAccessException("Error: unauthorized");
    }

    public void clear() {
        String queryString = "TRUNCATE TABLE users";
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
