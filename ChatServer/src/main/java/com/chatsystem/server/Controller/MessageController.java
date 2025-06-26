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
                return new Response(false, "Invalid Action", request.getAction());
        }
    }

    // Send a message
    private Response sendMessage(Request request) {
        try{
            if(request.getMessage().getMessage_type() != Message.MessageType.TEXT){
                return new Response(false, "Message type is not text", Action.SEND_MESSAGE);
            }
            Message message = request.getMessage();
        
            message = messageService.sendMessage(message);
            if (message != null) {
                Response response = new Response(true, "Message sent", Action.SEND_MESSAGE);
                response.setMessageObj(message);
                return response;
            } else {
                System.out.println("No Returned Message");
                return new Response(false, "Failed to send message", Action.SEND_MESSAGE);
            }
        } catch (Exception e){
            System.out.println("Exception at sendMessage :" + e.getMessage());
            return new Response(false, "Exception: Failed to send message ", Action.SEND_MESSAGE);
        }

    }

    // Send a message with attachment
    private Response sendMessageWithAttachment(Request request) {
        if(request.getMessage().getMessage_type() == Message.MessageType.TEXT){
            return new Response(false, "Message type is not file", Action.SEND_MESSAGE_WITH_ATTACHMENT);
            }
        Message message = request.getMessage();
        message = messageService.sendMessage(message);
        if (message != null) {
            Response response = new Response(true, "Message with Attachment sent", Action.SEND_MESSAGE);
            response.setMessageObj(message);;
            return response;
        } else {
            return new Response(false, "Failed to send message", Action.SEND_MESSAGE_WITH_ATTACHMENT);
        }
    }

    // Get messages between two users
    private Response getMessagesBetweenUsers(Request request) {
        if(request.getData() == null){
            return new Response(false, "No Data Sent", Action.GET_MESSAGES_BETWEEN_USERS);
        }
        try{
            int userId1 = Integer.parseInt((String) request.getData()[0]);
            int userId2 = Integer.parseInt((String) request.getData()[1]);
            List<Message> messages = messageService.getMessagesBetweenUsers(userId1, userId2);
            if (messages != null && !messages.isEmpty()) {
                for (Message message : messages) {
                    if(request.getUser().getUser_id() == message.getReceiver_id() && !message.is_read){
                        message.setIs_read(true);
                        messageService.updateMessage(message);
                    }
                }
                Response response = new Response(true, "Messages found", Action.GET_MESSAGES_BETWEEN_USERS);
                response.setMessages(messages);
                
                return response;
            } else {
                return new Response(true, "No messages found", Action.GET_MESSAGES_BETWEEN_USERS);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return new Response(false, "Exception: " + e.getMessage(), Action.GET_MESSAGES_BETWEEN_USERS);
        }

    }

    // Get unread messages for a user
    private Response getUnreadMessages(Request request) {
        int userId = ((User) request.getData()[0]).getUser_id();
        List<Message> messages = messageService.getUnreadMessages(userId);
        if (messages != null && !messages.isEmpty()) {
            Response response = new Response(true, "Unread messages found", Action.GET_UNREAD_MESSAGES);
            response.setMessages(messages);
            response.setAction(request.getAction());
            return response;
        } else {
            return new Response(false, "No unread messages", Action.GET_UNREAD_MESSAGES);
        }
    }

    // Mark messages as read
    private Response markMessageAsRead(Request request) {
        int messageId = request.getMessage().getMessage_id();
        boolean success = messageService.markAsRead(messageId);
        if (success) {
            return new Response(true, "Messages marked as read", request.getAction());
        } else {
            return new Response(false, "Failed to mark messages as read", request.getAction());
        }
    }

    // Get messages by type
    private Response getMessagesByType(Request request) {
        int userId = ((User) request.getData()[0]).getUser_id();
        MessageType type  = ((Message) request.getData()[1]).getMessage_type();
        List<Message> messages = messageService.getMessagesByType(userId, type);
        if (messages != null && !messages.isEmpty()) {
            Response response = new Response(true, "Messages found", Action.GET_MESSAGES_BY_TYPE);
            response.setMessages(messages);
            response.setAction(request.getAction());
            return response;
        } else {
            return new Response(false, "No messages found for type", request.getAction());
        }
    }

    // Delete a message
    private Response deleteMessage(Request request) {
        if(request.getData() == null || request.getData().length == 0 || !(request.getData()[0] instanceof Message)){
            return new Response(false, "Invalid request data", Action.DELETE_MESSAGE);
        }
        int messageId = ((Message) request.getData()[0]).getMessage_id();
        boolean success = messageService.deleteMessage(messageId);
        if (success) {
            return new Response(true, "Message deleted", request.getAction());
        } else {
            return new Response(false, "Failed to delete message", request.getAction());
        }
    }
}
