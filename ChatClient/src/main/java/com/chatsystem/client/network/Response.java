package com.chatsystem.client.network;

import com.chatsystem.client.Model.*;

import java.util.List;

public class Response {

    public boolean success;
    public String message;
    public String token;
    public User user;
    public Message messageObj;  
    public List<Message> messages;
    public List<User> users;
    public Action action; 

    public Response() {}

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Response(boolean success, String message, User user, Action action) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.action = action;
    }

    public Response(boolean success, String message, Action action) {
        this.success = success;
        this.message = message;
        this.action = action;
    }

    // Getters & Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessageObj() {
        return messageObj;
    }

    public void setMessageObj(Message messageObj) {
        this.messageObj = messageObj;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", user=" + user +
                ", messageObj=" + messageObj +
                ", messages=" + messages +
                ", users=" + users +
                ", action=" + action.toString() +
                '}';
    }
}