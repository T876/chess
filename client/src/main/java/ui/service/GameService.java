package ui.service;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import model.GameInfo;
import ui.EscapeSequences;
import ui.server.ServerFacade;

import java.util.List;
import java.util.Objects;

public class GameService {
    private ServerFacade server;
    public ChessGame selectedGame;
    public ChessGame.TeamColor color;

    public GameService(ServerFacade server) {
        this.server = server;
    }

    public int createGame(String name, String authToken) {
        return this.server.createGame(authToken, name);
    }

    public List<GameInfo> listGames(String authToken) {
        return this.server.listGames(authToken);
    }

    public void joinGame(int gameID, String teamColor, String authToken) {
        this.server.joinGame(authToken, gameID, teamColor);
        ChessGame game = new ChessGame();
        this.selectedGame = game;
        this.color = Objects.equals(teamColor, "WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
    }

    public void observeGame(int gameID, String authToken) {
        ChessGame game = new ChessGame();
        this.selectedGame = game;
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

    private void printBlack() {
        System.out.println(EscapeSequences.ERASE_SCREEN);
        String rowString = "    h  g  f  e  d  c  b  a    ";

        this.printCharRow(rowString);
        for (int i = 1; i <= 8 ; i++) {
            int flipper = i % 2;

            printRowDigit(i, false);

            for (int j = 1; j <= 8; j++) {

                if (flipper == 1) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_BLUE);
                } else{
                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                }

                ChessPiece piece = this.selectedGame.getBoard().getPiece(
                        new ChessPosition(i, j)
                );

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

            printRowDigit(i, true);
        }

        this.printCharRow(rowString);

    }

    private void printWhite() {
        System.out.println(EscapeSequences.ERASE_SCREEN);
        String rowString = "    a  b  c  d  e  f  g  h    ";

        printCharRow(rowString);
        for (int i = 8; i >= 1 ; i--) {
            int flipper = i % 2;

            printRowDigit(i, false);

            for (int j = 1; j <= 8; j++) {

                if (flipper == 1) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_BLUE);
                } else{
                    System.out.print(EscapeSequences.SET_BG_COLOR_RED);
                }

                ChessPiece piece = this.selectedGame.getBoard().getPiece(
                        new ChessPosition(i, j)
                );

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

            printRowDigit(i, true);
        }
        printCharRow(rowString);
    }
}
