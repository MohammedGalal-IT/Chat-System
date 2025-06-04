package com.chatsystem.server;

import com.chatsystem.server.DataAccess.repository.UserDaoImpl;
import com.chatsystem.server.DataAccess.interfaces.UserDao;
import com.chatsystem.server.Model.User;
import com.chatsystem.server.DataAccess.repository.MessageDaoImpl;
import com.chatsystem.server.DataAccess.interfaces.MessageDao;
import com.chatsystem.server.Model.Message;
import com.chatsystem.server.Model.Message.MessageType;
import java.sql.Timestamp;

public class ServerMain {
    public static void main(String[] args) {
        // Test: Add users to the database
        // UserDao userDao = new UserDaoImpl();
        // User user1 = new User("alice", "alice@example.com", "password123", true);
        // User user2 = new User("bob", "bob@example.com", "securepass", false);
        // User user3 = new User("charlie", "charlie@example.com", "charliepass", true);
        
        // System.out.println("Adding users...");
        // boolean added1 = userDao.save(user1);
        // boolean added2 = userDao.save(user2);
        // boolean added3 = userDao.save(user3);
        // System.out.println("User 1 added: " + added1);
        // System.out.println("User 2 added: " + added2);
        // System.out.println("User 3 added: " + added3);

        // Test: Add and fetch messages
         MessageDao messageDao = new MessageDaoImpl();
        // Message msg1 = new Message(1, 2, MessageType.TEXT, "Hello Bob!", null);
        // Message msg2 = new Message(2, 1, MessageType.TEXT, "Hi Alice!", null);
        // Message msg3 = new Message(3, 1, MessageType.IMAGE, "Check this out!", "path/to/image.jpg");
        
        // System.out.println("Adding messages...");
        // boolean m1 = messageDao.save(msg1);
        // boolean m2 = messageDao.save(msg2);
        // boolean m3 = messageDao.save(msg3);
        // System.out.println("Message 1 added: " + m1);
        // System.out.println("Message 2 added: " + m2);
        // System.out.println("Message 3 added: " + m3);

        // System.out.println("Fetching all messages:");
        // for (Message m : messageDao.getAllMessages()) {
        //     System.out.println("Message: " + m.getContent() + ", Type: " + m.getMessage_type() + ", Sender: " + m.getSender_id() + ", Receiver: " + m.getReceiver_id());
        // }
    }
}
