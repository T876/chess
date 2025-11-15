package dataaccess;


import dataaccess.interfaces.IAuthDAO;
import model.AuthData;

import java.util.ArrayList;
import java.util.List;
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
        return true;
    };

    public void logout(String authToken) throws DataAccessException {

    }

    public void clear() {};
}
