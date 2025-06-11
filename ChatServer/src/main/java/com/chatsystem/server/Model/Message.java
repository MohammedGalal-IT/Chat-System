package com.chatsystem.server.Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public class Message {

    private final int message_id;
    private int sender_id;
    private int receiver_id;
    private MessageType message_type;
    private String content;
    private FileFormat file_format;
    private long file_length;
    private String file_path_server;
    private String file_path_client;
    private boolean is_read;
    private Timestamp sentAt;
    private boolean isDeleted;


    public enum MessageType {
        TEXT, IMAGE, VIDEO, AUDIO, FILE, EMOJI
    }

    public enum FileFormat{
        jpg, png, mp4, mp3, pdf, docx, xlsx, zip, rar, txt
    }


    public Message(int message_id, int sender_id, int receiver_id, MessageType message_type, String content, FileFormat file_format, long file_length, String file_path_server, String file_path_client, boolean is_read, Timestamp sentAt, boolean isDeleted) {
        this.message_id = message_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.message_type = message_type;
        this.content = content;
        this.file_format = file_format;
        this.file_length = file_length;
        this.file_path_server = file_path_server;
        this.file_path_client = file_path_client;
        this.is_read = is_read;
        this.sentAt = sentAt;
        this.isDeleted = isDeleted;
    }

    public Message(int sender_id, int receiver_id, MessageType message_type, String content, FileFormat file_format, long file_length, String file_path_server, String file_path_client) {
        this(-1, sender_id, receiver_id, message_type, content, file_format, file_length, file_path_server, file_path_client, false, Timestamp.valueOf(LocalDateTime.now()), false);
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

    public FileFormat getFile_format() {
        return file_format;
    }

    public void setFile_length(long file_length) {
        this.file_length = file_length;
    }

    public long getFile_length() {
        return file_length;
    }


    public void setFile_format(FileFormat file_format) {
        this.file_format = file_format;
    }

    public String getFile_path_server() {
        return file_path_server;
    }

    public void setFile_path_server(String file_path_server) {
        this.file_path_server = file_path_server;
    }

    public String getFile_path_client() {
        return file_path_client;
    }

    public void setFile_path_client(String file_path_client) {
        this.file_path_client = file_path_client;
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


    @Override
    public String toString() {
        return "Message{" +
                "message_id=" + message_id +
                ", sender_id=" + sender_id +
                ", receiver_id=" + receiver_id +
                ", message_type=" + message_type +
                ", content='" + content + '\'' +
                ", file_format=" + file_format +
                ", file_length=" + file_length +
                ", file_path_server='" + file_path_server + '\'' +
                ", file_path_client='" + file_path_client + '\'' +
                ", is_read=" + is_read +
                ", sentAt=" + sentAt +
                ", isDeleted=" + isDeleted +
                '}';
                
    }
}
