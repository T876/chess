package dataaccess.interfaces;

import model.AuthData;
import model.UserData;

public interface IUserDAO {
    // Create
    public void createUser(UserData data);

    // Read
    public boolean verifyUser(String username, String password);

    // Delete
    public void clear();
}
