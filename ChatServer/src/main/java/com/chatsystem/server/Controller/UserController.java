package com.chatsystem.server.Controller;

import com.chatsystem.server.Model.User;
import com.chatsystem.server.network.Action;
import com.chatsystem.server.network.Request;
import com.chatsystem.server.network.Response;
import com.chatsystem.server.services.UserService;
import java.util.List;

public class UserController {
    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    public Response executeRequest(Request request) {
        switch (request.getAction()) {
            case GET_USER_BY_ID:
                return getUserById(request);
            case GET_USER_BY_EMAIL:
                return getUserByEmail(request);
            case UPDATE_USER:
                return updateUser(request);
            case UPDATE_USER_PROFILE_PICTURE:
                return updateProfilePicture(request);
            case CHANGE_PASSWORD:
                return changePassword(request);
            case SEARCH_USERS:
                return searchUsers(request);
            case GET_USERS:
                return getAllUsers(request);
            case GET_ONLINE_USERS:
                return getOnlineUsers(request);
            default:
                return new Response(false, "Invalid Action", request.getAction());
        }
    }

    // Get user by ID
    private Response getUserById(Request request) {
        User user = userService.getUserById(((User)request.getData()[0]).getUser_id());
        if (user != null) {
            return new Response(true, "User found", user, request.getAction());
        } else {
            return new Response(false, "User not found", null, request.getAction());
        }
    }

    // Get user by email
    private Response getUserByEmail(Request request) {
        User user = userService.getUserByEmail(((User)request.getData()[0]).getEmail());
        if (user != null) {
            return new Response(true, "User found", user, request.getAction());
        } else {
            return new Response(false, "User not found", null, request.getAction());
        }
    }

    // Update user profile (username, email, avatar, etc.)
    private Response updateUser(Request request) {
        User targetUser = (User) request.getData()[0];
        if(userService.getUserById(targetUser.getUser_id()) == null){
            return new Response(false, "User not found", request.getAction());
        }
        boolean success = userService.updateUser(targetUser);
        if (success) {
            return new Response(true, "User updated", null, request.getAction());
        } else {
            return new Response(false, "Update failed", null, request.getAction());
        }
    }

    // Update user profile_picture
    private Response updateProfilePicture(Request request) {
        int userId = ((User) request.getData()[0]).getUser_id();
        String profilePicturePath = (String) request.getData()[1];

        if(userService.getUserById(userId) == null && (profilePicturePath == null || profilePicturePath.isEmpty())){
            return new Response(false, "User not found", request.getAction());
        }
        boolean success = userService.setProfilePicture(userId, profilePicturePath);
        if (success) {
            return new Response(true, "Profile picture updated", null, request.getAction());
        } else {
            return new Response(false, "Profile picture update failed", null, request.getAction());
        }
    }

    // Change password
    private Response changePassword(Request request) {
        int userId = ((User) request.getData()[0]).getUser_id();
        String newPassword = (String) request.getData()[1];
        if(userService.getUserById(userId) == null && (newPassword == null || newPassword.isEmpty())){
            return new Response(false, "User not found", request.getAction());
        }
        boolean success = userService.changePassword(userId, newPassword);
        if (success) {
            return new Response(true, "Password changed", null, request.getAction());
        } else {
            return new Response(false, "Password change failed", null, request.getAction());
        }
    }

    // Search users by username or email (partial match)
    private Response searchUsers(Request request) {
        String username = (String) request.getData()[0];
        List<User> users = userService.searchByUsername(username);
        if (users.isEmpty() || users == null) {
            return new Response(false, "No users found", null, request.getAction());
        }
        Response response = new Response(true, "Users found", null, request.getAction());
        response.setUsers(users);
        return response;
    }

    // Get all users
    private Response getAllUsers(Request request) {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty() || users == null) {
            return new Response(false, "No users found", null, request.getAction());
        }
        Response response = new Response(true, "Users found", null, request.getAction());
        response.setUsers(users);
        return response;
    }

    // Get online users
    private Response getOnlineUsers(Request request) {
        List<User> onlineUsers = userService.getOnlineUsers();
        if (onlineUsers.isEmpty() || onlineUsers == null) {
            return new Response(false, "No online users found", null, request.getAction());
        }
        Response response = new Response(true, "Online users found", null, request.getAction());
        response.setUsers(onlineUsers);
        return response;
    }
}
