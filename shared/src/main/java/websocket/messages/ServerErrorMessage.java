package websocket.messages;

import chess.ChessGame;

public class ServerErrorMessage extends ServerMessage  {
    String errorMessage;

    public ServerErrorMessage(String message) {
        super(ServerMessage.ServerMessageType.ERROR);
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
