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
            System.out.print(">>> ");

            String input = inputScanner.nextLine();

            if (Objects.equals(input, "quit")) {
                isRunning = false;
                continue;
            }

            System.out.println(input);
        }
    }
}