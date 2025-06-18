package com.chatsystem.client.network;

public enum Action{
    LOGIN,
    REGISTER,
    LOGOUT,
    REFRESH,

    GET_USER_BY_ID,
    GET_USER_BY_EMAIL,
    UPDATE_USER,
    UPDATE_USER_PROFILE_PICTURE,
    CHANGE_PASSWORD,
    SEARCH_USERS,
    GET_USERS,
    GET_ONLINE_USERS,

    SEND_MESSAGE,
    RECEIVE_MESSAGE,
    SEND_MESSAGE_WITH_ATTACHMENT, //
    DELETE_MESSAGE,
    UPDATE_MESSAGE,
    GET_MESSAGES,
    GET_MESSAGES_BY_USER_ID,
    GET_MESSAGES_BETWEEN_USERS,
    GET_UNREAD_MESSAGES,
    MARK_MESSAGES_AS_READ,
    GET_MESSAGES_BY_TYPE;

     public static Action fromString(String text) {
        for (Action action : Action.values()) {
            if (action.name().equalsIgnoreCase(text)) {
                return action;
            }
        }
        return null;
    }
    
}
