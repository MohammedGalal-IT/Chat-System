package com.chatsystem.server.services;

import com.chatsystem.server.DataAccess.interfaces.UserDao;
import com.chatsystem.server.DataAccess.repository.UserDaoImpl;
import com.chatsystem.server.Model.User;
import com.chatsystem.server.util.SecurityUtil;

public class AuthService {

    private final UserDao userDao;

    public AuthService() {
        this.userDao = new UserDaoImpl();
    }

    public User login(String email, String password) {
        User user = userDao.getByEmail(email);
        if(user != null && SecurityUtil.checkPassword(password, user.getPasswordHash())) {
            userDao.setOnline(user.getUser_id());
            return user; 
        }
        return null;
    }

    public boolean register(String username, String email, String password) {
        if (userDao.getByEmail(email) != null || userDao.getByUsername(username) != null) {
            return false;
        }
        return userDao.save(new User(
            username,
            email,
            SecurityUtil.hashPassword(password)
        ));
    }

    public boolean logout(int userId) {
        return userDao.setOffline(userId); 
    }
    
    public User validateToken(String token) {
        return userDao.getByUsername(token); // Using username as token for now
    }
    
    
}
