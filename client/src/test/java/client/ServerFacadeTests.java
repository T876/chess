package client;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import server.Server;
import ui.server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;

    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(new Gson());
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

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
