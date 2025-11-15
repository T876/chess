package dataaccess;

import dataaccess.interfaces.IUserDAO;
import model.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryUserDAO implements IUserDAO {
    List<UserData> users = new ArrayList<>();
    public MemoryUserDAO() {};

    public void createUser(UserData data) throws DataAccessException{
        if (userAlreadyExists(data.username())) {
            throw new DataAccessException("Error: already taken");
        }
        users.add(data);
    };

    boolean userAlreadyExists(String username) {
        for(UserData user :users) {
            if (Objects.equals(user.username(), username)) {
                return true;
            }
        }
        return false;
    };

    public boolean verifyUser(String username, String password) throws DataAccessException {
        for(UserData user :users) {
            if (Objects.equals(user.username(), username)
                    && Objects.equals(user.password(), password)) {
                return true;
            }
        }
       throw new DataAccessException("Error: Unauthorized");
    };

    public void clear() {
        this.users = new ArrayList<>();
    };
}
