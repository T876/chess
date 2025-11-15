package dataaccess;

import dataaccess.interfaces.IUserDAO;
import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements IUserDAO {
    List<UserData> users = new ArrayList<>();
    public MemoryUserDAO() {};

    public void createUser(UserData data) throws DataAccessException{
        users.add(data);
    };

    public boolean verifyUser(String username, String password) throws DataAccessException {
        return true;
    };

    public void clear() {

    };
}
