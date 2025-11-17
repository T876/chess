package service;

import dataaccess.*;
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
        try {
            userDAO = new SQLUserDAO();
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

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
