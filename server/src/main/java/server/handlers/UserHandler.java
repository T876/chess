package server.handlers;

import com.google.gson.Gson;


import io.javalin.http.Context;
import model.UserData;

public class UserHandler {
    private final Gson serializer;

    public UserHandler() {
        this.serializer = new Gson();
    }

    public void register(Context context) {
        UserData request = serializer.fromJson(context.body(), UserData.class);
        String response = serializer.toJson(new Object());
        context.status(200);
        context.json(response);
    }
}
