package com.chatsystem.server.network;

import com.chatsystem.server.Controller.AuthController;
import com.chatsystem.server.Controller.UserController;
import com.chatsystem.server.Model.User;
import com.chatsystem.server.Model.Message.MessageType;
import com.chatsystem.server.services.MessageService;
import com.chatsystem.server.services.UserService;
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
    private String userKey = null; // user_id as key for online users

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
                    System.out.println("Received a Request form: " + clientSocket.getInetAddress().getHostName() + ", Request: " + request.getAction());
                    Response response = null;

                    if(request.getAction() != Action.SEND_MESSAGE_WITH_ATTACHMENT){
                        response = handleRequest(request);
                        if(response.getAction() == null) {
                            response = new Response(false, "Failed to process request", request.getAction());
                            System.out.println("The Response Action is null ");
                        }
                        String responseJson = gson.toJson(response);
                        writer.println(responseJson); // Send response back to client
                        if(response.getAction() == Action.LOGIN){
                            broadcastToOnlineUsers(new Response(true, "new user is online", Action.REFRESH));
                        }

                        if(request.getAction() == Action.SEND_MESSAGE){
                            // Handle sending message
                            if(response.isSuccess() && response.getMessageObj() != null && response.getMessageObj().getMessage_type() == MessageType.TEXT){
                                // send message to the receiver client if it's online
                                String receiverKey = String.valueOf(response.getMessageObj().getReceiver_id());
                                ClientHandler receiverHandler = getOnlineUser(receiverKey); 
                                if(receiverHandler != null && receiverHandler.running){
                                    // update message to be read
                                    response.getMessageObj().setIs_read(true);
                                    new MessageService().updateMessage(response.getMessageObj());
                                    response.setAction(Action.RECEIVE_MESSAGE);
                                    responseJson = gson.toJson(response);
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
                    writer.println(gson.toJson(new Response(false, "Invalid JSON format",Action.SEND_MESSAGE)));
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    handleRequest(new Request(Action.LOGOUT, null, new User(Integer.parseInt(userKey), null, null, null, running, null, null), null));
                    close();
                } catch (Exception e) {
//                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    close();
                }
            }
        } catch (Exception e){
//            e.printStackTrace();
            System.out.println(e.getMessage());
            close();
        } finally {
            close();
        }
    }

    private Response handleRequest(Request request) {
        switch (request.getAction()) {
            case LOGIN: {
                Response response = authController.login(request);
                if (response.isSuccess() && response.getUser() != null) {
                    userKey = String.valueOf(response.getUser().getUser_id());
                    onlineUsers.put(userKey, this);
                }
                return response;
            }
            case LOGOUT: {
                if (userKey != null) {
                    onlineUsers.remove(userKey);
                    userKey = null;
                }
                Response response = authController.logout(request);
                broadcastToOnlineUsers(new Response(true, "a user logout", Action.REFRESH));
                return response;
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
                return new Response(false, "Unknown action", request.getAction());
        }
    }

    public Response handleRequestWithAttachment(Request request) {
        try {
            if(request.getAction() == Action.SEND_MESSAGE_WITH_ATTACHMENT) {
                request = fileController.saveFile(request, clientSocket.getInputStream());
                if (request == null) {
                    return new Response(false, "File handling failed", Action.SEND_MESSAGE_WITH_ATTACHMENT);
                }
                
                Response response =  handleRequest(request);
                if (response != null && response.isSuccess() && response.getMessageObj() != null && response.getMessageObj().getMessage_type() != MessageType.TEXT) {
                    data = fileController.getFile(response);
                    return response;
                }
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
//            e.printStackTrace();
        }
        return new Response(false, "File handling failed", Action.SEND_MESSAGE_WITH_ATTACHMENT);
    }

    public static ClientHandler getOnlineUser(String key) {
        return onlineUsers.get(key);
    }

   public static void broadcastToOnlineUsers(Response response) {
        System.out.println("broadcastToOnlineUsers ...");
       for (ClientHandler handler : onlineUsers.values()) {
         handler.writer.println(handler.gson.toJson(response));
       }
   }

    public void close() {
        running = false;
        if (userKey != null){
            User user = new User(Integer.parseInt(userKey), null, null, null, false, null, null);
            authController.logout(new Request(Action.LOGOUT, null, user, null));
            onlineUsers.remove(userKey); // Remove from online users if userKey is set
        }
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
