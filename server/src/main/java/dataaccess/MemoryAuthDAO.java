package dataaccess;


import dataaccess.interfaces.IAuthDAO;
import model.AuthData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthDAO implements IAuthDAO {
    List<AuthData> sessions = new ArrayList<>();

    public MemoryAuthDAO() {

    };

    public AuthData makeAuthData(String username) {
        AuthData newAuth = new AuthData(UUID.randomUUID().toString(), username);
        sessions.add(newAuth);
        return newAuth;
    };

    public boolean verifyAuthToken(String authToken) throws DataAccessException {
        for (AuthData session: sessions) {
            if (Objects.equals(session.authToken(), authToken)) {
                return true;
            }
        }
        throw new DataAccessException("Error: unauthorized");
    };

    public void logout(String authToken) throws DataAccessException {
        AuthData match = null;

        for (AuthData session: sessions) {
            if (Objects.equals(session.authToken(), authToken)) {
                match = session;
                break;
            }
        }

        if (match == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        sessions.remove(match);
    }

    public void clear() {};
}
