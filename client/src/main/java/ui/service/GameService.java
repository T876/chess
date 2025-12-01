package ui.service;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameInfo;
import ui.EscapeSequences;
import ui.server.ServerFacade;
import ui.server.WebsocketFacade;

import java.util.List;
import java.util.Objects;

public class GameService {
    private final ServerFacade server;
    private final WebsocketFacade websocket;
    public ChessGame selectedGame;
    public ChessGame.TeamColor color;
    private List<GameInfo> gamesList;

    public GameService(ServerFacade server, WebsocketFacade websocket) {
        this.server = server;
        this.websocket = websocket;
    }

    public int createGame(String name, String authToken) {
        int gameID = this.server.createGame(authToken, name);
        List<GameInfo> games = this.server.listGames(authToken);
        int counter = 1;

        for (GameInfo game : games) {
            if (game.gameID() == gameID) {
                return counter;
            }

            counter++;
        }
        throw new RuntimeException("Game creation failed. Please try again");
    }

    public List<GameInfo> listGames(String authToken) {
        return this.server.listGames(authToken);
    }

    public void joinGame(int gameIndex, String teamColor, String authToken) {
        this.gamesList = this.server.listGames(authToken);
        GameInfo gameToJoin = this.gamesList.get(gameIndex - 1);

        this.server.joinGame(authToken, gameToJoin.gameID(), teamColor);
        this.websocket.sendJoinGameCommand(authToken, gameToJoin.gameID(), teamColor);
        this.selectedGame = new ChessGame();
        this.color = Objects.equals(teamColor, "WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
    }

    public void observeGame(int gameIndex, String authToken) {
        this.gamesList = this.server.listGames(authToken);
        try {
            gamesList.get(gameIndex);
        } catch (Exception e) {
            throw new RuntimeException("Game does not exist");
        }
        this.selectedGame = new ChessGame();
    }

    public void printGame() {
        if (this.color == ChessGame.TeamColor.BLACK) {
            printBlack();
        } else {
            printWhite();
        }
    }

    private void printCharRow(String rowString) {
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print(rowString);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.println();
    }

    private void printRowDigit(int i, boolean newLine) {
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print(" " + i + " ");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        if (newLine) {
            System.out.println();
        }
    }

    private void printRow(int rowNum, ChessGame.TeamColor color) {
        int flipper = rowNum % 2;

        if (color == ChessGame.TeamColor.BLACK) {
            if (flipper == 1) {
                flipper = 0;
            } else {
                flipper = 1;
            }
        }

        printRowDigit(rowNum, false);

        for (int j = 1; j <= 8; j++) {

            if (flipper == 1) {
                System.out.print(EscapeSequences.SET_BG_COLOR_BLUE);
            } else{
                System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            }

            ChessPiece piece;

            int colNum;

            if (color == ChessGame.TeamColor.WHITE) {
                colNum = j;
                piece = this.selectedGame.getBoard().getPiece(
                        new ChessPosition(rowNum, colNum)
                );
            } else {
                colNum = 9-j;
                piece = this.selectedGame.getBoard().getPiece(
                        new ChessPosition(rowNum, colNum)
                );
            }

            if (piece == null) {
                System.out.print("   ");
            } else {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
                } else {
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                }
                System.out.print(piece);
            }

            if (flipper == 1) {
                flipper = 0;
            } else {
                flipper = 1;
            }
        }

        printRowDigit(rowNum, true);
    }

    private void printBlack() {
        System.out.println(EscapeSequences.ERASE_SCREEN);
        String rowString = "    h  g  f  e  d  c  b  a    ";

        this.printCharRow(rowString);
        for (int i = 1; i <= 8 ; i++) {
            printRow(i, ChessGame.TeamColor.BLACK);
        }

        this.printCharRow(rowString);

    }

    private void printWhite() {
        System.out.println(EscapeSequences.ERASE_SCREEN);
        String rowString = "    a  b  c  d  e  f  g  h    ";

        printCharRow(rowString);
        for (int i = 8; i >= 1 ; i--) {
            printRow(i, ChessGame.TeamColor.WHITE);
        }
        printCharRow(rowString);
    }
}
