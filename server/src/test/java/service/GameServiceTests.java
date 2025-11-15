package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.interfaces.IAuthDAO;
import dataaccess.interfaces.IGameDAO;
import dataaccess.interfaces.IUserDAO;
import org.junit.jupiter.api.*;
import service.models.GameListResponse;
import service.models.RegisterRequest;

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
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
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
}
