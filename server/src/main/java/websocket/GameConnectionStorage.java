package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GameConnectionStorage {
    public final ConcurrentHashMap<String, List<Session>> connections = new ConcurrentHashMap<>();

    public void add(int gameId, Session session) {
        String gameIDstring = Integer.toString(gameId);
        connections.computeIfAbsent(gameIDstring, list -> new ArrayList<>()).add(session);
        return;
    }

    public void remove(int gameId, Session session) {
        connections.get(Integer.toString(gameId)).remove(session);
    }

    public void endGame(int gameId) {
        connections.remove(Integer.toString(gameId));
    }

    public void broadcastToGame(int gameID, String message, Session sender) throws IOException {
        String gameIDString = Integer.toString(gameID);
        for (Session s : connections.get(gameIDString)) {
            if (s.isOpen() && !s.equals(sender)) {
                s.getRemote().sendString(message);
            }
        }
    }
}
