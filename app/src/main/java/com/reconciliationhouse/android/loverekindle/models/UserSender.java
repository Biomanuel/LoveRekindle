package com.reconciliationhouse.android.loverekindle.models;

public class UserSender {


    private final String userId,name,profileImageUrl;

    public UserSender(String userId, String name, String profileImageUrl) {
        this.userId = userId;
        this.name = name;

        this.profileImageUrl=profileImageUrl;

    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
