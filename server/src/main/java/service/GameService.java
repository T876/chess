package service;

import dataaccess.interfaces.IAuthDAO;
import dataaccess.interfaces.IGameDAO;
import service.models.*;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    public GameService(IAuthDAO authDAO, IGameDAO gameDAO) { }

    public GameListResponse listGames(String auth) {
        return new GameListResponse(new ArrayList<GameInfo>());
    }

    public CreateGameResponse createGame(String auth, CreateGameRequest request) {
        return new CreateGameResponse(1234);
    }

    public void joinGame(String auth, JoinGameRequest request) { }
}
