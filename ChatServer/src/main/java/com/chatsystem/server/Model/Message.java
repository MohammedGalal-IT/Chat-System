package com.chatsystem.server.Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public class Message {

    private int message_id;
    private int sender_id;
    private int receiver_id;
    private MessageType message_type;
    private String content;
    private String file_path;
    private boolean is_read;
    private Timestamp sentAt;
    private boolean isDeleted;


    public enum MessageType {
        TEXT, IMAGE, VIDEO, AUDIO, FILE, EMOJI
    }


    public Message(int message_id, int sender_id, int receiver_id, MessageType message_type, String content, String file_path, boolean is_read, Timestamp sentAt, boolean isDeleted) {
        this.message_id = message_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.message_type = message_type;
        this.content = content;
        this.file_path = file_path;
        this.is_read = is_read;
        this.sentAt = sentAt;
        this.isDeleted = isDeleted;
    }

    public Message(int sender_id, int receiver_id, MessageType message_type, String content, String file_path) {
        this(0, sender_id, receiver_id, message_type, content, file_path, false, null, false);
    }

    public int getMessage_id() {
        return message_id;
    }
    
    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public MessageType getMessage_type() {
        return message_type;
    }

    public void setMessage_type(MessageType message_type) {
        this.message_type = message_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
