package dataaccess;

import dataaccess.interfaces.IAuthDAO;
import model.AuthData;

public class SQLAuthDAO implements IAuthDAO {
    public SQLAuthDAO () { }

    public AuthData makeAuthData(String username) {
        return new AuthData("token", "username");
    }

    public AuthData verifyAuthToken(String authToken) throws DataAccessException {
        return new AuthData("token", "username");
    };

    public void logout(String authToken) throws DataAccessException {};

    public void clear() {};
}
