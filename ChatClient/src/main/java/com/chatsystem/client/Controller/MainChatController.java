package com.chatsystem.client.Controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.URI;
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
import com.chatsystem.client.util.viewUtil.ReceivedVideoMessage;
import com.chatsystem.client.util.viewUtil.SendAudioMessage;
import com.chatsystem.client.util.viewUtil.SendDocumentMessage;
import com.chatsystem.client.util.viewUtil.SendImageMessage;
import com.chatsystem.client.util.viewUtil.SendMessage;
import com.chatsystem.client.util.viewUtil.SendVideoMessage;
import com.chatsystem.client.util.viewUtil.SmoothishScrollpaneUtil;
import com.chatsystem.client.util.viewUtil.VideoMessageView;
import com.chatsystem.client.util.viewUtil.AudioMessageView;
import com.chatsystem.client.util.viewUtil.DocumentMessageView;
import com.chatsystem.client.util.viewUtil.ImageMessageView;
import com.chatsystem.client.util.viewUtil.MessageView;
import com.chatsystem.client.util.viewUtil.ReceivedAudioMessage;
import com.chatsystem.client.util.viewUtil.ReceivedDocumentMessage;
import com.chatsystem.client.util.viewUtil.ReceivedImageMessage;
import com.chatsystem.client.util.collectionsUtil.Pair;

public class MainChatController {

    @FXML
    private ListView<User> contactListView;
    @FXML
    private Label chatTitleLabel;
    @FXML
    private ImageView chatIconView;
    @FXML
    private ScrollPane chatScrollPane;
    @FXML
    private VBox messagesContainer;
    @FXML
    private TextField messageTextField;
    private User selectedUser;
    private ClientSocketManager clientSocketManager;

    private Map<String, User> userMap = new HashMap<>();

