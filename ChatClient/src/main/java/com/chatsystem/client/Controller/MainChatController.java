package com.chatsystem.client.Controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.nio.file.Path; 
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chatsystem.client.Model.Message;
import com.chatsystem.client.Model.User;
import com.chatsystem.client.network.Action;
import com.chatsystem.client.network.ClientSocketManager;
import com.chatsystem.client.network.Request;
import com.chatsystem.client.util.Session;
import com.chatsystem.client.util.viewUtil.ReceivedMessage;
import com.chatsystem.client.util.viewUtil.SendMessage;
import com.chatsystem.client.util.viewUtil.MessageView;

public class MainChatController {

    @FXML private ListView<User> contactListView;
    @FXML private Label chatTitleLabel;
    @FXML private ImageView chatIconView;
    @FXML private ScrollPane chatScrollPane;
    @FXML private VBox messagesContainer;
    @FXML private TextField messageTextField;
    private User selectedUser;
    private ClientSocketManager clientSocketManager;


    private Map<String, User> userMap = new HashMap<>();


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
                            setUserMap(response.getUsers());
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

        // Initialize the chat scroll pane
        intiChatScrollPane();

        
        // Add action for Enter key in messageTextField
        messageTextField.setOnAction(event -> onSendMessage());
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

        for (Message message : messages) {
            messagesContainer.getChildren().add(getMessageView(message));  
        }
        System.out.println("Messages loaded successfully.");
    }


    /**
     * Returns a MessageView based on the message sender.
     * If the message is sent by the current user, it returns a SendMessage view.
     * Otherwise, it returns a ReceivedMessage view.
     *
     * @param message The message to be displayed.
     * @return A MessageView instance representing the message.
     */

    public MessageView getMessageView(Message message) {
        LocalDateTime dateTime = message.getSentAt().toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm a");

        if(message.getSender_id() == Session.currentUser.getUser_id())
            {
                return new SendMessage(message.getContent(), getUserById(message.sender_id).getUsername(), dateTime.format(formatter));
            } else{
                return new ReceivedMessage(message.getContent(),getUserById(message.sender_id).getUsername(), dateTime.format(formatter));
            }
    }

    public void intiChatScrollPane(){
        // إعداد ScrollPane
        chatScrollPane.setPannable(true); // تمكين السحب بالماوس
        chatScrollPane.setCache(true); // تفعيل التخزين المؤقت

        // عند تغيير محتوى الـ ScrollPane
        messagesContainer.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            // تأخير التمرير حتى يتم تحديث المشهد
            Platform.runLater(() -> {
                chatScrollPane.setVvalue(1.0); // الانتقال إلى الأسفل
            });
        });

        // إضافة فلتر للتمرير
        // لتطبيق تأثير التمرير السلس
        chatScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
        double deltaY = event.getDeltaY() * 0.008;
        double newV = chatScrollPane.getVvalue() - deltaY;

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(chatScrollPane.vvalueProperty(), newV, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        event.consume();// منع التمرير الافتراضي
        });
    }

    private void loadMessage(Message message){
        User receiverUser = getUserById(message.getSender_id());
    if (message != null && receiverUser.getUser_id() == selectedUser.getUser_id()) {
        messagesContainer.getChildren().add(getMessageView(message));
        return;
    }
    System.out.println("Can not display the received message");
    }

    public void setSelectedUser(User user) {
        this.selectedUser = user;
        // load user-specific data if needed
    }

    @FXML
    private void onSendMessage() {
        String text = messageTextField.getText().trim();
        if (!text.isEmpty() && selectedUser != null) {
            Message msg = new Message();
            msg.setContent(text);
            msg.setSender_id(Session.currentUser.getUser_id());
            msg.setReceiver_id(selectedUser.getUser_id());
            msg.setMessage_type(Message.MessageType.TEXT);
            msg.setSentAt(Timestamp.valueOf(LocalDateTime.now()));

            messagesContainer.getChildren().add(getMessageView(msg));

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

    /**
     * Sets the userMap from a list of users. The key is user.getUser_id().
     */
    public void setUserMap(List<User> users) {
        userMap.clear();
        for (User user : users) {
            userMap.put(String.valueOf(user.getUser_id()), user);
        }
    }

    /**
     * Returns the User with the given userId from the userMap, or null if not found.
     */
    public User getUserById(int userId) {
        return userMap.get(String.valueOf(userId));
    }
}