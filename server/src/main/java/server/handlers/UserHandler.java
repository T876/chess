package server.handlers;

import com.google.gson.Gson;


import io.javalin.http.Context;
import model.UserData;
import service.models.LoginRequest;
import service.models.RegisterRequest;

public class UserHandler {
    private final Gson serializer;

    public UserHandler() {
        this.serializer = new Gson();
    }

    public void register(Context context) {
        RegisterRequest request = serializer.fromJson(context.body(), RegisterRequest.class);
        String response = serializer.toJson(new Object());
        context.status(200);
        context.json(response);
    }

    public void login(Context context) {
        LoginRequest request = serializer.fromJson(context.body(), LoginRequest.class);
        String response = serializer.toJson(new Object());
        context.status(200);
        context.json(response);
    }

    public void logout(Context context) {
        String response = serializer.toJson(new Object());
        context.status(200);
        context.json(response);
    }
}
