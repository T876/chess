package service;

import dataaccess.DataAccessException;
import dataaccess.interfaces.IAuthDAO;
import dataaccess.interfaces.IGameDAO;
import dataaccess.interfaces.IUserDAO;

public class DestructionService {
    IAuthDAO authDAO;
    IUserDAO userDAO;
    IGameDAO gameDAO;

    public DestructionService(IAuthDAO authDAO, IUserDAO userDAO, IGameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public void clearApplication() {
        this.authDAO.clear();
        this.userDAO.clear();
        this.gameDAO.clear();
    }
}
