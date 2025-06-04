package com.chatsystem.server.DataAccess.interfaces;

import com.chatsystem.server.Model.Message;
import java.util.List;

public interface MessageDao {
    Message getById(int id);
    List<Message> getByUserId(int userId);
    boolean save(Message message);
    boolean update(Message message);
    boolean delete(int id);
    List<Message> getAllMessages();
}
