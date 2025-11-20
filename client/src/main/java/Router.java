import service.GameService;
import service.UserService;

public class Router {
    private UserService userService;
    private GameService gameService;

    public Router(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

}
