package com.reconciliationhouse.android.loverekindle.models;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.auth.User;
import com.reconciliationhouse.android.loverekindle.models.UserModel;

import java.util.Date;

public class Message {
    private String message;
    private Date dateCreated;
    private UserSender userSender;
    private MessageType messageType;
    private String imageUrl;
    private Boolean isSeen;



    public   static enum MessageType{
        TEXT,IMAGE,Audio
    }


    public Message() {
    }



    public Message(MessageType type ,String message, UserSender userSender,Boolean isSeen) {
        this.message = message;
        this.messageType=type;
        this.userSender = userSender;
        this.isSeen=isSeen;
    }

    public Message(MessageType messageType,UserSender userSender, String imageUrl,Boolean isSeen) {
        this.userSender = userSender;
        this.imageUrl = imageUrl;
        this.messageType=messageType;
        this.isSeen=isSeen;
    }

    // --- GETTERS ---
    public String getMessage() {
        return message;
    }

    @ServerTimestamp
    public Date getDateCreated() {
        return dateCreated;
    }

    public UserSender getUserSender() {
        return userSender;
    }

    public void setUserSender(UserSender userSender) {
        this.userSender = userSender;
    }

    // --- SETTERS ---
    public void setMessage(String message) {
        this.message = message;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getSeen() {
        return isSeen;
    }

    public void setSeen(Boolean seen) {
        isSeen = seen;
    }
}
