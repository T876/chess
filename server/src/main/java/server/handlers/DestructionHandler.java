package server.handlers;
import com.google.gson.Gson;
import io.javalin.http.Context;

public class DestructionHandler {
    public final Gson serializer;

    public DestructionHandler() {
        this.serializer = new Gson();
    }

    public void clearApplication(Context context) {
        String response = serializer.toJson(new Object());
        context.status(200);
        context.json(response);
    }
}
