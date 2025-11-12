package server.handlers;
import com.google.gson.Gson;
import io.javalin.http.Context;
import service.GameService;
import service.models.CreateGameRequest;
import service.models.CreateGameResponse;
import service.models.GameListResponse;
import service.models.JoinGameRequest;

public class GameHandler {
    private final Gson serializer;
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.serializer = new Gson();
        this.gameService = gameService;
    }

    public void listGames(Context context) {
        String auth = context.header("authToken");
        GameListResponse response = this.gameService.listGames(auth);
        String responseJson = serializer.toJson(response);

        context.status(200);
        context.json(responseJson);
    }

    public void createGame(Context context) {
        String auth = context.header("authToken");
        CreateGameRequest request = serializer.fromJson(context.body(), CreateGameRequest.class);
        CreateGameResponse response = this.gameService.createGame(auth, request);
        String responseJson = serializer.toJson(response);

        context.status(200);
        context.json(responseJson);
    }

    public void joinGame(Context context) {
        String auth = context.header("authToken");
        JoinGameRequest request = serializer.fromJson(context.body(), JoinGameRequest.class);
        this.gameService.joinGame(auth, request);
        String responseJson = serializer.toJson(new Object());

        context.status(200);
        context.json(responseJson);
    }
}
