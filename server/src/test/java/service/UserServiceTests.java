package service;

import dataaccess.*;
import dataaccess.interfaces.IAuthDAO;
import dataaccess.interfaces.IUserDAO;
import org.junit.jupiter.api.*;
import service.models.LoginRequest;
import service.models.LoginResponse;
import service.models.RegisterRequest;
import service.models.RegisterResponse;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTests {
    private static IUserDAO userDAO;
    private static IAuthDAO authDAO;
    private static UserService userService;

    @BeforeAll
    public static void init() {
        try{
            userDAO = new SQLUserDAO();
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        userService = new UserService(authDAO, userDAO);
    }

    @BeforeEach
    public void setup() {
        userDAO.clear();
        authDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Register Success")
    public void registerSuccess() {
        RegisterRequest request = new RegisterRequest("username", "password", "email");
        RegisterResponse response;

        try {
            response = userService.register(request);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(request.username(), response.username());
        Assertions.assertNotNull(response.authToken());
    }

    @Test
    @Order(2)
    @DisplayName("Register Same User")
    public void registerMalformed() {
        RegisterRequest request = new RegisterRequest("username", "password", "email");
        RegisterRequest requestDuplicate = new RegisterRequest("username", "password", "email");

        try {
            userService.register(request);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        assertThrows(DataAccessException.class, () -> {
            userService.register(requestDuplicate);
        });
    }

    @Test
    @Order(3)
    @DisplayName("Login Success")
    public void loginSuccessful() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        LoginRequest loginRequest = new LoginRequest("username", "password");
        LoginResponse response;

        try {
            userService.register(registerRequest);
            response = userService.login(loginRequest);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(loginRequest.username(), response.username());
        Assertions.assertNotNull(response.authToken());
    }

    @Test
    @Order(4)
    @DisplayName("Login unauthorized")
    public void loginUnauthorized() {
        LoginRequest loginRequest = new LoginRequest("username", "password");

        assertThrows(DataAccessException.class, () -> {
            userService.login(loginRequest);
        });
    }

    @Test
    @Order(5)
    @DisplayName("Logout Success")
    public void logoutSuccessful() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        RegisterResponse response;

        try {
            response = userService.register(registerRequest);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertDoesNotThrow(() -> {
            userService.logout(response.authToken());
        });
    }

    @Test
    @Order(6)
    @DisplayName("Logout Unauthorized")
    public void logoutUnauthorized() {
        assertThrows(DataAccessException.class, () -> {
            userService.logout("fakeToken");
        });
    }
}
