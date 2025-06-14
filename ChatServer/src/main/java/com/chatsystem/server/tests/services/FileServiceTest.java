package com.chatsystem.server.tests.services;

import com.chatsystem.server.services.FileService;
import com.chatsystem.server.Model.Message.MessageType;
import com.chatsystem.server.Model.Message.FileFormat;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileServiceTest {
    public static void main(String[] args) {
        FileService fileService = new FileService();
        String testContent = "This is a test file content.";
        byte[] testBytes = testContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(testBytes);
        String fileName = fileService.generateRandomFileName(FileFormat.txt);
        String subDir = "documents";
        String filePath = null;

        // Test saveFile (basic)
        try {
            filePath = fileService.saveFile(subDir, fileName, inputStream);
            System.out.println("File saved at: " + filePath);
        } catch (IOException e) {
            System.out.println("saveFile failed: " + e.getMessage());
        }

        // Test fileExists
        boolean exists = fileService.fileExists(filePath);
        System.out.println("File exists: " + exists);

        // Test readFile
        try {
            byte[] readBytes = fileService.readFile(filePath);
            System.out.println("Read file content: " + new String(readBytes, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("readFile failed: " + e.getMessage());
        }

        // Test writeFile (overwrite)
        String newContent = "Overwritten content.";
        try {
            fileService.writeFile(newContent.getBytes(StandardCharsets.UTF_8), filePath);
            byte[] readBytes = fileService.readFile(filePath);
            System.out.println("After overwrite: " + new String(readBytes, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("writeFile failed: " + e.getMessage());
        }

        // Test saveFile with fileLength
        InputStream inputStream2 = new ByteArrayInputStream(testBytes);
        String fileName2 = fileService.generateRandomFileName(FileFormat.txt);
        String filePath2 = null;
        try {
            filePath2 = fileService.saveFile(subDir, fileName2, inputStream2, testBytes.length);
            System.out.println("File saved with length at: " + filePath2);
        } catch (IOException e) {
            System.out.println("saveFile (with length) failed: " + e.getMessage());
        }

        // Test deleteFile
        boolean deleted = fileService.deleteFile(filePath);
        System.out.println("File deleted: " + deleted);
        boolean deleted2 = fileService.deleteFile(filePath2);
        System.out.println("File deleted (2): " + deleted2);
    }
}
