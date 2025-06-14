package com.chatsystem.server.tests.controllers;

import com.chatsystem.server.Controller.UserController;
import com.chatsystem.server.Model.User;
import com.chatsystem.server.network.Action;
import com.chatsystem.server.network.Request;
import com.chatsystem.server.network.Response;

public class UserControllerTest {
    public static void main(String[] args) {
        UserController userController = new UserController();

        // Create a user for testing
         User testUser = new User("authuser", "authuser@example.com", "hashedpassword");
        // Simulate registration (should be done via AuthController in real app)
        // For test, assume user is already in DB after AuthControllerTest

        // Test get user by email
        Request getByEmailReq = new Request();
        getByEmailReq.setAction(Action.GET_USER_BY_EMAIL);
        getByEmailReq.setData(new Object[]{testUser});
        Response getByEmailResp = userController.executeRequest(getByEmailReq);
        System.out.println("Get by email: " + getByEmailResp.getMessage() + ", User: " + getByEmailResp.getUser());
        System.out.println();

        // Test get user by id
        User foundUser = getByEmailResp.getUser();
        if (foundUser != null) {
            Request getByIdReq = new Request();
            getByIdReq.setAction(Action.GET_USER_BY_ID);
            getByIdReq.setData(new Object[]{foundUser});
            Response getByIdResp = userController.executeRequest(getByIdReq);
            System.out.println("Get by id: " + getByIdResp.getMessage() + ", User: " + getByIdResp.getUser());
        }
        System.out.println();

        // Test update user
        if (foundUser != null) {
            foundUser.setUsername("usercontroller_updated");
            Request updateReq = new Request();
            updateReq.setAction(Action.UPDATE_USER);
            updateReq.setData(new Object[]{foundUser});
            Response updateResp = userController.executeRequest(updateReq);
            System.out.println("Update user: " + updateResp.getMessage());
        }
        System.out.println();

        // Test set profile picture
        if (foundUser != null) {
            Request picReq = new Request();
            picReq.setAction(Action.UPDATE_USER_PROFILE_PICTURE);
            picReq.setData(new Object[]{foundUser, "/uploads/images/profile.jpg"});
            Response picResp = userController.executeRequest(picReq);
            System.out.println("Set profile picture: " + picResp.getMessage());
        }
        System.out.println();

        // Test change password
        if (foundUser != null) {
            Request pwReq = new Request();
            pwReq.setAction(Action.CHANGE_PASSWORD);
            pwReq.setData(new Object[]{foundUser, "newhashedpassword"});
            Response pwResp = userController.executeRequest(pwReq);
            System.out.println("Change password: " + pwResp.getMessage());
        }
        System.out.println();

        // Test search users
        Request searchReq = new Request();
        searchReq.setAction(Action.SEARCH_USERS);
        searchReq.setData(new Object[]{"usercontroller"});
        Response searchResp = userController.executeRequest(searchReq);
        System.out.println("Search users: " + searchResp.getMessage() + ", Users: " + searchResp.getUsers());
        System.out.println();

        // Test get all users
        Request allReq = new Request();
        allReq.setAction(Action.GET_USERS);
        Response allResp = userController.executeRequest(allReq);
        System.out.println("Get all users: " + allResp.getMessage() + ", Users: " + allResp.getUsers());
        System.out.println();

        // Test get online users
        Request onlineReq = new Request();
        onlineReq.setAction(Action.GET_ONLINE_USERS);
        Response onlineResp = userController.executeRequest(onlineReq);
        System.out.println("Get online users: " + onlineResp.getMessage() + ", Users: " + onlineResp.getUsers());
        System.out.println();
    }
}
