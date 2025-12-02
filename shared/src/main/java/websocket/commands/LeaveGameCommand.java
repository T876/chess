package websocket.commands;

import chess.ChessGame;

public class LeaveGameCommand extends UserGameCommand {
    public final String color;

    public LeaveGameCommand(String color, String authToken, int gameID) {
        super(CommandType.LEAVE, authToken, gameID);
        this.color = color;
    }
}
