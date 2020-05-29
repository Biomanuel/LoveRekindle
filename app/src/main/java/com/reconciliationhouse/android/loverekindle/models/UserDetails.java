package com.reconciliationhouse.android.loverekindle.models;

public class UserDetails {
    public static String name, email ,imageUrl,category,balance;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        UserDetails.name = name;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserDetails.email = email;
    }

    public static String getImageUrl() {
        return imageUrl;
    }

    public static void setImageUrl(String imageUrl) {
        UserDetails.imageUrl = imageUrl;
    }

    public static String getCategory() {
        return category;
    }

    public static void setCategory(String category) {
        UserDetails.category = category;
    }

    public static String getBalance() {
        return balance;
    }

    public static void setBalance(String balance) {
        UserDetails.balance = balance;
    }
}
