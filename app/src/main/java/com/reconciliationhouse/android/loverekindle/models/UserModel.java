package com.reconciliationhouse.android.loverekindle.models;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class UserModel {

    private String userId;
    private String name;
    private String email;
    private String profileImageUrl;
    private String balance;
    private List<String> saveMedia;
    private List<String> likedMedia;
    private Role role;
    private Category category;

    public enum Role {
        Regular, Counsellor, Admin
    }

    public enum Category {
        Spiritual_Growth, Godly_Parenting, Marriage_and_Relationship, Health
    }

    public UserModel() {

    }

    public UserModel(String userId, String name, String email, String profileImageUrl, String balance, List<String> saveMedia, List<String> likedMedia, Role role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.balance = balance;
        this.saveMedia = saveMedia;
        this.likedMedia = likedMedia;
        this.role = role;
    }

    public UserModel(String userId, String name, String email, String profileImageUrl, String balance, List<String> saveMedia, List<String> likedMedia, Role role, Category category) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.balance = balance;
        this.saveMedia = saveMedia;
        this.likedMedia = likedMedia;
        this.role = role;
        this.category = category;
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

    public List<String> getSaveMedia() {
        return saveMedia;
    }

    public void setSaveMedia(List<String> saveMedia) {
        this.saveMedia = saveMedia;
    }

    public List<String> getLikedMedia() {
        return likedMedia;
    }

    public void setLikedMedia(List<String> likedMedia) {
        this.likedMedia = likedMedia;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
