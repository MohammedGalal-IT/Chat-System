package com.chatsystem.server.tests.controllers;

import com.chatsystem.server.Controller.FileController;
import com.chatsystem.server.Model.Message;
import com.chatsystem.server.Model.User;
import com.chatsystem.server.Model.Message.MessageType;
import com.chatsystem.server.Model.Message.FileFormat;
import com.chatsystem.server.network.Request;
import com.chatsystem.server.network.Response;
import com.chatsystem.server.services.UserService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.Arrays;

public class FileControllerRealFilesTest {
    public static void main(String[] args) throws IOException {
        FileController fileController = new FileController();
         User sender = new User("alice", "alice@example.com", "pass");
        User receiver = new User("bob", "bob@example.com", "pass");
        UserService userService = new UserService();
        sender = userService.getUserByEmail(sender.getEmail());
        receiver = userService.getUserByEmail(receiver.getEmail());

        // Test files
        String[] filePaths = {
            "C:/Users/Mohammed Galal/Downloads/The CRUD Operations in Programming (1).pdf",
            "C:/Users/Mohammed Galal/Downloads/deepseek_mermaid_20250603_bd8685.png",
            "C:/Users/Mohammed Galal/Downloads/Guna UI2 ControlBox Control.mp4",
            "C:/Users/Mohammed Galal/Downloads/Telegram Desktop/روابط مواقع اسلام + حمدي.txt"
        };
        MessageType[] types = {MessageType.FILE, MessageType.IMAGE, MessageType.VIDEO, MessageType.FILE};
        FileFormat[] formats = {FileFormat.pdf, FileFormat.png, FileFormat.mp4, FileFormat.txt};

        for (int i = 0; i < filePaths.length; i++) {
            String path = filePaths[i];
            MessageType type = types[i];
            FileFormat format = formats[i];
            File file = new File(path);
            if (!file.exists()) {
                System.out.println("File does not exist: " + path);
                continue;
            }
            long fileLength = file.length();
            InputStream fileStream = new FileInputStream(file);
            Message fileMessage = new Message(sender.getUserId(), receiver.getUserId(), type, null, format, fileLength, null, null);
            fileMessage.setFile_length(fileLength);
            Request saveReq = new Request();
            saveReq.setMessage(fileMessage);
            Request resultReq = fileController.saveFile(saveReq, fileStream);
            if (resultReq != null && resultReq.getMessage().getFile_path_server() != null) {
                System.out.println("Saved: " + path + " -> " + resultReq.getMessage().getFile_path_server());
                // Try to read back
                Response resp = new Response();
                resp.setMessageObj(resultReq.getMessage());
                byte[] loadedBytes = fileController.getFile(resp);
                if (loadedBytes != null && loadedBytes.length == fileLength) {
                    System.out.println("Loaded successfully, size: " + loadedBytes.length);
                } else if (loadedBytes != null) {
                    System.out.println("Loaded but size mismatch: " + loadedBytes.length + " vs " + fileLength);
                } else {
                    System.out.println("Failed to load file");
                }
            } else {
                System.out.println("Failed to save: " + path);
            }
        }
    }
}
