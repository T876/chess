package server.handlers;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.UserService;
import service.models.LoginRequest;
import service.models.LoginResponse;
import service.models.RegisterRequest;
import service.models.RegisterResponse;

import java.util.Objects;

public class UserHandler {
    private final Gson serializer;
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.serializer = new Gson();
        this.userService = userService;
    }

    void returnErrorResponse(Context context, int status, String message) {
        context.status(status);
        context.json(serializer.toJson(new ErrorBody(message)));
    }

    public void register(Context context) {
        RegisterRequest request;

        try {
            request = serializer.fromJson(context.body(), RegisterRequest.class);
        } catch (Exception e) {
            returnErrorResponse(context, 400, "Error: bad request");
            return;
        }

        if (request.email() == null || request.password() == null || request.username() == null) {
            returnErrorResponse(context, 400, "Error: bad request");
            return;
        }

        RegisterResponse response;

        try{
            response = userService.register(request);
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: unauthorized")) {
                this.returnErrorResponse(context, 403, e.getMessage());
            } else if (Objects.equals(e.getMessage(), "Error: already taken")) {
                this.returnErrorResponse(context, 403, e.getMessage());
            }else {
                this.returnErrorResponse(context,500, "Error:" + e.getMessage());
            }

            return;
        } catch (Exception e) {
            this.returnErrorResponse(context, 500, "Error:" + e.getMessage());
            return;
        }

        String responseJson = serializer.toJson(response);

        context.status(200);
        context.json(responseJson);
    }

    public void login(Context context) {
        LoginRequest request;

        try {
            request = serializer.fromJson(context.body(), LoginRequest.class);
        } catch(Exception e) {
            returnErrorResponse(context, 400, "Error: bad request");
            return;
        }

        if (request.username() == null || request.password() == null) {
            returnErrorResponse(context, 400, "Error: bad request");
            return;
        }


        LoginResponse response;
        try{
            response = userService.login(request);
        } catch (DataAccessException e) {
            String message = e.getMessage();
            if (Objects.equals(message, "Error: unauthorized")) {
                this.returnErrorResponse(context, 401, e.getMessage());
            } else {
                this.returnErrorResponse(context, 500, "Error:" + e.getMessage());
            }

            return;
        } catch (Exception e) {
            this.returnErrorResponse(context, 500, "Error:" + e.getMessage());
            return;
        }
        String responseJson = serializer.toJson(response);

        context.status(200);
        context.json(responseJson);
    }

    public void logout(Context context) {
        String auth;

        try {
            auth = context.header("authorization");
        } catch(Exception e) {
            returnErrorResponse(context, 400, "Error: bad request");
            return;
        }

        try{
            userService.logout(auth);
        } catch (DataAccessException e) {
            this.returnErrorResponse(context, 401, e.getMessage());
            return;
        } catch (Exception e) {
            this.returnErrorResponse(context, 500, "Error:" + e.getMessage());
            return;
        }

        String response = serializer.toJson(new Object());

        context.status(200);
        context.json(response);
    }
}
