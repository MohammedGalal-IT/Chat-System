package com.chatsystem.server.services;

import com.chatsystem.server.DataAccess.interfaces.MessageDao;
import com.chatsystem.server.DataAccess.repository.MessageDaoImpl;
import com.chatsystem.server.Model.Message;
import com.chatsystem.server.Model.Message.MessageType;

import java.util.List;

public class MessageService {
    private final MessageDao messageDao;

    public MessageService() {
        this.messageDao = new MessageDaoImpl();
    }

    public Message sendMessage(Message message) {
        return messageDao.save(message);
    }

    public Message getMessageById(int id) {
        return messageDao.getById(id);
    }

    public List<Message> getMessagesByUserId(int userId) {
        return messageDao.getByUserId(userId);
    }

    public List<Message> getAllMessages() {
        return messageDao.getAllMessages();
    }

    public boolean updateMessage(Message message) {
        return messageDao.update(message);
    }

    public boolean deleteMessage(int id) {
        return messageDao.delete(id);
    }

    public List<Message> getUnreadMessages(int userId) {
        return messageDao.getUnreadMessages(userId);
    }

    public boolean markAsRead(int messageId) {
        return messageDao.markAsRead(messageId);
    }

    public List<Message> getMessagesBetweenUsers(int userId1, int userId2) {
        return messageDao.getMessagesBetweenUsers(userId1, userId2);
    }

    public List<Message> getMessagesByType(int userId, MessageType type) {
        return messageDao.getMessagesByType(userId, type);
    }
}
