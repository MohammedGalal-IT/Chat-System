package com.chatsystem.server.DataAccess.interfaces;

import com.chatsystem.server.Model.User;
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
}

