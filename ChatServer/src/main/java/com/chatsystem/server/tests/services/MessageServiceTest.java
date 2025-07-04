package com.chatsystem.server.tests.services;

import com.chatsystem.server.services.MessageService;
import com.chatsystem.server.Model.Message;
import com.chatsystem.server.Model.Message.MessageType;
import com.chatsystem.server.Model.Message.FileFormat;

import java.util.List;

public class MessageServiceTest {
    public static void main(String[] args) {
        MessageService messageService = new MessageService();

        // Test send message (TEXT)
        Message textMsg = new Message(1, 2, MessageType.TEXT, "Hello, this is a test message!", null, 0, null, null);
        boolean sent = messageService.sendMessage(textMsg) != null;
        System.out.println("Text message sent: " + sent);
        System.out.println();

        // Test send message (FILE)
        Message fileMsg = new Message(1, 2, MessageType.FILE, "Test file message", FileFormat.pdf, 12345L, "/server/path/file.pdf", "client/path/file.pdf");
        boolean fileSent = messageService.sendMessage(fileMsg) != null;
        System.out.println("File message sent: " + fileSent);
        System.out.println();

        // Test get message by id
        Message fetched = messageService.getMessageById(25);
        System.out.println("Fetched by ID: " + fetched);
        System.out.println();

        // Test get messages by user id
        List<Message> byUser = messageService.getMessagesByUserId(2);
        System.out.println("Messages by user 2: " + byUser);
        System.out.println();


        // Test get all messages
        List<Message> all = messageService.getAllMessages();
        System.out.println("All messages: " + all);
        System.out.println();

        // Test update message
        if (fetched != null) {
            fetched.setContent("Updated content");
            boolean updated = messageService.updateMessage(fetched);
            System.out.println("Message updated: " + updated);
            Message afterUpdate = messageService.getMessageById(fetched.getMessage_id());
            System.out.println("After update: " + afterUpdate);
        }
        System.out.println();

        // Test get unread messages
        List<Message> unread = messageService.getUnreadMessages(2);
        System.out.println("Unread messages for user 2: " + unread);
        System.out.println();

        // Test mark as read
        if (fetched != null) {
            boolean marked = messageService.markAsRead(fetched.getMessage_id());
            System.out.println("Message marked as read: " + marked);
        }
        System.out.println();

        // Test get messages between users
        List<Message> between = messageService.getMessagesBetweenUsers(1, 2);
        System.out.println("Messages between user 1 and 2: " + between);
        System.out.println();

        // Test get messages by type
        List<Message> byType = messageService.getMessagesByType(2, MessageType.FILE);
        System.out.println("File messages for user 2: " + byType);
        System.out.println();

        // Test delete message
        if (fetched != null) {
            boolean deleted = messageService.deleteMessage(fetched.getMessage_id());
            System.out.println("Message deleted: " + deleted);
            System.out.println();
        }
    }
}
