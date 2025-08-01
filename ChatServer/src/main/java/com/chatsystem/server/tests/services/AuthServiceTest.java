package com.chatsystem.server.tests.services;

import com.chatsystem.server.services.AuthService;
import com.chatsystem.server.Model.User;

public class AuthServiceTest {
    public static void main(String[] args) {
        AuthService authService = new AuthService();

        // Test registration
        boolean registered = authService.register("testuserFackFack", "testuserFack@example.com", "hashedpasswordFack");
        System.out.println("Registration: " + registered);

        // Test duplicate registration
        boolean duplicate = authService.register("testuser", "testuser@example.com", "hashedpassword");
        System.out.println("Duplicate registration: " + duplicate);

        // Test login
        User user = authService.login("testuserFack@example.com", "hashedpasswordFack");
        System.out.println("Login: " + (user != null ? "Success" : "Failed"));

        // Test login with wrong password
        User wrongLogin = authService.login("testuserFack@example.com", "wrongpassword");
        System.out.println("Login with wrong password: " + (wrongLogin != null ? "Success" : "Failed"));

        // Test logout
        if (user != null) {
            authService.logout(user.getUser_id());
            System.out.println("Logout: Success");
        }
    }
}
