package com.reconciliationhouse.android.loverekindle.models;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserModel {
    private String category;
    private String userId;
    private String name;
    private String email;
    private String profileImageUrl;
    private String balance;
    private String role;

    public UserModel() {
    }

    public UserModel(String userId) {
        this.userId = userId;
    }

    public UserModel(String userId, String name, String category, String profileImageUrl) {
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.profileImageUrl = profileImageUrl;
    }

    public UserModel(String userId, String name, String profileImageUrl) {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public UserModel(String userId, String name, String profileImageUrl, String email, String balance, String role, String category) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.balance = balance;
        this.role = role;
        this.category = category;
    }

    public UserModel(String userId, String name, String profileImageUrl, String email, String balance, String role) {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
        this.balance = balance;
        this.role = role;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
