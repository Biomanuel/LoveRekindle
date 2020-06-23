package com.reconciliationhouse.android.loverekindle.models;

public class ChatType  {
    private String userId;
    private String name;
    private UserModel.Category category;
    private String profileImageUrl;
    private ChatItem.ChatType chatType;


    //for single chat
    public ChatType(String userId, String name, UserModel.Category category, String profileImageUrl, ChatItem.ChatType chatType) {
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.profileImageUrl = profileImageUrl;
        this.chatType = chatType;
    }

    //for groupChat
    public ChatType( String groupName, String chatImageUrl, ChatItem.ChatType chatType) {

        this.name = groupName;

        this.profileImageUrl = profileImageUrl;
        this.chatType = chatType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserModel.Category getCategory() {
        return category;
    }

    public void setCategory(UserModel.Category category) {
        this.category = category;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public ChatItem.ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatItem.ChatType chatType) {
        this.chatType = chatType;
    }
}
