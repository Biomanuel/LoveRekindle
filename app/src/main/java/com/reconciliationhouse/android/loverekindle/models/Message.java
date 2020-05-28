package com.reconciliationhouse.android.loverekindle.models;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.auth.User;
import com.reconciliationhouse.android.loverekindle.models.UserModel;

import java.util.Date;

public class Message {
    private String message;
    private Date dateCreated;
    private UserSender userSender;


    public Message() {
    }

    public Message(String message, UserSender userSender) {
        this.message = message;

        this.userSender = userSender;
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


}
