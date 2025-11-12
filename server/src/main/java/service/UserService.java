package service;

import service.models.LoginRequest;
import service.models.LoginResponse;
import service.models.RegisterRequest;
import service.models.RegisterResponse;

public class UserService {

    public UserService() {}

    public RegisterResponse register (RegisterRequest request) {
        return new RegisterResponse("username", "token");
    }

    public LoginResponse login (LoginRequest request) {
        return new LoginResponse("username", "token");
    }

    public void logout(String authToken) { }
}
