import chess.*;
import com.google.gson.Gson;
import server.ServerFacade;
import service.GameService;
import service.UserService;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Services and Utils
        boolean isRunning = true;
        Scanner inputScanner = new Scanner(System.in);
        ServerFacade facade = new ServerFacade(new Gson());
        UserService userService = new UserService(facade);
        GameService gameService = new GameService(facade);
        Router router = new Router(userService, gameService);

        System.out.println("♕ 240 Chess Client ♕");

        while (isRunning) {
            if (router.showHelp) {
                Main.printHelp(userService.authData != null);
            }

            String inputThing = userService.authData == null ?
                    "[LOGGED_OUT]" :
                    "[" + userService.authData.username() + "]";

            System.out.print(inputThing + " >>> ");

            String input = inputScanner.nextLine();

            if (Objects.equals(input, "quit")) {
                isRunning = false;
                continue;
            }

            try {
                router.routeUserInput(input.split(" "));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Type 'help' for more info");
            }
        }
    }

    private static void printHelp(boolean isLoggedIn) {
        if (isLoggedIn) {
            System.out.println("create <NAME>");
            System.out.println("list");
            System.out.println("join <ID> <WHITE/BLACK>");
            System.out.println("logout");
            System.out.println("quit");
            System.out.println("help");
        } else {
            System.out.println("register <USERNAME> <PASSWORD> <EMAIL>");
            System.out.println("login <USERNAME> <PASSWORD>");
            System.out.println("quit");
            System.out.println("help");
        }
    }
}