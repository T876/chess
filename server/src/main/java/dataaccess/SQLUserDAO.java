package dataaccess;

import dataaccess.interfaces.IUserDAO;
import model.UserData;

public class SQLUserDAO implements IUserDAO {

    public SQLUserDAO() { }

    public void createUser(UserData data) throws DataAccessException{ };

    public boolean verifyUser(String username, String password) throws DataAccessException {
        return true;
    };

    public void clear() { };
}
