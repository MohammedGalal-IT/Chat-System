package com.chatsystem.client.network;

import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;
import java.util.function.BiConsumer;
import com.chatsystem.client.Model.Message.MessageType;

public class ClientSocketManager {

    private static ClientSocketManager instance;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private final Gson gson = new Gson();
    private Thread listenerThread;
    private volatile boolean running = false;
    private BiConsumer<Response, byte[]> onResponse;

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

    public void setOnResponse(BiConsumer<Response, byte[]> cb) {
        this.onResponse = cb;
    }

    public void startListening() {
        if (listenerThread != null && listenerThread.isAlive()) return;
        listenerThread = new Thread(() -> {
            try {
                String line;
                while (running && (line = reader.readLine()) != null) {
                    Response response = gson.fromJson(line, Response.class);
                    byte[] fileData = null;
                    if (response.getMessageObj() != null && response.getMessageObj().getMessage_type() != MessageType.TEXT) {
                        long fileLength = response.getMessageObj().getFile_length();
                        if (fileLength > 0) {
                            fileData = new byte[(int) fileLength];
                            InputStream in = socket.getInputStream();
                            int totalRead = 0;
                            while (totalRead < fileLength) {
                                int read = in.read(fileData, totalRead, (int)(fileLength - totalRead));
                                if (read == -1) break;
                                totalRead += read;
                            }
                        }
                    }
                    if (onResponse != null) onResponse.accept(response, fileData);
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
            writer.println(json);
            System.out.println("Request Sent to server: ");
        } catch (Exception e) {
            System.err.println("Error sending request: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendRequestWithAttachment(Request request, InputStream fileStream, long fileLength) throws IOException {
        // Send the request JSON first
        sendRequest(request);
        // Then send the file bytes
        OutputStream out = socket.getOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        long remaining = fileLength;
        while (remaining > 0 && (bytesRead = fileStream.read(buffer, 0, (int)Math.min(buffer.length, remaining))) != -1) {
            out.write(buffer, 0, bytesRead);
            remaining -= bytesRead;
        }
        out.flush();
    }

    // استقبال رد
    public Response receiveResponse() {
        try {
            String json = reader.readLine();
            return gson.fromJson(json, Response.class);
        } catch (IOException e) {
            System.err.println("Error reading response: " + e.getMessage());
        }
        return null;
    }

    

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
