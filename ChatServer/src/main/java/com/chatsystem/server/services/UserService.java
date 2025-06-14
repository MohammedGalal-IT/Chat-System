package com.chatsystem.server.services;

import com.chatsystem.server.DataAccess.interfaces.UserDao;
import com.chatsystem.server.DataAccess.repository.UserDaoImpl;
import com.chatsystem.server.Model.User;
import com.chatsystem.server.util.SecurityUtil;

import java.util.List;

public class UserService {
    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDaoImpl();
    }

    public User getUserById(int id) {
        return userDao.getById(id);
    }

    public User getUserByUsername(String username) {
        return userDao.getByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userDao.getByEmail(email);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public boolean updateUser(User user) {
        return userDao.update(user);
    }

    public boolean deleteUser(int id) {
        return userDao.delete(id);
    }

    public boolean setProfilePicture(int userId, String profilePicturePath) {
        return userDao.setProfilePicture(userId, profilePicturePath);
    }

    public boolean changePassword(int userId, String newPassword) {
        return userDao.changePassword(userId, SecurityUtil.hashPassword(newPassword));
    }

    public List<User> searchByUsername(String partialUsername) {
        return userDao.searchByUsername(partialUsername);
    }

    public List<User> getOnlineUsers() {
        return userDao.getOnlineUsers();
    }

    public boolean createUser(String username, String email, String password) {
        // Hash the password before saving
        String hashedPassword = SecurityUtil.hashPassword(password);
        User user = new User(username, email, hashedPassword);
        return userDao.save(user);
    }
}
