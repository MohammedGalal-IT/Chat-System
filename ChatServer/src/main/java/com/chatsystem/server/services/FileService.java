package com.chatsystem.server.services;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import com.chatsystem.server.Model.Message.MessageType;
import com.chatsystem.server.Model.Message.FileFormat;;

public class FileService {

    private static final String UPLOADS_DIR = "uploads";  // changed to "uploads" when separate the server 

    public FileService() {
        File uploads = new File(UPLOADS_DIR);
        if (!uploads.exists()) {
            uploads.mkdirs();
        }
    }

    public  String saveFile(String subDir, String fileName, InputStream fileContent) throws IOException {
        Path dirPath = Paths.get(UPLOADS_DIR, subDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        Path filePath = dirPath.resolve(fileName);
        try (FileOutputStream out = new FileOutputStream(filePath.toFile())) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fileContent.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return filePath.toString();
    }

    public  String saveFile(String subDir, String fileName, InputStream fileContent, long fileLength) throws IOException {
        Path dirPath = Paths.get(UPLOADS_DIR, subDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        Path filePath = dirPath.resolve(fileName);
        long totalBytes = 0;
        long remaining = fileLength;
        try (FileOutputStream out = new FileOutputStream(filePath.toFile())) {
            // byte[] buffer = new byte[8192];
            // int bytesRead;
            // while (remaining > 0 && (bytesRead = fileContent.read(buffer, 0, (int)Math.min(buffer.length, remaining))) != -1) {
            //     out.write(buffer, 0, bytesRead);
            //     totalBytes += bytesRead;
            //     remaining -= bytesRead;
            //     System.out.println("write: " + bytesRead);
            // }
            // byte[] data = new byte[(int)fileLength];
            // dis.readFully(data);
            // out.write(data);

            BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent));
            String line = reader .readLine();
            byte[] bytes = Base64.getDecoder().decode(line);
            out.write(bytes);
            System.out.println("File Saved");
        }
//        // Validate written bytes match expected fileLength
//        if (totalBytes != fileLength) {
//            System.out.println("File length mismatch: expected " + fileLength + ", but wrote " + totalBytes);
//        }
        
        return filePath.toString();
    }

    public  String saveFile(MessageType type, String fileName, InputStream fileContent) throws IOException {
        
        switch (type) {
            case TEXT:
                return null;
            case IMAGE:
                return saveFile("images", fileName, fileContent);
             case VIDEO:
                return saveFile("videos", fileName, fileContent);
             case AUDIO:
                return saveFile("audio", fileName, fileContent);
        
            default:
                return saveFile("documents", fileName, fileContent);
        }
    }

    public  String saveFile(MessageType type, String fileName, InputStream fileContent, long fileLength) throws IOException {
        
        switch (type) {
            case TEXT:
                return null;
            case IMAGE:
                return saveFile("images", fileName, fileContent, fileLength);
             case VIDEO:
                return saveFile("videos", fileName, fileContent, fileLength);
             case AUDIO:
                return saveFile("audio", fileName, fileContent, fileLength);
        
            default:
                return saveFile("documents", fileName, fileContent, fileLength);
        }
    }

    public  boolean deleteFile(String fileUrl) {
        Path filePath = Paths.get(fileUrl);
        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public  boolean fileExists(String fileUrl) {
        Path filePath = Paths.get(fileUrl);
        return Files.exists(filePath);
    }

    public  byte[] readFile(String fileUrl) throws IOException {
        Path filePath = Paths.get(fileUrl);
        return Files.readAllBytes(filePath);
    }

    public void getFile(String fileUrl, OutputStream out) throws IOException{
        out.write(readFile(fileUrl));
        out.flush();
    }

    public String getFile(String fileUrl) throws IOException{
        Path filePath = Paths.get(fileUrl);
        byte[] data = Files.readAllBytes(filePath);
        return Base64.getEncoder().encodeToString(data);
    }

    public  void writeFile(byte[] bytes, String fileUrl) throws IOException {
        Path filePath = Paths.get(fileUrl);
        try (FileOutputStream out = new FileOutputStream(filePath.toFile())) {
            out.write(bytes);
        }
    }

    // Generate a random file name with the given format (extension)
    public String generateRandomFileName(FileFormat fileFormat) {
        String randomName = UUID.randomUUID().toString();
        String extension = fileFormat != null ? fileFormat.name().toLowerCase() : "bin";
        return randomName + "." + extension;
    }
}
