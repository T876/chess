package server.handlers;
import com.google.gson.Gson;
import io.javalin.http.Context;
import service.UserService;
import service.models.LoginRequest;
import service.models.LoginResponse;
import service.models.RegisterRequest;
import service.models.RegisterResponse;

public class UserHandler {
    private final Gson serializer;
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.serializer = new Gson();
        this.userService = userService;
    }

    void returnErrorResponse(Context context, int status, String message) {
        context.status(status);
        context.json(serializer.toJson(new ErrorBody(message)));
    }

    public void register(Context context) {
        RegisterRequest request;

        try {
            request = serializer.fromJson(context.body(), RegisterRequest.class);
        } catch (Exception e) {
            returnErrorResponse(context, 400, "Error: bad request");
            return;
        }

        if (request.email() == null || request.password() == null || request.username() == null) {
            returnErrorResponse(context, 400, "Error: bad request");
            return;
        }

        RegisterResponse response = userService.register(request);
        String responseJson = serializer.toJson(response);

        context.status(200);
        context.json(responseJson);
    }

    public void login(Context context) {
        LoginRequest request;

        try {
            request = serializer.fromJson(context.body(), LoginRequest.class);
        } catch(Exception e) {
            returnErrorResponse(context, 400, "Error: bad request");
            return;
        }

        if (request.username() == null || request.password() == null) {
            returnErrorResponse(context, 400, "Error: bad request");
            return;
        }

        LoginResponse response = userService.login(request);
        String responseJson = serializer.toJson(response);

        context.status(200);
        context.json(responseJson);
    }

    public void logout(Context context) {
        String auth;

        try {
            auth = context.header("authToken");
        } catch(Exception e) {
            returnErrorResponse(context, 400, "Error: bad request");
            return;
        }

        userService.logout(auth);
        String response = serializer.toJson(new Object());

        context.status(200);
        context.json(response);
    }
}
