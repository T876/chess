package service;

import dataaccess.DataAccessException;
import dataaccess.interfaces.IAuthDAO;
import dataaccess.interfaces.IUserDAO;
import model.*;

public class UserService {
    IUserDAO userDAO;
    IAuthDAO authDAO;

    public UserService(IAuthDAO authDAO, IUserDAO userDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResponse register (RegisterRequest request) throws DataAccessException {
        this.userDAO.createUser(new UserData(request.username(), request.password(), request.email()));
        AuthData authData = this.authDAO.makeAuthData(request.username());
        return new RegisterResponse(authData.username(), authData.authToken());
    }

    public LoginResponse login (LoginRequest request) throws DataAccessException {
        this.userDAO.verifyUser(request.username(), request.password());
        AuthData authData = this.authDAO.makeAuthData(request.username());
        return new LoginResponse(authData.username(), authData.authToken());
    }

    public void logout(String authToken) throws DataAccessException {
        this.authDAO.verifyAuthToken(authToken);
        this.authDAO.logout(authToken);
    }
}
