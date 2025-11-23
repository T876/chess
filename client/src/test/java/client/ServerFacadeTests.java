package client;

import com.google.gson.Gson;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;

    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void setup() {
        facade.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Register works")
    public void registerSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            facade.register("username", "password", "email");
        });
    }

    @Test
    @Order(2)
    @DisplayName("Register no duplicates")
    public void registerNoDuplicates() {
        facade.register("username", "password", "email");

        Assertions.assertThrows(RuntimeException.class, () -> {
            facade.register("username", "password", "email");
        });
    }

    @Test
    @Order(3)
    @DisplayName("Login Success")
    public void loginSuccess() {
        facade.register("username", "password", "email");

        Assertions.assertDoesNotThrow(() -> {
            facade.login("username", "password");
        });
    }

    @Test
    @Order(4)
    @DisplayName("Login Unauthorized")
    public void loginUnauthorized() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            facade.login("username", "password");
        });
    }

    @Test
    @Order(5)
    @DisplayName("Logout Success")
    public void logoutSuccess() {
        AuthData response = facade.register("username", "password", "email");
        Assertions.assertDoesNotThrow(() -> {
            facade.logout(response.authToken());
        });
    }

    @Test
    @Order(6)
    @DisplayName("Logout Unauthorized")
    public void logoutUnauthorized() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            facade.logout("fakeToken");
        });
    }

    @Test
    @Order(7)
    @DisplayName("List Success")
    public void listSuccess() {
        AuthData response = facade.register("username", "password", "email");
        Assertions.assertDoesNotThrow(() -> {
            facade.listGames(response.authToken());
        });
    }

    @Test
    @Order(8)
    @DisplayName("List Unauthorized")
    public void listUnauthorized() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            facade.listGames("fakeToken");
        });
    }

    @Test
    @Order(9)
    @DisplayName("Create Success")
    public void createSuccess() {
        AuthData response = facade.register("username", "password", "email");
        Assertions.assertDoesNotThrow(() -> {
            facade.createGame(response.authToken(), "game1");
        });
    }

    @Test
    @Order(10)
    @DisplayName("Create Unauthorized")
    public void createUnauthorized() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            facade.createGame("fakeToken", "game1");
        });
    }

    @Test
    @Order(11)
    @DisplayName("Join Success")
    public void joinSuccess() {
        AuthData response = facade.register("username", "password", "email");
        int id = facade.createGame(response.authToken(), "game1");
        Assertions.assertDoesNotThrow(() -> {
            facade.joinGame(response.authToken(), id, "WHITE");
        });
    }

    @Test
    @Order(12)
    @DisplayName("Join Unauthorized")
    public void joinUnauthorized() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            facade.joinGame("fakeToken", 1, "WHITE");
        });
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
