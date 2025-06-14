package com.chatsystem.server.tests.controllers;

import com.chatsystem.server.Controller.AuthController;
import com.chatsystem.server.Model.User;
import com.chatsystem.server.network.Action;
import com.chatsystem.server.network.Request;
import com.chatsystem.server.network.Response;

public class AuthControllerTest {
    public static void main(String[] args) {
        AuthController authController = new AuthController();

        // Test registration
        User regUser = new User("authuser", "authuser@example.com", "hashedpassword");
        Request regRequest = new Request();
        regRequest.setAction(Action.REGISTER);
        regRequest.setUser(regUser);
        Response regResponse = authController.executeRequest(regRequest);
        System.out.println("Registration: " + regResponse.getMessage());
        System.out.println();

        // Test duplicate registration
        Response dupRegResponse = authController.executeRequest(regRequest);
        System.out.println("Duplicate registration: " + dupRegResponse.getMessage());
        System.out.println();

        // Test login
        User loginUser = new User("authuser", "authuser@example.com", "hashedpassword");
        Request loginRequest = new Request();
        loginRequest.setAction(Action.LOGIN);
        loginRequest.setUser(loginUser);
        Response loginResponse = authController.executeRequest(loginRequest);
        System.out.println("Login: " + loginResponse.getMessage());
        System.out.println();

        // Test login with wrong password
        User wrongLoginUser = new User("authuser", "authuser@example.com", "wrongpassword");
        Request wrongLoginRequest = new Request();
        wrongLoginRequest.setAction(Action.LOGIN);
        wrongLoginRequest.setUser(wrongLoginUser);
        Response wrongLoginResponse = authController.executeRequest(wrongLoginRequest);
        System.out.println("Login with wrong password: " + wrongLoginResponse.getMessage());
        System.out.println();

        // Test logout
        if (loginResponse.isSuccess() && loginResponse.getUser() != null) {
            Request logoutRequest = new Request();
            logoutRequest.setAction(Action.LOGOUT);
            logoutRequest.setUser(loginResponse.getUser());
            Response logoutResponse = authController.executeRequest(logoutRequest);
            System.out.println("Logout: " + logoutResponse.getMessage());
            System.out.println();
        }
    }
}
