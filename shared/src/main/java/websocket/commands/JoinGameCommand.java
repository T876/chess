package websocket.commands;

public class JoinGameCommand extends UserGameCommand {
    String joinGameAs;

    public JoinGameCommand(String authToken, Integer gameID, String joinGameAs) {
        super(CommandType.CONNECT, authToken, gameID);
        this.joinGameAs = joinGameAs;
    }
}
