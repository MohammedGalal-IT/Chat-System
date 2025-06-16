package com.chatsystem.server.tests.other;

import com.chatsystem.server.Model.Message;
import com.chatsystem.server.Model.User;
import com.chatsystem.server.network.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestGson {

    public static void main(String[] args) {
        // User user = new User("testUser", "hhhhh@fake.com",",fgfjgffbfdgb");
        Request request = new Request();
        request.setAction(com.chatsystem.server.network.Action.LOGIN);
        
        Gson gson = new Gson();
        String json = gson.toJson(request);
        System.out.println("Serialized JSON: " + json);

        Request deserializedRequest = gson.fromJson(json, Request.class);
        System.out.println("Deserialized Request Action: " + deserializedRequest);
    
}}
