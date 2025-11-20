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

    public void routeUserInput(String[] inputArgs) {
        this.showHelp = false;

        if (this.userService.authData != null){
            switch (inputArgs[0]) {
                case "help":
                    this.showHelp = true;
                    break;
                default:
                    System.out.println("Invalid input, please type help to see the list of valid inputs");

            }
        } else {
            switch (inputArgs[0]) {
                case "help":
                    this.showHelp = true;
                    break;
                default:
                    System.out.println("Invalid input, please type help to see the list of valid inputs");

            }
        }

    }

}


