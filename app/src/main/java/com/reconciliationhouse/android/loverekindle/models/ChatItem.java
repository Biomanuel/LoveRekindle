package com.reconciliationhouse.android.loverekindle.models;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;


import java.util.Date;
@IgnoreExtraProperties
public class ChatItem {
    private String chatId;
    public ChatType chatType;
    String name;
    String imageUrl;
    Date dateCreated;


    public ChatItem() {
    }

    public ChatItem(String chatId, ChatType chatType, String name, String imageUrl) {
        this.chatId = chatId;
        this.chatType = chatType;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public ChatItem(String chatId, ChatType chatType, String imageUrl) {
        this.chatId = chatId;
        this.chatType = chatType;

        this.imageUrl = imageUrl;
    }



    public enum ChatType{
        Single_Chat,Group_Chat
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    @ServerTimestamp
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
