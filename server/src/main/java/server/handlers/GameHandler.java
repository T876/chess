package server.handlers;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.CreateGameRequest;
import model.CreateGameResponse;
import model.GameListResponse;
import model.JoinGameRequest;
import service.GameService;

import java.util.Objects;

public class GameHandler {
    private final Gson serializer;
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.serializer = new Gson();
        this.gameService = gameService;
    }

    void returnErrorResponse(Context context, int status, String message) {
        context.status(status);
        context.json(serializer.toJson(new ErrorBody(message)));
    }

    public void listGames(Context context) {
        String auth;

        try {
            auth = context.header("authorization");
        } catch(Exception e) {
            returnErrorResponse(context, 400, "Error: bad request");
            return;
        }

        GameListResponse response;

        try{
            response = this.gameService.listGames(auth);
        } catch (DataAccessException e) {
            this.returnErrorResponse(context, 401, e.getMessage());
            return;
        } catch (Exception e) {
            this.returnErrorResponse(context, 500, "Error:" + e.getMessage());
            return;
        }
        String responseJson = serializer.toJson(response);

        context.status(200);
        context.json(responseJson);
    }

    public void createGame(Context context) {
        String auth;
        CreateGameRequest request;

        try {
            auth = context.header("authorization");
            request = serializer.fromJson(context.body(), CreateGameRequest.class);
        } catch(Exception e) {
            returnErrorResponse(context, 400, "Error: malformed request");
            return;
        }

        if (request.gameName() == null) {
            returnErrorResponse(context, 400, "Error: no game name provided");
            return;
        }

        CreateGameResponse response;

        try{
            response = this.gameService.createGame(auth, request);
        } catch (DataAccessException e) {
            this.returnErrorResponse(context, 401, e.getMessage());
            return;
        } catch (Exception e) {
            this.returnErrorResponse(context, 500, "Error:" + e.getMessage());
            return;
        }
        String responseJson = serializer.toJson(response);

        context.status(200);
        context.json(responseJson);
    }

    public void joinGame(Context context) {
        String auth;
        JoinGameRequest request;

        try {
            auth = context.header("authorization");
            request = serializer.fromJson(context.body(), JoinGameRequest.class);
        } catch(Exception e) {
            returnErrorResponse(context, 400, "Error: bad request");
            return;
        }

        if (joinGameRequestIsInvalid(request)) {
            returnErrorResponse(context, 400, "Error: bad request");
            return;
        }

        try{
            this.gameService.joinGame(auth, request);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: unauthorized")) {
                this.returnErrorResponse(context, 401, e.getMessage());
            } else if (Objects.equals(e.getMessage(), "Error: already taken")) {
                this.returnErrorResponse(context, 403, e.getMessage());
            }
            return;
        } catch (Exception e) {
            this.returnErrorResponse(context, 500, "Error:" + e.getMessage());
            return;
        }
        String responseJson = serializer.toJson(new Object());

        context.status(200);
        context.json(responseJson);
    }

    boolean joinGameRequestIsInvalid(JoinGameRequest request) {
        return (!Objects.equals(request.playerColor(), "WHITE")
                && !Objects.equals(request.playerColor(), "BLACK"))
                // Included to ensure game ID is always passed in. This will never be zero for a real game
                || request.gameID() == 0;
    }
}
