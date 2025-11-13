package dataaccess;

import dataaccess.interfaces.IUserDAO;
import model.UserData;

public class MemoryUserDAO implements IUserDAO {
    public MemoryUserDAO() {};

    public void createUser(UserData data) throws DataAccessException{

    };

    public boolean verifyUser(String username, String password) throws DataAccessException {
        return true;
    };

    public void clear() {

    };
}
