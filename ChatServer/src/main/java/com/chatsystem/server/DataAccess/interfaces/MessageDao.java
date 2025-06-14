package com.chatsystem.server.DataAccess.interfaces;

import com.chatsystem.server.Model.Message;
import java.util.List;

public interface MessageDao {
    Message getById(int id);
    List<Message> getByUserId(int userId);
    Message save(Message message); // Changed to return Message for consistency
    boolean update(Message message);
    boolean delete(int id);
    List<Message> getAllMessages();
    List<Message> getUnreadMessages(int userId);
    boolean markAsRead(int messageId);
    List<Message> getMessagesBetweenUsers(int userId1, int userId2);
    List<Message> getMessagesByType(int userId, Message.MessageType type);
}
