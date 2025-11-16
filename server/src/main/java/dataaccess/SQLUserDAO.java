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
        if (userDataIsSanitized(data)) {
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
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
    };

    String queryUsers(Connection c, String username) throws DataAccessException {
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
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

        return null;
    }


    private boolean userDataIsSanitized(UserData data) {
        return data.username().matches("[a-zA-Z]+")
                && data.password().matches("[a-zA-Z]+")
                && data.email().matches("[a-zA-Z]+");
    }

    public boolean verifyUser(String username, String password) throws DataAccessException {
        String hashedPass;

        if (loginDataIsSanitized(username, password)) {
            try(Connection c = DatabaseManager.getConnection()) {
                hashedPass = queryUsers(c, username);
                if (hashedPass == null) {
                    throw new DataAccessException("Error: Unauthorized");
                }

                if (BCrypt.checkpw(password, hashedPass)) {
                    return true;
                }
            } catch (Exception e) {
                throw new DataAccessException(e.getMessage());
            }
        }

        throw new DataAccessException("Error: Unauthorized");
    }

    private boolean loginDataIsSanitized(String username, String password ) {
        return username.matches("[a-zA-Z]+")
               && password.matches("[a-zA-Z]+");
    }

    public void clear() {
        String queryString = "TRUNCATE TABLE users";
        try (Connection c = DatabaseManager.getConnection()) {
            try (var query = c.prepareStatement(queryString)){
                query.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } catch (DataAccessException e) {
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
