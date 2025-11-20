package server;

import com.google.gson.Gson;

public class ServerFacade {
    private Gson serializer;

    public ServerFacade(Gson gson) {
        this.serializer = gson;
    }
}
