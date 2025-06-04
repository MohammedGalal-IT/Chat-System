package com.chatsystem.server.DataAccess.repository;

import com.chatsystem.server.DataAccess.interfaces.UserDao;
import com.chatsystem.server.Model.User;
import com.chatsystem.server.util.*;

import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public User getById(int id) {
        return DatabaseManager.executeQuerySingle("SELECT * FROM users WHERE id = ?",
                rs -> new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getBoolean("is_online"),
                        rs.getString("profile_picture"),
                        rs.getTimestamp("created_at")

                ), id
                );
    }

    @Override
    public User getByUsername(String username) {
        return DatabaseManager.executeQuerySingle("SELECT * FROM users WHERE username = ?",
                rs -> new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getBoolean("is_online"),
                        rs.getString("profile_picture"),
                        rs.getTimestamp("created_at")
                ), username
        );
    }

    @Override
    public User getByEmail(String email) {
        return DatabaseManager.executeQuerySingle("SELECT * FROM `users` WHERE email = ?",
                rs -> new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getBoolean("is_online"),
                        rs.getString("profile_picture"),
                        rs.getTimestamp("created_at")
                ), email
        );
    }

    @Override
    public List<User> getAllUsers() {
        return DatabaseManager.executeQueryList("SELECT * FROM `users`",
                rs -> new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getBoolean("is_online"),
                        rs.getString("profile_picture"),
                        rs.getTimestamp("created_at")
                ));
    }

    @Override
    public boolean save(User user) {
        String hashedPassword = SecurityUtil.hashPassword(user.getPasswordHash());
        return DatabaseManager.executeUpdate(
                "INSERT INTO `users` (username, email, password_hash, is_online, profile_picture) VALUES (?, ?, ?, ?, ?)",
                user.getUsername(),
                user.getEmail(),
                hashedPassword, 
                user.isOnline(), 
                user.getProfilePicture()
        ) > 0;
    }

    @Override
    public boolean update(User user) {
        return DatabaseManager.executeUpdate(
                "UPDATE `users` SET username = ?, email = ?, password_hash = ?, is_online = ?, profile_picture = ? WHERE id = ?",
                user.getUsername(), 
                user.getEmail(), 
                user.getPasswordHash(), 
                user.isOnline(), 
                user.getProfilePicture(), 
                user.getUserId()
        ) > 0;
    }

    @Override
    public boolean delete(int id) {
        return DatabaseManager.executeUpdate(
            "DELETE FROM `users` WHERE id = ?",
             id) > 0;
    }

    @Override
    public boolean setOnline(int id) {
        return DatabaseManager.executeUpdate(
                "UPDATE `users` SET is_online = TRUE WHERE id = ?",
                id) > 0;
    }

    @Override
    public boolean setOffline(int id) {
        return DatabaseManager.executeUpdate(
                "UPDATE `users` SET is_online = FALSE WHERE id = ?",
                id) > 0;
    }
}
