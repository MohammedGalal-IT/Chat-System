package com.chatsystem.server.tests.data;

import java.util.List;

import com.chatsystem.server.DataAccess.interfaces.UserDao;
import com.chatsystem.server.DataAccess.repository.UserDaoImpl;
import com.chatsystem.server.Model.User;

public class UserDaoTest {

    public void runTest(){
        UserDao userDao = new UserDaoImpl();
        System.out.println("--- Testing UserDaoImpl ---");

        // Test save
        User user = new User("testuser", "testuser@example.com", "testpass", true);
        boolean saved = userDao.save(user);
        System.out.println("User saved: " + saved);

        // Test getByUsername
        User fetchedByUsername = userDao.getByUsername("testuser");
        System.out.println("Fetched by username: " + fetchedByUsername);

        // Test getByEmail
        User fetchedByEmail = userDao.getByEmail("testuser@example.com");
        System.out.println("Fetched by email: " + fetchedByEmail);

        // Test getAllUsers
        List<User> allUsers = userDao.getAllUsers();
        System.out.println("All users: " + allUsers);

        // Test getById
        int userId = fetchedByUsername != null ? fetchedByUsername.getUser_id() : -1;
        User fetchedById = userDao.getById(userId);
        System.out.println("Fetched by ID: " + fetchedById);

        // Test update
        if (fetchedById != null) {
            fetchedById.setUsername("updateduser");
            boolean updated = userDao.update(fetchedById);
            System.out.println("User updated: " + updated);
            System.out.println("After update: " + userDao.getById(userId));
        }

        // Test setProfilePicture
        boolean profilePicSet = userDao.setProfilePicture(userId, "/path/to/profile.jpg");
        System.out.println("Profile picture set: " + profilePicSet);

        // Test changePassword
        boolean passwordChanged = userDao.changePassword(userId, "newhashedpassword");
        System.out.println("Password changed: " + passwordChanged);

        // Test setOnline and setOffline
        boolean setOnline = userDao.setOnline(userId);
        boolean setOffline = userDao.setOffline(userId);
        System.out.println("Set online: " + setOnline + ", Set offline: " + setOffline);

        // Test searchByUsername
        List<User> searchResults = userDao.searchByUsername("updated");
        System.out.println("Search by username 'updated': " + searchResults);

        // Test getOnlineUsers
        List<User> onlineUsers = userDao.getOnlineUsers();
        System.out.println("Online users: " + onlineUsers);

        // Test updateLastSeen (if supported by interface)
        // boolean lastSeenUpdated = ((UserDaoImpl)userDao).updateLastSeen(userId, new Timestamp(System.currentTimeMillis()));
        // System.out.println("Last seen updated: " + lastSeenUpdated);

        // Test delete
        boolean deleted = userDao.delete(userId);
        System.out.println("User deleted: " + deleted);
    }
    
}
