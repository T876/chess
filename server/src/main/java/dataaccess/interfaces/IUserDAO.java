package dataaccess.interfaces;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

public interface IUserDAO {
    // Create
    public void createUser(UserData data) throws DataAccessException;

    // Read
    public boolean verifyUser(String username, String password) throws DataAccessException;

    // Delete
    public void clear();
}
