package com.chatsystem.server.Controller;

import com.chatsystem.server.Model.User;
import com.chatsystem.server.network.Action;
import com.chatsystem.server.network.Request;
import com.chatsystem.server.network.Response;
import com.chatsystem.server.services.AuthService;

public class AuthController {
    private final AuthService authService;

    public AuthController() {
        this.authService = new AuthService();
    }

    public Response executeRequest(Request request) {
        switch (request.getAction()) {
            case LOGIN:
                return login(request);
            case REGISTER:
                return register(request);
            case LOGOUT:
                return logout(request);
            default:
                return new Response(false, "Invalid action", null);
        }
    }

    // تسجيل الدخول
    public Response login(Request request) {
        User user = authService.login(request.getUser().getEmail(), request.getUser().getPasswordHash());
        if (user != null) {
            return new Response(true, "Login successful", user, Action.LOGIN);
        } else {
            return new Response(false, "Invalid email or password", null, Action.LOGIN);
        }
    }

    // تسجيل مستخدم جديد
    public Response register(Request request) {
        boolean success = authService.register(request.getUser().getUsername(), request.getUser().getEmail(), request.getUser().getPasswordHash());
        if (success) {
            return new Response(true, "Registration successful", Action.REGISTER);
        } else {
            return new Response(false, "Email or username already taken", Action.REGISTER);
        }
    }

    // تسجيل الخروج
    public Response logout(Request request) {
        int userId = request.getUser().getUser_id();
        authService.logout(userId);
        return new Response(true, "Logout successful", Action.LOGOUT);
    }
}
