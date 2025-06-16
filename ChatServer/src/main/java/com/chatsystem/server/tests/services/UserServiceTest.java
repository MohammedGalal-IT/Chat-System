package com.chatsystem.server.tests.services;

import com.chatsystem.server.services.UserService;
import com.chatsystem.server.Model.User;
import java.util.List;

public class UserServiceTest {
    public static void main(String[] args) {
        UserService userService = new UserService();

        // Test create user (register)
        boolean created = userService.createUser("usertest", "usertest@example.com", "hashedpassword");
        System.out.println("User created: " + created);

        // Test get user by email
        User user = userService.getUserByEmail("usertest@example.com");
        System.out.println("Get by email: " + (user != null ? user : "Not found"));

        // Test get user by id
        if (user != null) {
            User byId = userService.getUserById(user.getUser_id());
            System.out.println("Get by id: " + (byId != null ? byId : "Not found"));
        }

        // Test update user
        if (user != null) {
            user.setUsername("usertest_updated");
            boolean updated = userService.updateUser(user);
            System.out.println("User updated: " + updated);
            User updatedUser = userService.getUserById(user.getUser_id());
            System.out.println("After update: " + updatedUser);
        }

        // Test set profile picture
        if (user != null) {
            boolean picSet = userService.setProfilePicture(user.getUser_id(), "/uploads/images/profile.jpg");
            System.out.println("Profile picture set: " + picSet);
        }

        // Test change password
        if (user != null) {
            boolean pwChanged = userService.changePassword(user.getUser_id(), "newhashedpassword");
            System.out.println("Password changed: " + pwChanged);
        }

        // Test search by username
        List<User> found = userService.searchByUsername("usertest");
        System.out.println("Search by username: " + found);

        // Test get all users
        List<User> all = userService.getAllUsers();
        System.out.println("All users: " + all);

        // Test get online users
        List<User> online = userService.getOnlineUsers();
        System.out.println("Online users: " + online);

        // Test delete user
        if (user != null) {
            boolean deleted = userService.deleteUser(user.getUser_id());
            System.out.println("User deleted: " + deleted);
        }
    }
}
