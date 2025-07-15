package com.chatsystem.client.network;

import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import com.chatsystem.client.Model.Message.MessageType;
import com.chatsystem.client.services.FileService;

public class ClientSocketManager {

    private static ClientSocketManager instance;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private final Gson gson = new Gson();
    private Thread listenerThread;
    private volatile boolean running = false;
    private Consumer<Response> onResponse;

    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 8000;

    private ClientSocketManager() {
        connect(SERVER_ADDRESS, SERVER_PORT);
    }

    public boolean connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            running = true;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

     public static ClientSocketManager getInstance() {
        if (instance == null) {
            instance = new ClientSocketManager();
        }
        return instance;
    }

    public void setOnResponse(Consumer<Response> cb) {
        this.onResponse = cb;
    }

    // change to save the file at downloads directory and update the file path in message object for client url at response

    public void startListening() {
        if (listenerThread != null && listenerThread.isAlive()) return;
        listenerThread = new Thread(() -> {
            try {
                String line;
                while (running && (line = reader.readLine()) != null) {
                    Response response = gson.fromJson(line, Response.class);
                    FileService fileService = new FileService();
                    if (response.getMessageObj() != null && response.getMessageObj().getMessage_type() != MessageType.TEXT && response.getAction() == Action.RECEIVE_MESSAGE) {
                        String fileName = Paths.get(response.getMessageObj().file_path_server).getFileName().toString();
                        String filePath = fileService.saveFile(response.getMessageObj().getMessage_type(), fileName, socket.getInputStream(), response.getMessageObj().getFile_length());
                        response.getMessageObj().setFile_path_client(filePath);
                    }

                    if (onResponse != null) onResponse.accept(response);
                }
            } catch (IOException e) {
                if (running) e.printStackTrace();
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void sendRequest(Request request) {
         try {
            String json = gson.toJson(request);
            // dos.writeUTF(json);
            writer.println(json);
            writer.flush();
            if(request.getMessage() != null && request.getMessage().getMessage_type() != MessageType.TEXT) {
                // If the request has a file, we will handle it separately
                Path filePath = Paths.get(request.getMessage().getFile_path_client());
                byte[] fileBytes = Files.readAllBytes(filePath);
                // socket.getOutputStream().write(fileBytes);
                // socket.getOutputStream().flush();
                String base64 = Base64.getEncoder().encodeToString(fileBytes);
                writer.println(base64);
                writer.flush();
                System.out.println("Request with file sent: " + (request.getMessage().getMessage_type().toString()) + " ->" + request.getMessage().getFile_path_client());

                FileService fileService = new FileService();
                fileService.writeFile(fileBytes, "ChatClient/downloads" + "/test.png");
            }
            System.out.println("Request Sent to server: ");
        } catch (Exception e) {
            System.err.println("Error sending request to the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // public void sendRequestWithAttachment(Request request, InputStream fileStream, long fileLength) throws IOException {
    //     // Send the request JSON first
    //     sendRequest(request);
    //     // Then send the file bytes
    //     OutputStream out = socket.getOutputStream();
    //     byte[] buffer = new byte[8192];
    //     int bytesRead;
    //     long remaining = fileLength;
    //     while (remaining > 0 && (bytesRead = fileStream.read(buffer, 0, (int)Math.min(buffer.length, remaining))) != -1) {
    //         out.write(buffer, 0, bytesRead);
    //         remaining -= bytesRead;
    //     }
    //     out.flush();
    // }

    // استقبال رد
    // public Response receiveResponse() {
    //     try {
    //         String json = reader.readLine();
    //         return gson.fromJson(json, Response.class);
    //     } catch (IOException e) {
    //         System.err.println("Error reading response: " + e.getMessage());
    //     }
    //     return null;
    // }

    

    public void close() {
        running = false;
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}
