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

    public void register(Context context) {
        RegisterRequest request = serializer.fromJson(context.body(), RegisterRequest.class);
        RegisterResponse response = userService.register(request);
        String responseJson = serializer.toJson(response);

        context.status(200);
        context.json(responseJson);
    }

    public void login(Context context) {
        LoginRequest request = serializer.fromJson(context.body(), LoginRequest.class);
        LoginResponse response = userService.login(request);
        String responseJson = serializer.toJson(response);

        context.status(200);
        context.json(responseJson);
    }

    public void logout(Context context) {
        String auth = context.header("authToken");
        userService.logout(auth);
        String response = serializer.toJson(new Object());

        context.status(200);
        context.json(response);
    }
}
