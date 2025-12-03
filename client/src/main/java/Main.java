import com.google.gson.Gson;
import ui.WebsocketRouter;
import ui.server.ServerFacade;
import ui.server.WebsocketClient;
import ui.server.WebsocketFacade;
import ui.service.GameService;
import ui.service.UserService;
import ui.Router;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Services and Utils
        boolean isRunning = true;
        Scanner inputScanner = new Scanner(System.in);
        ServerFacade facade = new ServerFacade(8080);
        WebsocketClient wsClient;
        WebsocketFacade wsFacade;

        try {
            wsClient = new WebsocketClient();
            wsFacade = new WebsocketFacade(wsClient);
        } catch (Exception e) {
            System.out.println("Connection to server failed. Please try again and make sure you are online");
            return;
        }

        UserService userService = new UserService(facade);
        GameService gameService = new GameService(facade, wsFacade);
        Router router = new Router(userService, gameService, wsFacade);
        WebsocketRouter wsRouter = new WebsocketRouter(gameService, userService, wsClient);
        wsRouter.startMessageListener();

        System.out.println("♕ Welcome to Chess ♕");

        while (isRunning) {
            if (router.showHelp) {
                Main.printHelp(userService.authData != null);
            }

            String inputThing = userService.authData == null ?
                    "[LOGGED_OUT]" :
                    "[" + userService.authData.username() + "]";

            if (gameService.shouldDrawInputThing) {
                System.out.print(inputThing + " >>> ");
            }
            gameService.shouldDrawInputThing = true;


            String input = inputScanner.nextLine();

            if (Objects.equals(input, "quit")) {
                isRunning = false;
                continue;
            }

            try {
                if (gameService.selectedGame != null) {
                    router.routeInGameInput(input.split(" "));
                } else {
                    router.routeUserInput(input.split(" "));
                }
            } catch (Exception e) {
                if (e.getMessage().contains("For input string")) {
                    System.out.println("Game ID must be a number");
                } if (e.getMessage().contains("Game not found")){
                    System.out.println("Game not found");
                } else {
                    System.out.println(e.getMessage());
                }

                System.out.println("Type 'help' for more info");
            }
        }
    }

    private static void printHelp(boolean isLoggedIn) {
        if (isLoggedIn) {
            System.out.println("create <NAME>");
            System.out.println("list - list all games");
            System.out.println("join <ID> <WHITE/BLACK> - join a game");
            System.out.println("observe <ID> - start observing a game");
            System.out.println("logout - log out of the app");
            System.out.println("quit - close app");
            System.out.println("help");
        } else {
            System.out.println("register <USERNAME> <PASSWORD> <EMAIL>");
            System.out.println("login <USERNAME> <PASSWORD>");
            System.out.println("quit - close app");
            System.out.println("help");
        }
    }
}