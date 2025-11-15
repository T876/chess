package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.interfaces.IAuthDAO;
import dataaccess.interfaces.IGameDAO;
import dataaccess.interfaces.IUserDAO;
import org.junit.jupiter.api.*;

public class DestructionServiceTests {
    private static IUserDAO userDAO;
    private static IAuthDAO authDAO;
    private static IGameDAO gameDAO;
    private static DestructionService destructionService;

    @BeforeAll
    public static void init() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        destructionService = new DestructionService(authDAO, userDAO, gameDAO);
    }

    @Test
    @Order(1)
    @DisplayName("Clear success")
    public void clearSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            destructionService.clearApplication();
        });
    }
}
