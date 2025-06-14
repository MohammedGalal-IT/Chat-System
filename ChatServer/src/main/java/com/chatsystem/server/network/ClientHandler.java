package com.chatsystem.server.network;

import com.chatsystem.server.Controller.AuthController;
import com.chatsystem.server.Controller.UserController;
import com.chatsystem.server.Model.Message.MessageType;
import com.chatsystem.server.Controller.MessageController;
import com.chatsystem.server.Controller.FileController;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Gson gson = new Gson();
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final AuthController authController;
    private final UserController userController;
    private final MessageController messageController;
    private final FileController fileController;
    private byte[] data;
    private volatile boolean running = true;
    private static final Map<String, ClientHandler> onlineUsers = new ConcurrentHashMap<>();
    private String userKey = null; // Track the logged-in user's key (e.g., email or userId as string)

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.authController = new AuthController();
        this.userController = new UserController();
        this.messageController = new MessageController();
        this.fileController = new FileController();
        this.data = null;
    }

    @Override
    public void run() {
        try {
            while (running && !clientSocket.isClosed()) {
                try {
                    String requestJson = reader.readLine();
                    if (requestJson == null) break;

                    Request request = gson.fromJson(requestJson, Request.class);
                    Response response = null;

                    if(request.getAction() != Action.SEND_MESSAGE_WITH_ATTACHMENT){
                        response = handleRequest(request);
                        String responseJson = gson.toJson(response);
                        writer.println(responseJson); // Send response back to client

                        if(request.getAction() == Action.SEND_MESSAGE){
                            // Handle sending message
                            if(response.isSuccess() && response.getMessageObj() != null && response.getMessageObj().getMessage_type() == MessageType.TEXT){
                                // send message to the receiver client if it's online
                                String receiverKey = String.valueOf(response.getMessageObj().getReceiver_id());
                                ClientHandler receiverHandler = getOnlineUser(receiverKey); 
                                if(receiverHandler != null && receiverHandler.running){
                                    receiverHandler.writer.println(responseJson);
                                }
                            }
                        }

                    } else{
                        response = handleRequestWithAttachment(request);
                        String responseJson = gson.toJson(response);
                        writer.println(responseJson); // Send response back to client

                        String receiverKey = String.valueOf(response.getMessageObj().getReceiver_id());
                        ClientHandler receiverHandler = getOnlineUser(receiverKey);
                            if(receiverHandler != null && receiverHandler.running){
                                receiverHandler.writer.println(responseJson);
                                 // send file data to the receiver client if it's online
                                 OutputStream out = clientSocket.getOutputStream();
                                 out.write(data);
                                 out.flush();
                            }
                        }
                }
                catch (JsonSyntaxException e) {
                    // Handle invalid JSON
                    writer.println(gson.toJson(new Response(false, "Invalid JSON format")));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private Response handleRequest(Request request) {
        switch (request.getAction()) {
            case LOGIN: {
                Response response = authController.login(request);
                if (response.isSuccess() && response.getUser() != null) {
                    userKey = String.valueOf(response.getUser().getUserId());
                    onlineUsers.put(userKey, this);
                }
                return response;
            }
            case LOGOUT: {
                if (userKey != null) {
                    onlineUsers.remove(userKey);
                    userKey = null;
                }
                return authController.logout(request);
            }
            case REGISTER:
                return authController.register(request);
            case SEND_MESSAGE:
            case GET_MESSAGES_BETWEEN_USERS:
            case GET_UNREAD_MESSAGES:
            case MARK_MESSAGES_AS_READ:
            case GET_MESSAGES_BY_TYPE:
            case DELETE_MESSAGE:
                return messageController.executeRequest(request);
            case GET_USER_BY_ID:
            case GET_USER_BY_EMAIL:
            case UPDATE_USER:
            case UPDATE_USER_PROFILE_PICTURE:
            case CHANGE_PASSWORD:
            case SEARCH_USERS:
            case GET_USERS:
            case GET_ONLINE_USERS:
                return userController.executeRequest(request);
            // File actions are not in Action enum, so remove them for now
            default:
                return new Response(false, "Unknown action");
        }
    }

    public Response handleRequestWithAttachment(Request request) {
        try {
            if(request.getAction() == Action.SEND_MESSAGE_WITH_ATTACHMENT) {
                request = fileController.saveFile(request, clientSocket.getInputStream());
                if (request == null) {
                    return new Response(false, "File handling failed");
                }
                
                Response response =  handleRequest(request);
                if (response != null && response.isSuccess() && response.getMessageObj() != null && response.getMessageObj().getMessage_type() != MessageType.TEXT) {
                    data = fileController.getFile(response);
                    return response;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return new Response(false, "File handling failed");
    }

    public static ClientHandler getOnlineUser(String key) {
        return onlineUsers.get(key);
    }

   public static void broadcastToOnlineUsers(Response response) {
       for (ClientHandler handler : onlineUsers.values()) {
         handler.writer.println(handler.gson.toJson(response));
       }
   }

    public void close() {
        running = false;
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserKey() {
        return userKey;
    }

    public boolean isRunning() {
        return running;
    }

    public byte[] getData() {
        return data;
    }

    public FileController getFileController() {
        return fileController;
    }

    public MessageController getMessageController() {
        return messageController;
    }

    public UserController getUserController() {
        return userController;
    }

    public AuthController getAuthController() {
        return authController;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public Gson getGson() {
        return gson;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
