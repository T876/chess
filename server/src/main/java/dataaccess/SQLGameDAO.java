package dataaccess;

import dataaccess.interfaces.IGameDAO;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGameDAO implements IGameDAO {

    public SQLGameDAO() throws DataAccessException {
        this.setupTable();
    };

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