    @FXML
    public void initialize() {
        clientSocketManager = ClientSocketManager.getInstance();
        clientSocketManager.setOnResponse((response) -> {
            if (response != null)
                System.out.println("Response Received");

            switch (response.getAction()) {

                case Action.GET_ONLINE_USERS:
                    if (response.isSuccess()) {
                        Platform.runLater(() -> {
                            if (response.getUsers() != null) {
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
                    Platform.runLater(() -> messageLoader(response.getMessageObj()));
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
                    setGraphic(null);
                    setStyle("");
                } else {
                    javafx.scene.layout.HBox wrapper = new javafx.scene.layout.HBox(10); // spacing between icon and
                                                                                         // name
                    // Load user icon
                    ImageView iconView = null;
                    try {
                        Image icon = new Image(
                                getClass().getResource("/com/chatsystem/client/Assets/userIcon.png").toExternalForm(),
                                25, 25, true, true);
                        iconView = new ImageView(icon);
                        // Make image circular using a clip
                        javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(12, 12, 12);
                        iconView.setClip(clip);
                        iconView.setSmooth(true);
                    } catch (Exception e) {
                        iconView = new ImageView(); // fallback empty
                    }
                    Label nameLabel = new Label(user.getUsername());
                    nameLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #8a034b;");
                    wrapper.getChildren().addAll(iconView, nameLabel);
                    wrapper.setStyle(
                            "-fx-background-color: #f0f0f0ff; -fx-background-radius: 16; -fx-padding: 8 16 8 16;");
                    javafx.geometry.Insets margin = new javafx.geometry.Insets(15, 0, 15, 0);
                    javafx.scene.layout.HBox.setMargin(wrapper, margin);
                    setGraphic(wrapper);
                    setText(null);
                    setStyle("");
                }
            }

            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                if (selected && getGraphic() != null) {
                    getGraphic().setStyle(
                            "-fx-background-color: #995f7e; -fx-background-radius: 16; -fx-padding: 8 16 8 16;");
                    ((Label) ((javafx.scene.layout.HBox) getGraphic()).getChildren().get(1))
                            .setStyle("-fx-font-size: 14; -fx-text-fill: white; font-weight: bold;");
                } else if (getGraphic() != null) {
                    getGraphic().setStyle(
                            "-fx-background-color: #8a034b; -fx-background-radius: 16; -fx-padding: 8 16 8 16;");
                    ((Label) ((javafx.scene.layout.HBox) getGraphic()).getChildren().get(1))
                            .setStyle("-fx-font-size: 14; -fx-text-fill: white; font-weight: normal;");
                }
            }

        });

        // Add action when an item is selected
        contactListView.getSelectionModel().selectedItemProperty().addListener((observable, oldUser, newUser) -> {
            if (newUser != null && Session.currentUser != null) {
                Request request2 = new Request();
                request2.setAction(Action.GET_MESSAGES_BETWEEN_USERS);
                request2.setUser(Session.currentUser);
                request2.setData(new Object[] { Session.currentUser.getUser_id() + "", newUser.getUser_id() + "" });
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

    private void requestOnlineUsers() {
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

        if (message.getSender_id() == Session.currentUser.getUser_id()) {
            return new SendMessage(message.getContent(), getUserById(message.sender_id).getUsername(),
                    dateTime.format(formatter));
        } else {
            return new ReceivedMessage(message.getContent(), getUserById(message.sender_id).getUsername(),
                    dateTime.format(formatter));
        }
    }

    public ImageMessageView getImageMessageView(Message message) {
        LocalDateTime dateTime = message.getSentAt().toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm a");

        if (message.getSender_id() == Session.currentUser.getUser_id()) {
            return new SendImageMessage(message.getContent(), getUserById(message.sender_id).getUsername(),
                    dateTime.format(formatter), message.getFile_path_client());
        } else {
            return new ReceivedImageMessage(message.getContent(), getUserById(message.sender_id).getUsername(),
                    dateTime.format(formatter), message.getFile_path_client());
        }
    }

    public VideoMessageView getVideoMessageView(Message message) {
        LocalDateTime dateTime = message.getSentAt().toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm a");

        if (message.getSender_id() == Session.currentUser.getUser_id()) {
            return new SendVideoMessage(message.getContent(), getUserById(message.sender_id).getUsername(),
                    dateTime.format(formatter), message.getFile_path_client());
        } else {
            return new ReceivedVideoMessage(message.getContent(), getUserById(message.sender_id).getUsername(),
                    dateTime.format(formatter), message.getFile_path_client());
        }
    }

    public AudioMessageView getAudioMessageView(Message message) {
        LocalDateTime dateTime = message.getSentAt().toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm a");

        if (message.getSender_id() == Session.currentUser.getUser_id()) {
            return new SendAudioMessage(message.getContent(), getUserById(message.sender_id).getUsername(),
                    dateTime.format(formatter), message.getFile_path_client());
        } else {
            return new ReceivedAudioMessage(message.getContent(), getUserById(message.sender_id).getUsername(),
                    dateTime.format(formatter), message.getFile_path_client());
        }
    }

    public DocumentMessageView getDocumentMessageView(Message message) {
        LocalDateTime dateTime = message.getSentAt().toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm a");

        if (message.getSender_id() == Session.currentUser.getUser_id()) {
            return new SendDocumentMessage(message.getContent(), getUserById(message.sender_id).getUsername(),
                    dateTime.format(formatter), message.getFile_path_client());
        } else {
            return new ReceivedDocumentMessage(message.getContent(), getUserById(message.sender_id).getUsername(),
                    dateTime.format(formatter), message.getFile_path_client());
        }
    }

    public void intiChatScrollPane() {
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

        new SmoothishScrollpaneUtil().init(chatScrollPane, messagesContainer);
    }

    private void messageLoader(Message message) {
        User receiverUser = getUserById(message.getSender_id());
        if (message == null || receiverUser.getUser_id() != selectedUser.getUser_id()) {
            return;
        }
        switch (message.getMessage_type()) {
            case IMAGE:
                messagesContainer.getChildren().add(getImageMessageView(message));
                break;

            case VIDEO:
                messagesContainer.getChildren().add(getVideoMessageView(message));
                break;

            case AUDIO:
                messagesContainer.getChildren().add(getAudioMessageView(message));
                break;

            case FILE:
                messagesContainer.getChildren().add(getDocumentMessageView(message));
                break;

            default:
                messagesContainer.getChildren().add(getMessageView(message));
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

    /**
     * Detects the message type and file format based on the file name.
     * 
     * @param fileName the name of the file to analyze
     * @return a Pair containing the detected MessageType and FileFormat, or nulls
     *         if unsupported
     */
    private Pair<Message.MessageType, Message.FileFormat> detectMessageTypeAndFormat(String fileName) {
        fileName = fileName.toLowerCase();
        Message.MessageType type = null;
        Message.FileFormat format = null;
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
            type = Message.MessageType.IMAGE;
            format = fileName.endsWith(".png") ? Message.FileFormat.png : Message.FileFormat.jpg;
        } else if (fileName.endsWith(".mp4")) {
            type = Message.MessageType.VIDEO;
            format = Message.FileFormat.mp4;
        } else if (fileName.endsWith(".mp3")) {
            type = Message.MessageType.AUDIO;
            format = Message.FileFormat.mp3;
        } else if (fileName.endsWith(".pdf")) {
            type = Message.MessageType.FILE;
            format = Message.FileFormat.pdf;
        } else if (fileName.endsWith(".docx")) {
            type = Message.MessageType.FILE;
            format = Message.FileFormat.docx;
        } else if (fileName.endsWith(".xlsx")) {
            type = Message.MessageType.FILE;
            format = Message.FileFormat.xlsx;
        } else if (fileName.endsWith(".zip")) {
            type = Message.MessageType.FILE;
            format = Message.FileFormat.zip;
        } else if (fileName.endsWith(".rar")) {
            type = Message.MessageType.FILE;
            format = Message.FileFormat.rar;
        } else if (fileName.endsWith(".txt")) {
            type = Message.MessageType.FILE;
            format = Message.FileFormat.txt;
        }
        return new Pair<>(type, format);
    }

    @FXML
    private void onSendMessageFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Send");
        File file = fileChooser.showOpenDialog(messagesContainer.getScene().getWindow());
        if (file == null || selectedUser == null)
            return;

        Pair<Message.MessageType, Message.FileFormat> typeAndFormat = detectMessageTypeAndFormat(file.getName());
        if (typeAndFormat.first() == null || typeAndFormat.second() == null) {
            showError("Unsupported file type.");
            return;
        }

        Message msg = new Message();

        try {
            msg.setSender_id(Session.currentUser.getUser_id());
            msg.setReceiver_id(selectedUser.getUser_id());
            msg.setMessage_type(typeAndFormat.first());
            msg.setFile_format(typeAndFormat.second());
            msg.setFile_length(file.length());
            String text = messageTextField.getText().trim();
            if (!text.isEmpty() && selectedUser != null)
                msg.setContent(text);
            else
                msg.setContent("Sent a file: " + file.getName());
            msg.setFile_path_client(file.getAbsolutePath());
            msg.setSentAt(Timestamp.valueOf(LocalDateTime.now()));

        } catch (Exception e) {
            System.out.println("Error at onSendMessageFile method: " + e.getMessage());
        }

        switch (msg.getMessage_type()) {
            case IMAGE:
                messagesContainer.getChildren().add(getImageMessageView(msg));
                break;

            case VIDEO:
                messagesContainer.getChildren().add(getVideoMessageView(msg));
                break;

            case AUDIO:
                messagesContainer.getChildren().add(getAudioMessageView(msg));
                break;

            case FILE:  
                messagesContainer.getChildren().add(getDocumentMessageView(msg));
                break;

            default:
                messagesContainer.getChildren().add(getMessageView(msg));
                break;
        }

        messageTextField.clear();

        clientSocketManager
                .sendRequest(new Request(Action.SEND_MESSAGE_WITH_ATTACHMENT, null, Session.currentUser, msg));
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
     * Returns the User with the given userId from the userMap, or null if not
     * found.
     */
    public User getUserById(int userId) {
        return userMap.get(String.valueOf(userId));
    }
}