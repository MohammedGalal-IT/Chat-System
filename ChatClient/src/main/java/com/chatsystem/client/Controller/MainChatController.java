package com.chatsystem.client.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.nio.file.Path;
import java.util.List;

import com.chatsystem.client.Model.Message;
import com.chatsystem.client.Model.User;
import com.chatsystem.client.network.Action;
import com.chatsystem.client.network.ClientSocketManager;
import com.chatsystem.client.network.Request;
import com.chatsystem.client.util.Session;

public class MainChatController {

    @FXML private ListView<User> contactListView;
    @FXML private Label chatTitleLabel;
    @FXML private ImageView chatIconView;
    @FXML private ScrollPane chatScrollPane;
    @FXML private VBox messagesContainer;
    @FXML private TextField messageTextField;
    private User selectedUser;
    private ClientSocketManager clientSocketManager;



    @FXML
    public void initialize() {
        clientSocketManager = ClientSocketManager.getInstance();
        clientSocketManager.setOnResponse((response, fileData) -> {
            if(response != null) System.out.println("Request Received");

            switch(response.getAction()) {

                case Action.GET_ONLINE_USERS:
                    if (response.isSuccess()) {
                     Platform.runLater(() -> {
                        if(response.getUsers() != null) {
                            loadContacts(response.getUsers());
                            System.out.println("Online users loaded successfully.");
                        } else {
                            showError("No online users found.");
                        }
                     });
                } else {
                    Platform.runLater(() -> showError("Register failed: " + response.getMessage()));
                }
             break;

                case Action.GET_MESSAGES_BETWEEN_USERS:
                System.out.println("Received messages between users");
                    if (response.isSuccess()) {
                        List<Message> messages = response.getMessages();
                        Platform.runLater(() -> loadMessages(messages));
                    } else {
                        Platform.runLater(() -> showError("Failed to load messages: " + response.getMessage()));
                    }
                    break;

                    case Action.REFRESH:
                    System.out.println("refresh ....");
                    requestOnlineUsers();
                    break;

                    case Action.RECEIVE_MESSAGE:
                    System.out.println("received a message: " + response.getMessage());
                    Platform.runLater(() -> loadMessage(response.getMessageObj()));
                    break;

                default:
                    System.out.println("Unhandled action: " + response.getMessage());
            }
        });
        // clientSocketManager.startListening();

        // Request online users when the controller is initialized
        requestOnlineUsers();

        // Set up the contact list view
        contactListView.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getUsername());
                }
            }
        });

        // Add action when an item is selected
        contactListView.getSelectionModel().selectedItemProperty().addListener((observable, oldUser, newUser) ->{
            if(newUser != null && Session.currentUser != null) {
                Request request2 = new Request();
                request2.setAction(Action.GET_MESSAGES_BETWEEN_USERS);
                request2.setUser(Session.currentUser);
                request2.setData(new Object[]{Session.currentUser.getUser_id()+"", newUser.getUser_id()+""});
                clientSocketManager.sendRequest(request2);
                this.selectedUser = newUser; // Store the selected user
                chatTitleLabel.setText(newUser.getUsername());
                
                // Set chat icon if available [for now, just a placeholder]
                }
        });
    }


    private void requestOnlineUsers(){
        Request request = new Request();
        request.setUser(Session.currentUser);
        request.setAction(Action.GET_ONLINE_USERS);
        clientSocketManager.sendRequest(request);
    }

    private void loadContacts(List<User> contacts) {
        contactListView.getItems().clear();
        for (User contact : contacts) {
            // Exclude the current user from the contact list
            if (Session.currentUser == null || contact.getUser_id() != Session.currentUser.getUser_id()) {
            contactListView.getItems().add(contact);
            }
        }
    }

    
    private void loadMessages(List<Message> messages) {
        System.out.println("Loading messages...");
        messagesContainer.getChildren().clear();
        if (messages == null || messages.isEmpty()) {
            System.out.println("No messages found.");
            return;
        }
        // loop through messages
        // check the message "sender" and "receiver" to determine how to display it
        // check the message type (text, image, file, etc.) to display accordingly
        for (Message message : messages) {
            Label msgLabel = new Label(message.getContent());
            messagesContainer.getChildren().add(msgLabel);
        }
        System.out.println("Messages loaded successfully.");
    }

    private void loadMessage(Message message){
        Label msgLabel = new Label(message.getContent());
        messagesContainer.getChildren().add(msgLabel);
    }

    public void setSelectedUser(User user) {
        this.selectedUser = user;
        // load user-specific data if needed
    }

    @FXML
    private void onSendMessage() {
        String text = messageTextField.getText().trim();
        if (!text.isEmpty()) {
            Message msg = new Message();
            msg.setContent(text);
            msg.setSender_id(Session.currentUser.getUser_id());
            msg.setReceiver_id(selectedUser.getUser_id());
            msg.setMessage_type(Message.MessageType.TEXT);

            // عرض الرسالة في الشاشة (مؤقتًا)
            Label msgLabel = new Label("Me: " + text);
            messagesContainer.getChildren().add(msgLabel);

            messageTextField.clear();

            clientSocketManager.sendRequest(new Request(Action.SEND_MESSAGE, null, Session.currentUser, msg));
        }
    }

    void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
        // Optionally, switch to another view or update UI
    }
}