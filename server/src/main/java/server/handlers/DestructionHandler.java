package server.handlers;
import com.google.gson.Gson;
import io.javalin.http.Context;
import service.DestructionService;

public class DestructionHandler {
    public final Gson serializer;
    public final DestructionService destructionService;

    public DestructionHandler(DestructionService destructionService) {
        this.serializer = new Gson();
        this.destructionService = destructionService;
    }

    void returnErrorResponse(Context context, int status, String message) {
        context.status(status);
        context.json(serializer.toJson(new ErrorBody(message)));
    }

    public void clearApplication(Context context) {
        this.destructionService.clearApplication();
        String response = serializer.toJson(new Object());
        context.status(200);
        context.json(response);
    }
}
