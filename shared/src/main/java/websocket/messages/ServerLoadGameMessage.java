package websocket.messages;

import chess.ChessGame;

public class ServerLoadGameMessage extends ServerMessage{
    ChessGame game;

    public ServerLoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getChessGame() {
        return game;
    }
}
