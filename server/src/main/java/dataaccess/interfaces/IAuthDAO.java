package dataaccess.interfaces;

import dataaccess.DataAccessException;
import model.AuthData;

public interface IAuthDAO {
    // Create
    public AuthData makeAuthData(String username);

    // Read
    public boolean verifyAuthToken(String authToken) throws DataAccessException;

    // Delete
    public void logout(String authToken) throws DataAccessException;
    public void clear();
}
