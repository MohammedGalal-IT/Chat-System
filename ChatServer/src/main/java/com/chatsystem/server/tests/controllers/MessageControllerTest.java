package com.chatsystem.server.tests.controllers;

import com.chatsystem.server.Controller.MessageController;
import com.chatsystem.server.Controller.UserController;
import com.chatsystem.server.Model.Message;
import com.chatsystem.server.Model.User;
import com.chatsystem.server.Model.Message.MessageType;
import com.chatsystem.server.network.Action;
import com.chatsystem.server.network.Request;
import com.chatsystem.server.network.Response;
import com.chatsystem.server.services.UserService;

public class MessageControllerTest {
    public static void main(String[] args) {
        MessageController messageController = new MessageController();

        // Test users
        User sender = new User("alice", "alice@example.com", "pass");
        User receiver = new User("bob", "bob@example.com", "pass");
        UserService userService = new UserService();
        sender = userService.getUserByEmail(sender.getEmail());
        receiver = userService.getUserByEmail(receiver.getEmail());

        Message textMessage = new Message(sender.getUserId(), receiver.getUserId(), MessageType.TEXT, "Hello from MessageControllerTest", null, 0, null, null);
        // Create a file message
        Message fileMessage = new Message(sender.getUserId(), receiver.getUserId(), MessageType.IMAGE, null, Message.FileFormat.jpg, 1234, "/server/path.jpg", "/client/path.jpg");

        // Test send text message
        Request sendTextReq = new Request();
        sendTextReq.setAction(Action.SEND_MESSAGE);
        sendTextReq.setMessage(textMessage);
        Response sendTextResp = messageController.executeRequest(sendTextReq);
        System.out.println("Send text message: " + sendTextResp.getMessage() + ", Success: " + sendTextResp.isSuccess());
        System.out.println();

        // Test send message with attachment
        Request sendFileReq = new Request();
        sendFileReq.setAction(Action.SEND_MESSAGE_WITH_ATTACHMENT);
        sendFileReq.setMessage(fileMessage);
        Response sendFileResp = messageController.executeRequest(sendFileReq);
        System.out.println("Send file message: " + sendFileResp.getMessage() + ", Success: " + sendFileResp.isSuccess());
        System.out.println();

        // Test get messages between users
        Request betweenReq = new Request();
        betweenReq.setAction(Action.GET_MESSAGES_BETWEEN_USERS);
        betweenReq.setData(new Object[]{sender, receiver});
        Response betweenResp = messageController.executeRequest(betweenReq);
        System.out.println("Get messages between users: " + betweenResp.getMessage() + ", Messages: " + betweenResp.getMessages());
        System.out.println();

        // Test get unread messages for receiver
        Request unreadReq = new Request();
        unreadReq.setAction(Action.GET_UNREAD_MESSAGES);
        unreadReq.setData(new Object[]{receiver});
        Response unreadResp = messageController.executeRequest(unreadReq);
        System.out.println("Get unread messages: " + unreadResp.getMessage() + ", Messages: " + unreadResp.getMessages());
        System.out.println();

        // Test mark message as read (simulate with textMessage)
        Request markReadReq = new Request();
        markReadReq.setAction(Action.MARK_MESSAGES_AS_READ);
        markReadReq.setMessage(sendTextResp.getMessageObj());
        Response markReadResp = messageController.executeRequest(markReadReq);
        System.out.println("Mark messages as read: " + markReadResp.getMessage() + ", Success: " + markReadResp.isSuccess());
        System.out.println();

        // Test get messages by type (IMAGE)
        Request byTypeReq = new Request();
        byTypeReq.setAction(Action.GET_MESSAGES_BY_TYPE);
        byTypeReq.setData(new Object[]{sender, fileMessage});
        Response byTypeResp = messageController.executeRequest(byTypeReq);
        System.out.println("Get messages by type: " + byTypeResp.getMessage() + ", Messages: " + byTypeResp.getMessages());
        System.out.println();

        // Test delete message (simulate with textMessage)
        Request deleteReq = new Request();
        deleteReq.setAction(Action.DELETE_MESSAGE);
        deleteReq.setData(new Object[]{sendTextResp.getMessageObj()});
        Response deleteResp = messageController.executeRequest(deleteReq);
        System.out.println("Delete message: " + deleteResp.getMessage() + ", Success: " + deleteResp.isSuccess());
        System.out.println();
    }
}
