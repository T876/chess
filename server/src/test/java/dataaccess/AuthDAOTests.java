package dataaccess;

import dataaccess.interfaces.IAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.*;

public class AuthDAOTests {
    private static IAuthDAO authDAO;

    @BeforeAll
    public static void init() {
        try {
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() {
        authDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Make Auth Data Success")
    public void makeAuthDataSuccess() {
        AuthData result;
        try{
            result = authDAO.makeAuthData("username");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals("username", result.username());
    }

    @Test
    @Order(2)
    @DisplayName("Make Auth Data SQL injection")
    public void makeAuthDataSQLInjection() {
        Assertions.assertThrows(RuntimeException.class, () ->{
            authDAO.makeAuthData("username); NOW I DELETE THEM ALL");
        });
    }

    @Test
    @Order(3)
    @DisplayName("Verify Auth Token Success")
    public void verifyAuthTokenSuccess() {
        AuthData result;
        try{
            result = authDAO.makeAuthData("username");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertDoesNotThrow(() -> {
            authDAO.verifyAuthToken(result.authToken());
        });
    }

    @Test
    @Order(4)
    @DisplayName("Verify Auth Token Failure")
    public void verifyAuthTokenFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.verifyAuthToken("Fake Token");
        });
    }

    @Test
    @Order(5)
    @DisplayName("Logout Success")
    public void logoutSuccess() {
        AuthData result;
        int logoutResult;
        try{
            result = authDAO.makeAuthData("username");
            logoutResult = authDAO.logout(result.authToken());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(1, logoutResult);
    }

    @Test
    @Order(6)
    @DisplayName("Logout Failure")
    public void logoutFailure() {
        int logoutResult;
        try{
            logoutResult = authDAO.logout("fake token");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(0, logoutResult);
    }

    @Test
    @Order(7)
    @DisplayName("Clear Success")
    public void clearSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            authDAO.clear();
        });
    }
}
