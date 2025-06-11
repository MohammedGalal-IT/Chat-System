package com.chatsystem.server.DataAccess.repository;

import com.chatsystem.server.DataAccess.interfaces.MessageDao;
import com.chatsystem.server.Model.Message;
import com.chatsystem.server.Model.Message.MessageType;
import com.chatsystem.server.util.DatabaseManager;
import java.util.List;

public class MessageDaoImpl implements MessageDao {
    @Override
    public Message getById(int id) {
        return DatabaseManager.executeQuerySingle(
            "SELECT * FROM messages WHERE message_id = ?",
            rs -> new Message(
                rs.getInt("message_id"),
                rs.getInt("sender_id"),
                rs.getInt("receiver_id"),
                MessageType.valueOf(rs.getString("message_type")),
                rs.getString("content"),
                rs.getString("file_format") == null ? null : Message.FileFormat.valueOf(rs.getString("file_format")),
                rs.getLong("file_length"),
                rs.getString("file_path_server"),
                rs.getString("file_path_client"),
                rs.getBoolean("is_read"),
                rs.getTimestamp("sent_at"),
                rs.getBoolean("is_deleted")
            ),
            id
        );
    }

    @Override
    public List<Message> getByUserId(int userId) {
        return DatabaseManager.executeQueryList(
            "SELECT * FROM messages WHERE sender_id = ? OR receiver_id = ?",
            rs -> new Message(
                rs.getInt("message_id"),
                rs.getInt("sender_id"),
                rs.getInt("receiver_id"),
                MessageType.valueOf(rs.getString("message_type")),
                rs.getString("content"),
                rs.getString("file_format") == null ? null : Message.FileFormat.valueOf(rs.getString("file_format")),
                rs.getLong("file_length"),
                rs.getString("file_path_server"),
                rs.getString("file_path_client"),
                rs.getBoolean("is_read"),
                rs.getTimestamp("sent_at"),
                rs.getBoolean("is_deleted")
            ),
            userId, userId
        );
    }

    @Override
    public boolean save(Message message) {
        return DatabaseManager.executeUpdate(
            "INSERT INTO messages (sender_id, receiver_id, message_type, content, file_format, file_length, file_path_server, file_path_client) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            message.getSender_id(),
            message.getReceiver_id(),
            message.getMessage_type().name(),
            message.getContent(),
            message.getFile_format() == null ? null : message.getFile_format().name(),
            message.getFile_length(),
            message.getFile_path_server(),
            message.getFile_path_client()
        ) > 0;
    }

    @Override
    public boolean update(Message message) {
        return DatabaseManager.executeUpdate(
            "UPDATE messages SET sender_id = ?, receiver_id = ?, message_type = ?, content = ?, file_format = ?, file_length = ?, file_path_server = ?, file_path_client = ?, is_read = ?, sent_at = ?, is_deleted = ? WHERE message_id = ?",
            message.getSender_id(),
            message.getReceiver_id(),
            message.getMessage_type().name(),
            message.getContent(),
            message.getFile_format() == null ? null : message.getFile_format().name(),
            message.getFile_length(),
            message.getFile_path_server(),
            message.getFile_path_client(),
            message.isIs_read(),
            message.getSentAt(),
            message.isDeleted(),
            message.getMessage_id()
        ) > 0;
    }

    @Override
    public boolean delete(int id) {
        return DatabaseManager.executeUpdate(
            "DELETE FROM messages WHERE message_id = ?",
            id
        ) > 0;
    }

    @Override
    public List<Message> getAllMessages() {
        return DatabaseManager.executeQueryList(
            "SELECT * FROM messages",
            rs -> new Message(
                rs.getInt("message_id"),
                rs.getInt("sender_id"),
                rs.getInt("receiver_id"),
                MessageType.valueOf(rs.getString("message_type")),
                rs.getString("content"),
                rs.getString("file_format") == null ? null : Message.FileFormat.valueOf(rs.getString("file_format")),
                rs.getLong("file_length"),
                rs.getString("file_path_server"),
                rs.getString("file_path_client"),
                rs.getBoolean("is_read"),
                rs.getTimestamp("sent_at"),
                rs.getBoolean("is_deleted")
            )
        );
    }

    @Override
    public List<Message> getUnreadMessages(int userId) {
        return DatabaseManager.executeQueryList(
            "SELECT * FROM messages WHERE (receiver_id = ? OR sender_id = ?) AND is_read = FALSE",
            rs -> new Message(
                rs.getInt("message_id"),
                rs.getInt("sender_id"),
                rs.getInt("receiver_id"),
                MessageType.valueOf(rs.getString("message_type")),
                rs.getString("content"),
                rs.getString("file_format") == null ? null : Message.FileFormat.valueOf(rs.getString("file_format")),
                rs.getLong("file_length"),
                rs.getString("file_path_server"),
                rs.getString("file_path_client"),
                rs.getBoolean("is_read"),
                rs.getTimestamp("sent_at"),
                rs.getBoolean("is_deleted")
            ),
            userId, userId
        );
    }

    @Override
    public boolean markAsRead(int messageId) {
        return DatabaseManager.executeUpdate(
            "UPDATE messages SET is_read = TRUE WHERE message_id = ?",
            messageId
        ) > 0;
    }

    @Override
    public List<Message> getMessagesBetweenUsers(int userId1, int userId2) {
        return DatabaseManager.executeQueryList(
            "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY sent_at ASC",
            rs -> new Message(
                rs.getInt("message_id"),
                rs.getInt("sender_id"),
                rs.getInt("receiver_id"),
                MessageType.valueOf(rs.getString("message_type")),
                rs.getString("content"),
                rs.getString("file_format") == null ? null : Message.FileFormat.valueOf(rs.getString("file_format")),
                rs.getLong("file_length"),
                rs.getString("file_path_server"),
                rs.getString("file_path_client"),
                rs.getBoolean("is_read"),
                rs.getTimestamp("sent_at"),
                rs.getBoolean("is_deleted")
            ),
            userId1, userId2, userId2, userId1
        );
    }

    @Override
    public List<Message> getMessagesByType(int userId, MessageType type) {
        return DatabaseManager.executeQueryList(
            "SELECT * FROM messages WHERE (sender_id = ? OR receiver_id = ?) AND message_type = ?",
            rs -> new Message(
                rs.getInt("message_id"),
                rs.getInt("sender_id"),
                rs.getInt("receiver_id"),
                MessageType.valueOf(rs.getString("message_type")),
                rs.getString("content"),
                rs.getString("file_format") == null ? null : Message.FileFormat.valueOf(rs.getString("file_format")),
                rs.getLong("file_length"),
                rs.getString("file_path_server"),
                rs.getString("file_path_client"),
                rs.getBoolean("is_read"),
                rs.getTimestamp("sent_at"),
                rs.getBoolean("is_deleted")
            ),
            userId, userId, type.name()
        );
    }
}
