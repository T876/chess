package ui;

import model.GameData;
import ui.service.GameService;
import ui.service.UserService;

import java.util.List;
import java.util.Objects;

public class Router {
    private UserService userService;
    private GameService gameService;

    public boolean showHelp;

    public Router(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public void routeUserInput(String[] inputArgs) {
        this.showHelp = false;

        if (this.userService.authData != null){
            switch (inputArgs[0]) {
                case "help":
                    this.showHelp = true;
                    break;
                case "logout":
                    this.userService.logout();
                    break;
                case "create":
                    if (inputArgs.length != 2) {
                        throw new RuntimeException("Error: Please enter only the game name to create a game");
                    }

                    if (this.gameService.createGame(inputArgs[1], userService.authData.authToken())) {
                        System.out.println("Game created successfully");
                    } else {
                        System.out.println("Game creation failed. Please try again.");
                    }
                    break;
                case "list":
                    List<GameData> games = gameService.listGames(userService.authData.authToken());
                    int counter = 1;
                    for (GameData game : games) {
                        System.out.print(counter + ". ");
                        System.out.println(game.gameName());
                        System.out.println("White: " + game.whiteUsername());
                        System.out.println("Black: " + game.blackUsername());
                        System.out.println("Id: " + game.gameID());
                        counter++;
                    }
                    if (counter == 1) {
                        System.out.println("No games to list");
                    }
                    break;
                case "join":
                    if (inputArgs.length != 3){
                        throw new RuntimeException("Join failed. Please list the game id and color you would like to play " +
                                "\n Example: join 1 WHITE");
                    }

                    if (!Objects.equals(inputArgs[2], "WHITE") && !Objects.equals(inputArgs[2], "BLACK")) {
                        throw new RuntimeException("Color must be BLACK or WHITE");
                    }

                    this.gameService.joinGame(Integer.parseInt(inputArgs[1]), inputArgs[2],
                            this.userService.authData.authToken());

                    this.gameService.printGame();

                    break;
                case "observe":
                    if (inputArgs.length != 2) {
                        throw new RuntimeException("Please enter only the game id you want to observe");
                    }

                    this.gameService.observeGame(Integer.parseInt(inputArgs[1]), this.userService.authData.authToken());

                    this.gameService.printGame();
                    break;
                default:
                    System.out.println("Invalid input, please type help to see the list of valid inputs");

            }
        } else {
            switch (inputArgs[0]) {
                case "help":
                    this.showHelp = true;
                    break;
                case "register":
                    if (inputArgs.length != 4) {
                        throw new RuntimeException("Error: Please enter username, password and email to register");
                    }

                    this.userService.register(inputArgs[1], inputArgs[2], inputArgs[3]);
                    break;
                case "login":
                    if (inputArgs.length != 3) {
                        throw new RuntimeException("Error: Please enter username and password to log in");
                    }

                    this.userService.login(inputArgs[1], inputArgs[2]);
                    break;
                default:
                    System.out.println("Invalid input, please type help to see the list of valid inputs");

            }
        }

    }
}


