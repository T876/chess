import chess.ChessGame;
import service.GameService;
import service.UserService;

import java.util.List;

public class Router {
    private UserService userService;
    private GameService gameService;

    public boolean showHelp;
    public boolean quit;

    public Router(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public void routeUserInput(String[] inputArgs) throws InputException{
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
                        throw new InputException("Error: Please enter only the game name to create a game");
                    }

                    if (this.gameService.createGame(inputArgs[1], userService.authData.authToken())) {
                        System.out.println("Game created successfully");
                    } else {
                        System.out.println("Game creation failed. Please try again.");
                    }
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
                        throw new InputException("Error: Please enter username, password and email to register");
                    }

                    this.userService.register(inputArgs[1], inputArgs[2], inputArgs[3]);
                    break;
                case "login":
                    if (inputArgs.length != 3) {
                        throw new InputException("Error: Please enter username and password to log in");
                    }

                    this.userService.login(inputArgs[1], inputArgs[2]);
                    break;
                default:
                    System.out.println("Invalid input, please type help to see the list of valid inputs");

            }
        }

    }

}


