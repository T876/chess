package service;

import dataaccess.*;
import dataaccess.interfaces.IAuthDAO;
import dataaccess.interfaces.IGameDAO;
import dataaccess.interfaces.IUserDAO;
import model.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameServiceTests {
    private static IAuthDAO authDAO;
    private static IGameDAO gameDAO;
    private static IUserDAO userDAO;
    private static GameService gameService;
    private static UserService userService;
    private String authToken;

    @BeforeAll
    public static void init() {
        try {
            userDAO = new SQLUserDAO();
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        gameDAO = new MemoryGameDAO();
        gameService = new GameService(authDAO, gameDAO);
        userService = new UserService(authDAO, userDAO);
    }

    @BeforeEach
    public void setup() {
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();

        RegisterRequest request = new RegisterRequest("username", "password", "email");

        try {
            this.authToken = userService.register(request).authToken();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    @Order(1)
    @DisplayName("List games when empty success")
    public void listGamesWhenEmpty() {
        GameListResponse response;

        try{
            response = gameService.listGames(this.authToken);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(new ArrayList<>(), response.games());
    }

    @Test
    @Order(2)
    @DisplayName("List games unauthorized")
    public void listGamesUnauthorized() {
        assertThrows(DataAccessException.class, () -> {
            gameService.listGames("fakeToken");
        });
    }

    @Test
    @Order(3)
    @DisplayName("Create Game Successful")
    public void createGameSuccessful() {
        CreateGameRequest request = new CreateGameRequest("Game1");
        CreateGameResponse response;

        try {
            response = gameService.createGame(this.authToken, request);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(1, response.gameID());
    }

    @Test
    @Order(4)
    @DisplayName("Create game unauthorized")
    public void createGameUnauthorized() {
        assertThrows(DataAccessException.class, () -> {
            gameService.createGame("fakeToken", new CreateGameRequest("Game1"));
        });
    }

    @Test
    @Order(5)
    @DisplayName("Join game Successful")
    public void joinGameSuccessful() {
        CreateGameRequest request = new CreateGameRequest("Game1");
        CreateGameResponse createResponse;

        try {
            createResponse = gameService.createGame(this.authToken, request);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        JoinGameRequest joinRequest = new JoinGameRequest("WHITE", createResponse.gameID());

        Assertions.assertDoesNotThrow(() -> {
            gameService.joinGame(this.authToken, joinRequest);
        });
    }

    @Test
    @Order(6)
    @DisplayName("Create game unauthorized")
    public void joinGameUnauthorized() {
        assertThrows(DataAccessException.class, () -> {
            gameService.joinGame("fakeToken", new JoinGameRequest("WHITE", 1));
        });
    }
}
