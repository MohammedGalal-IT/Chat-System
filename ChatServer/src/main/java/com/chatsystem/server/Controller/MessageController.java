package com.chatsystem.server.Controller;

import com.chatsystem.server.Model.Message;
import com.chatsystem.server.Model.User;
import com.chatsystem.server.Model.Message.MessageType;
import com.chatsystem.server.network.Action;
import com.chatsystem.server.network.Request;
import com.chatsystem.server.network.Response;
import com.chatsystem.server.services.MessageService;
import java.util.List;

public class MessageController {
    private final MessageService messageService;

    public MessageController() {
        this.messageService = new MessageService();
    }

    public Response executeRequest(Request request) {
        switch (request.getAction()) {
            case SEND_MESSAGE:
                return sendMessage(request);
            case SEND_MESSAGE_WITH_ATTACHMENT:
                return sendMessageWithAttachment(request);
            case GET_MESSAGES_BETWEEN_USERS:
                return getMessagesBetweenUsers(request);
            case GET_UNREAD_MESSAGES:
                return getUnreadMessages(request);
            case MARK_MESSAGES_AS_READ:
                return markMessageAsRead(request);
            case GET_MESSAGES_BY_TYPE:
                return getMessagesByType(request);
            case DELETE_MESSAGE:
                return deleteMessage(request);
            default:
                return new Response(false, "Invalid Action");
        }
    }

    // Send a message
    private Response sendMessage(Request request) {
        if(request.getMessage().getMessage_type() != Message.MessageType.TEXT){
            return new Response(false, "Message type is not text");
            }
        Message message = request.getMessage();
        Response response = new Response();
        
        message = messageService.sendMessage(message);
        if (message != null) {
            response.setSuccess(true);
            response.setMessage("Message sent");
             response.setMessageObj(message);
            return response;
        } else {
            return new Response(false, "Failed to send message");
        }
    }

    // Send a message with attachment
    private Response sendMessageWithAttachment(Request request) {
        if(request.getMessage().getMessage_type() == Message.MessageType.TEXT){
            return new Response(false, "Message type is not file");
            }
        Message message = request.getMessage();
        Response response = new Response();
        response.setMessageObj(message);
        message = messageService.sendMessage(message);
        if (message != null) {
            response.setSuccess(true);
            response.setMessage("Message sent");
            response.setMessageObj(message);
            return response;
        } else {
            return new Response(false, "Failed to send message");
        }
    }

    // Get messages between two users
    private Response getMessagesBetweenUsers(Request request) {
        int userId1 = ((User) request.getData()[0]).getUserId();
        int userId2 = ((User) request.getData()[1]).getUserId();
        List<Message> messages = messageService.getMessagesBetweenUsers(userId1, userId2);
        if (messages != null && !messages.isEmpty()) {
            Response response = new Response(true, "Messages found");
            response.setMessages(messages);
            return response;
        } else {
            return new Response(false, "No messages found");
        }
    }

    // Get unread messages for a user
    private Response getUnreadMessages(Request request) {
        int userId = ((User) request.getData()[0]).getUserId();
        List<Message> messages = messageService.getUnreadMessages(userId);
        if (messages != null && !messages.isEmpty()) {
            Response response = new Response(true, "Unread messages found");
            response.setMessages(messages);
            return response;
        } else {
            return new Response(false, "No unread messages");
        }
    }

    // Mark messages as read
    private Response markMessageAsRead(Request request) {
        int messageId = request.getMessage().getMessage_id();
        boolean success = messageService.markAsRead(messageId);
        if (success) {
            return new Response(true, "Messages marked as read");
        } else {
            return new Response(false, "Failed to mark messages as read");
        }
    }

    // Get messages by type
    private Response getMessagesByType(Request request) {
        int userId = ((User) request.getData()[0]).getUserId();
        MessageType type  = ((Message) request.getData()[1]).getMessage_type();
        List<Message> messages = messageService.getMessagesByType(userId, type);
        if (messages != null && !messages.isEmpty()) {
            Response response = new Response(true, "Messages found");
            response.setMessages(messages);
            return response;
        } else {
            return new Response(false, "No messages found for type");
        }
    }

    // Delete a message
    private Response deleteMessage(Request request) {
        if(request.getData() == null || request.getData().length == 0 || !(request.getData()[0] instanceof Message)){
            return new Response(false, "Invalid request data");
        }
        int messageId = ((Message) request.getData()[0]).getMessage_id();
        boolean success = messageService.deleteMessage(messageId);
        if (success) {
            return new Response(true, "Message deleted");
        } else {
            return new Response(false, "Failed to delete message");
        }
    }
}
