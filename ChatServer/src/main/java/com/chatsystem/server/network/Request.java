package com.chatsystem.server.network;

import com.chatsystem.server.Model.Message;
import com.chatsystem.server.Model.User;



public class Request {

    private Action action;
    private String token;
    private User user;
    private Message message;
    private Object[] data;

    public Request(){}

    public Request(Action action, String token, User user, Message message) {
        this.action = action;
        this.token = token;
        this.user = user;
        this.message = message;
    }

    public Action getAction() {
        return action;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public Message getMessage() {
        return message;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Object[] getData() {
        return data;
    }
    public void setData(Object[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Request{" +
                "action=" + action +
                ", token='" + token + '\'' +
                ", user=" + user +
                ", message=" + message +
                ", data=" + data[0] +
                '}';
    }
}
