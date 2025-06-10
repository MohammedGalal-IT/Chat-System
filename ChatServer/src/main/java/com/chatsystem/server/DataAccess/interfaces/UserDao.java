package com.chatsystem.server.DataAccess.interfaces;

import com.chatsystem.server.Model.User;
import java.sql.Timestamp;
import java.util.List;

public interface UserDao {
    User getById(int id);
    User getByUsername(String username);
    User getByEmail(String email);
    List<User> getAllUsers();
    boolean save(User user);
    boolean update(User user);
    boolean delete(int id);
    boolean setOnline(int id);
    boolean setOffline(int id);
    // New methods matching User model and for richer user management:
    boolean setProfilePicture(int userId, String profilePicturePath);
    boolean changePassword(int userId, String newPasswordHash);
    // boolean deactivateUser(int userId); // need Modification in User model to support deactivation
    // boolean activateUser(int userId);
    List<User> searchByUsername(String partialUsername);
    List<User> getOnlineUsers();
    //boolean updateLastSeen(int userId, Timestamp lastSeen);// not supported yet

}

