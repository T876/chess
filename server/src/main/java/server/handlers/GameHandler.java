package server.handlers;
import com.google.gson.Gson;
import io.javalin.http.Context;
import service.models.CreateGameRequest;
import service.models.JoinGameRequest;

public class GameHandler {
    private final Gson serializer;

    public GameHandler() {
        this.serializer = new Gson();
    }

    public void listGames(Context context) {
        String response = serializer.toJson(new Object());
        context.status(200);
        context.json(response);
    }

    public void createGame(Context context) {
        CreateGameRequest request = serializer.fromJson(context.body(), CreateGameRequest.class);
        String response = serializer.toJson(new Object());
        context.status(200);
        context.json(response);
    }

    public void joinGame(Context context) {
        JoinGameRequest request = serializer.fromJson(context.body(), JoinGameRequest.class);
        String response = serializer.toJson(new Object());
        context.status(200);
        context.json(response);
    }
}
