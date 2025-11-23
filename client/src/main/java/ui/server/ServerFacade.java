package ui.server;

import com.google.gson.Gson;
import model.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ServerFacade {
    private final Gson serializer;
    private final HttpClient httpClient;

    public ServerFacade(Gson gson) {
        this.serializer = gson;
        this.httpClient = HttpClient.newHttpClient();
    }

    public AuthData register(String username, String password, String email) {
        String urlString = this.getURLString("/user");
        String registerRequestJson = serializer.toJson(new RegisterRequest(username, password, email));

        HttpRequest request;
        HttpResponse<String> httpResponse;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .timeout(java.time.Duration.ofMillis(6000))
                    .POST(HttpRequest.BodyPublishers.ofString(registerRequestJson))
                    .build();
            httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.ensureHTTPResponse(httpResponse);

        RegisterResponse goodResponse = serializer.fromJson(httpResponse.body(), RegisterResponse.class);

        return new AuthData(goodResponse.authToken(), goodResponse.username());
    }

    public AuthData login(String username, String password) {
        String urlString = this.getURLString("/session");
        String loginRequestJson = serializer.toJson(new LoginRequest(username, password));

        HttpRequest request;
        HttpResponse<String> httpResponse;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .timeout(java.time.Duration.ofMillis(6000))
                    .POST(HttpRequest.BodyPublishers.ofString(loginRequestJson))
                    .build();
            httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.ensureHTTPResponse(httpResponse);

        LoginResponse goodResponse = serializer.fromJson(httpResponse.body(), LoginResponse.class);

        return new AuthData(goodResponse.authToken(), goodResponse.username());
    }

    public void logout(String authToken) {
        String urlString = this.getURLString("/session");

        HttpRequest request;
        HttpResponse<String> httpResponse;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .timeout(java.time.Duration.ofMillis(6000))
                    .DELETE()
                    .header("authorization", authToken)
                    .build();
            httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.ensureHTTPResponse(httpResponse);
    }

    public List<GameInfo> listGames(String authToken) {
        String urlString = this.getURLString("/game");

        HttpRequest request;
        HttpResponse<String> httpResponse;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .timeout(java.time.Duration.ofMillis(6000))
                    .GET()
                    .header("authorization", authToken)
                    .build();
            httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.ensureHTTPResponse(httpResponse);

        GameListResponse goodResponse = serializer.fromJson(httpResponse.body(), GameListResponse.class);

        return goodResponse.games();
    }

    public int createGame(String authToken, String name) {
        String urlString = this.getURLString("/game");
        String createRequestJson = serializer.toJson(new CreateGameRequest(name));

        HttpRequest request;
        HttpResponse<String> httpResponse;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .timeout(java.time.Duration.ofMillis(6000))
                    .POST(HttpRequest.BodyPublishers.ofString(createRequestJson))
                    .header("authorization", authToken)
                    .build();
            httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.ensureHTTPResponse(httpResponse);

        CreateGameResponse goodResponse = serializer.fromJson(httpResponse.body(), CreateGameResponse.class);

        return goodResponse.gameID();
    }

    public void joinGame(String authToken, int gameID, String color) {
        String urlString = this.getURLString("/game");
        String joinRequestJson = serializer.toJson(new JoinGameRequest(color, gameID));

        HttpRequest request;
        HttpResponse<String> httpResponse;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .timeout(java.time.Duration.ofMillis(6000))
                    .PUT(HttpRequest.BodyPublishers.ofString(joinRequestJson))
                    .header("authorization", authToken)
                    .build();
            httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.ensureHTTPResponse(httpResponse);
    }

    public void clear () {
        String urlString = this.getURLString("/db");

        HttpRequest request;
        HttpResponse<String> httpResponse;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .timeout(java.time.Duration.ofMillis(6000))
                    .DELETE()
                    .build();

            httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.ensureHTTPResponse(httpResponse);
    }

    private String getURLString(String path) {
        return String.format(Locale.getDefault(), "http://localhost:8080%s",path);
    }

    private void ensureHTTPResponse(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            ServerErrorResponse errorResponse = serializer.fromJson(response.body(), ServerErrorResponse.class);
            throw new RuntimeException(errorResponse.message());
        }
    }
}
