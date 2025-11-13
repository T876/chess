package dataaccess;


import dataaccess.interfaces.IAuthDAO;
import model.AuthData;

import java.util.UUID;

public class MemoryAuthDAO implements IAuthDAO {
    public MemoryAuthDAO() {

    };

    public AuthData makeAuthData(String username) {
        AuthData newAuth = new AuthData(username, UUID.randomUUID().toString());
        return newAuth;
    };

    public boolean verifyAuthToken(String authToken) throws DataAccessException {
        return true;
    };

    public void logout(String authToken) throws DataAccessException {

    }

    public void clear() {};
}
