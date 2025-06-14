package com.chatsystem.server.tests.data;

import com.chatsystem.server.DataAccess.interfaces.MessageDao;
import com.chatsystem.server.DataAccess.repository.MessageDaoImpl;
import com.chatsystem.server.Model.Message;
import com.chatsystem.server.Model.Message.MessageType;
import com.chatsystem.server.Model.Message.FileFormat;

import java.sql.Timestamp;
import java.util.List;

public class MessageDaoTest {
    public void runTest() {
        MessageDao messageDao = new MessageDaoImpl();
        System.out.println("--- Testing MessageDaoImpl ---");

        // Test save
        Message msg = new Message(1, 2, MessageType.FILE, "Test file message", FileFormat.pdf, 12345L, "/server/path/file.pdf", "client/path/file.pdf");
        Message saved = messageDao.save(msg);
        System.out.println("Message saved: " + saved);

        // Test getById
        Message fetchedById = messageDao.getById(4);
        System.out.println("Fetched by ID: " + fetchedById);

        // Test getByUserId
        List<Message> byUser = messageDao.getByUserId(2);
        System.out.println("Messages by user 2: " + byUser);

        // Test getAllMessages
        List<Message> allMessages = messageDao.getAllMessages();
        System.out.println("All messages: " + allMessages);

        // Test update
        if (fetchedById != null) {
            fetchedById.setContent("Updated content");
            boolean updated = messageDao.update(fetchedById);
            System.out.println("Message updated: " + updated);
            System.out.println("After update: " + messageDao.getById(fetchedById.getMessage_id()));
        }

        // Test getUnreadMessages
        List<Message> unread = messageDao.getUnreadMessages(2);
        System.out.println("Unread messages for user 2: " + unread);

        // Test markAsRead
        if (fetchedById != null) {
            boolean marked = messageDao.markAsRead(fetchedById.getMessage_id());
            System.out.println("Message marked as read: " + marked);
        }

        // Test getMessagesBetweenUsers
        List<Message> between = messageDao.getMessagesBetweenUsers(1, 2);
        System.out.println("Messages between user 1 and 2: " + between);

        // Test getMessagesByType
        List<Message> byType = messageDao.getMessagesByType(2, MessageType.FILE);
        System.out.println("File messages for user 2: " + byType);

        // Test delete
        if (fetchedById != null) {
            boolean deleted = messageDao.delete(fetchedById.getMessage_id());
            System.out.println("Message deleted: " + deleted);
        }
    }
}
