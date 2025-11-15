package service;

import dataaccess.DataAccessException;
import dataaccess.interfaces.IAuthDAO;
import dataaccess.interfaces.IGameDAO;
import model.GameData;
import service.models.*;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    IAuthDAO authDAO;
    IGameDAO gameDAO;

    public GameService(IAuthDAO authDAO, IGameDAO gameDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public GameListResponse listGames(String auth)throws DataAccessException {
        this.authDAO.verifyAuthToken(auth);
        List<GameData> games = this.gameDAO.getAllGames();
        GameListResponse response = new GameListResponse(new ArrayList<>());
        for (GameData game : games) {
            response.games().add(new GameInfo(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }
        return response;
    }

    public CreateGameResponse createGame(String auth, CreateGameRequest request) {
        return new CreateGameResponse(1234);
    }

    public void joinGame(String auth, JoinGameRequest request) { }
}
