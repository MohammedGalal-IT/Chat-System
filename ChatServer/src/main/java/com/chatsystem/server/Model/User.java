package com.chatsystem.server.Model;

import java.sql.Timestamp;

public class User {
    private int user_id;
    private String username;
    private String email;
    private String password_hash;
    private boolean isOnline;
    private String profile_picture;
    private Timestamp createdAt;

    public User(int user_id, String username, String email, String password_hash, boolean isOnline, String profile_picture, Timestamp createdAt) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.password_hash = password_hash;
        this.isOnline = isOnline;
        this.profile_picture = profile_picture;
        this.createdAt = createdAt;
    }

    public User(String username, String email, String password_hash, boolean isOnline) {
        this(-1, username, email, password_hash, isOnline, null, null);
    }

    public User(String username, String email, String password_hash) {
        this(-1, username, email, password_hash, false, null, null);
    }

   
    public int getUserId() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return password_hash;
    }

    public void setPasswordHash(String password_hash) {
        this.password_hash = password_hash;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        this.isOnline = online;
    }

    public String getProfilePicture() {
        return profile_picture;
    }

    public void setProfilePicture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password_hash='" + password_hash + '\'' +
                ", isOnline=" + isOnline +
                ", profile_picture='" + profile_picture + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}