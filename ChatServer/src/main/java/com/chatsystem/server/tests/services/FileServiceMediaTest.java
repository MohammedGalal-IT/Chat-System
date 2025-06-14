package com.chatsystem.server.tests.services;

import com.chatsystem.server.services.FileService;
import com.chatsystem.server.Model.Message.MessageType;
import com.chatsystem.server.Model.Message.FileFormat;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileServiceMediaTest {
    public static void main(String[] args) {
        FileService fileService = new FileService();
        String[] filePaths = {
            "C:/Users/Mohammed Galal/Downloads/The CRUD Operations in Programming (1).pdf",
            "C:/Users/Mohammed Galal/Downloads/deepseek_mermaid_20250603_bd8685.png",
            "C:/Users/Mohammed Galal/Downloads/Guna UI2 ControlBox Control.mp4"
        };
        MessageType[] types = {MessageType.FILE, MessageType.IMAGE, MessageType.VIDEO};
        FileFormat[] formats = {FileFormat.pdf, FileFormat.png, FileFormat.mp4};
        String[] subDirs = {"documents", "images", "videos"};

        for (int i = 0; i < filePaths.length; i++) {
            String originalPath = filePaths[i];
            MessageType type = types[i];
            FileFormat format = formats[i];
            String subDir = subDirs[i];
            String fileName = fileService.generateRandomFileName(format);
            String savedPath = null;
            try (InputStream in = new FileInputStream(originalPath)) {
                long fileLength = Files.size(Paths.get(originalPath));
                savedPath = fileService.saveFile(subDir, fileName, in, fileLength);
                System.out.println(type + " file saved at: " + savedPath);
            } catch (IOException e) {
                System.out.println("Failed to save " + type + ": " + e.getMessage());
                continue;
            }

            // Test fileExists
            boolean exists = fileService.fileExists(savedPath);
            System.out.println("File exists: " + exists);

            // Test readFile (just print size)
            try {
                byte[] readBytes = fileService.readFile(savedPath);
                System.out.println("Read file size: " + readBytes.length + " bytes");
            } catch (IOException e) {
                System.out.println("Failed to read file: " + e.getMessage());
            }
        }
    }
}
