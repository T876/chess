package dataaccess.interfaces;

import dataaccess.DataAccessException;
import model.AuthData;

public interface IAuthDAO {
    // Create
    public AuthData makeAuthData(String username);

    // Read
    public AuthData verifyAuthToken(String authToken) throws DataAccessException;

    // Delete
    public int logout(String authToken) throws DataAccessException;
    public void clear();
}
