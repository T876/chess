package ui;

import chess.ChessGame;
import model.GameData;
import model.GameInfo;
import ui.server.WebsocketFacade;
import ui.service.GameService;
import ui.service.UserService;

import java.util.List;
import java.util.Objects;

public class Router {
    private UserService userService;
    private GameService gameService;
    private WebsocketFacade wsFacade;

    public boolean showHelp;

    public Router(UserService userService, GameService gameService, WebsocketFacade wsFacade) {
        this.userService = userService;
        this.gameService = gameService;
        this.wsFacade = wsFacade;
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

                    int gameID = this.gameService.createGame(inputArgs[1], userService.authData.authToken());
                    System.out.println("Game created successfully. Index: " + gameID);
                    break;
                case "list":
                    List<GameInfo> games = gameService.listGames(userService.authData.authToken());
                    int counter = 1;
                    for (GameInfo game : games) {
                        System.out.print(counter + ". ");
                        System.out.println(game.gameName());
                        System.out.println("White: " + game.whiteUsername());
                        System.out.println("Black: " + game.blackUsername());
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

                    int gameIDNum = convertIDToInt(inputArgs[1]);

                    this.gameService.joinGame(gameIDNum, inputArgs[2],
                            this.userService.authData.authToken());

                    this.gameService.printGame();

                    break;
                case "observe":
                    if (inputArgs.length != 2) {
                        throw new RuntimeException("Please enter only the game id you want to observe");
                    }

                    int observeIDNum = convertIDToInt(inputArgs[1]) - 1;

                    this.gameService.observeGame(observeIDNum, this.userService.authData.authToken());

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

    public void routeInGameInput(String[] inputArgs) {
        if (!Objects.equals(inputArgs[0], "leave")) {
            this.gameService.printGame();
        } else {
            System.out.println("You left the game.");
        }


        switch (inputArgs[0]) {
            case "help" -> this.printInGameHelp();
            case "redraw" -> System.out.println();
            case "leave" -> this.leaveGame();
            default -> System.out.println("invalid input");
        }
    }

    private void printInGameHelp() {
        System.out.println("redraw - redraw chess board");
        System.out.println("leave - leave the game");
        if (this.gameService.color != null) {
            System.out.println("move - <ROW 1-8> <COL a-h>");
            System.out.println("resign - resign game");
        }
        System.out.println("help");
    }

    private int convertIDToInt(String userInput) {
        try {
            return Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Game ID must be a number");
        }
    }

    private void leaveGame(){
        String colorString;
        if (this.gameService.color == ChessGame.TeamColor.WHITE) {
            colorString = "WHITE";
        } else if (this.gameService.color == ChessGame.TeamColor.BLACK) {
            colorString = "BLACK";
        } else {
            colorString = "NONE";
        }

        this.wsFacade.sendLeaveGameCommand(
                userService.authData.authToken(),
                gameService.selectedGameId,
                colorString);
        this.gameService.leaveGame();
    }
}


