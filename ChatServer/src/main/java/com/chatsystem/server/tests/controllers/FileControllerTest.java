package com.chatsystem.server.tests.controllers;

import com.chatsystem.server.Controller.FileController;
import com.chatsystem.server.Model.Message;
import com.chatsystem.server.Model.User;
import com.chatsystem.server.Model.Message.MessageType;
import com.chatsystem.server.Model.Message.FileFormat;
import com.chatsystem.server.network.Request;
import com.chatsystem.server.network.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FileControllerTest {
    public static void main(String[] args) throws IOException {
        FileController fileController = new FileController();

        // Prepare a test user and message
        User sender = new User("fileuser", "fileuser@example.com", "pass");
        User receiver = new User("fileuser2", "fileuser2@example.com", "pass");
        Message fileMessage = new Message(sender.getUserId(), receiver.getUserId(), MessageType.IMAGE, null, FileFormat.jpg, 10, null, null);

        // Simulate file content
        byte[] fileBytes = "testimagecontent".getBytes(StandardCharsets.UTF_8);
        InputStream fileStream = new ByteArrayInputStream(fileBytes);
        fileMessage.setFile_length(fileBytes.length);

        // Test saveFile
        Request saveReq = new Request();
        saveReq.setMessage(fileMessage);
        Request resultReq = fileController.saveFile(saveReq, fileStream);
        if (resultReq != null && resultReq.getMessage().getFile_path_server() != null) {
            System.out.println("File saved at: " + resultReq.getMessage().getFile_path_server());
        } else {
            System.out.println("File save failed");
        }

        // Test getFile
        if (resultReq != null) {
            Response resp = new Response();
            resp.setMessageObj(resultReq.getMessage());
            byte[] loadedBytes = fileController.getFile(resp);
            if (loadedBytes != null && Arrays.equals(fileBytes, loadedBytes)) {
                System.out.println("File loaded successfully and content matches");
            } else if (loadedBytes != null) {
                System.out.println("File loaded but content does not match");
            } else {
                System.out.println("File load failed");
            }
        }
    }
}
