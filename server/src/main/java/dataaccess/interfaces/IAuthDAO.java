package dataaccess.interfaces;

import model.AuthData;

public interface IAuthDAO {
    // Create
    public AuthData makeAuthData(String username);

    // Read
    public boolean verifyAuthToken(String authToken);

    // Delete
    public void logout(String authToken);
    public void clear();
}
