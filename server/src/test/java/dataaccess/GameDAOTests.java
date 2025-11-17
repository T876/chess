package dataaccess;

import dataaccess.interfaces.IAuthDAO;
import dataaccess.interfaces.IGameDAO;
import dataaccess.interfaces.IUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.ArrayList;

public class GameDAOTests {
    private static IGameDAO gameDAO;

    @BeforeAll
    public static void init() {
        try {
            gameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() {
        gameDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Create Game Success")
    public void createGameSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            gameDAO.createGame("name");
        });
    }

    @Test
    @Order(2)
    @DisplayName("Create Game Failure")
    public void createGameFailure() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            gameDAO.createGame(null);
        });
    }

    @Test
    @Order(3)
    @DisplayName("List Games Success")
    public void listGamesSuccess() {
        Assertions.assertEquals(new ArrayList<>(), gameDAO.getAllGames());
    }

    @Test
    @Order(4)
    @DisplayName("List Games Failure")
    public void listGamesFailure() {
        String createGameString  = """
                INSERT INTO game (whiteUsername, blackUsername, gameName, chessGame) VALUES(?, ?, ?, ?);
                """;
        try (Connection c = DatabaseManager.getConnection()) {
            try (var query = c.prepareStatement(createGameString)){
                query.setString(1, null);
                query.setString(2, null);
                query.setString(3, "testName");
                query.setString(4, "Not JSON");

                query.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertThrows(RuntimeException.class, () ->  {
            gameDAO.getAllGames();
        });
    }

    @Test
    @Order(5)
    @DisplayName("Join Game Success")
    public void joinGameSuccess() {
        gameDAO.createGame("name");
        Assertions.assertDoesNotThrow(() -> {
            gameDAO.joinGame("WHITE", 1, "username");
        });
    }

    @Test
    @Order(6)
    @DisplayName("Join Game Failure")
    public void joinGameFailure() {
        gameDAO.createGame("name");

        try {
            gameDAO.joinGame("WHITE", 1, "username");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.joinGame("WHITE", 1, "username");
        });
    }

    @Test
    @Order(7)
    @DisplayName("Clear Success")
    public void clearSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            gameDAO.clear();
        });
    }
}
