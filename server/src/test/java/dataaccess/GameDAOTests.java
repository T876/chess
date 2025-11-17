package dataaccess;

import dataaccess.interfaces.IUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;

public class GameDAOTests {
    private static IUserDAO userDAO;

    @BeforeAll
    public static void init() {
        try{
            userDAO = new SQLUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() {
        userDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Create User Success")
    public void createUserSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            userDAO.createUser(new UserData("username", "password", "email"));
        });
    }

    @Test
    @Order(2)
    @DisplayName("Create User Fails Duplicate")
    public void createUserDuplicate() {
        try{
            userDAO.createUser(new UserData("username", "password", "email"));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(new UserData("username", "password", "email"));
        });
    }

    @Test
    @Order(3)
    @DisplayName("Verify User Success")
    public void verifyUserSuccess() {
        boolean result = false;
        try{
            userDAO.createUser(new UserData("username", "password", "email"));
            result = userDAO.verifyUser("username", "password");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertTrue(result);
    }

    @Test
    @Order(4)
    @DisplayName("Verify User failure")
    public void verifyUserFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.verifyUser("username", "password");
        });
    }

    @Test
    @Order(5)
    @DisplayName("Clear Success")
    public void clearSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            userDAO.clear();
        });
    }

}
