package com.reconciliationhouse.android.loverekindle.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.reconciliationhouse.android.loverekindle.models.UserModel;

public class UserPreferences {
    public static boolean saveUserName(String name, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(UserConstants.USER_NAME, name);
        prefsEditor.apply();
        return true;
    }

    public static String getUsername(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(UserConstants.USER_NAME, null);
    }

    public static boolean saveEmail(String email, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(UserConstants.USER_EMAIL, email);
        prefsEditor.apply();
        return true;
    }

    public static String getEmail(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(UserConstants.USER_EMAIL, null);
    }
    public static boolean saveId(String id, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(UserConstants.USER_ID, id);
        prefsEditor.apply();
        return true;
    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(UserConstants.USER_ID, null);
    }


    //   role such as counsellor , regular and super
    public static boolean saveRole(UserModel.Role role, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(UserConstants.USER_ROLE, role.toString());
        prefsEditor.apply();
        return true;
    }

    public static String getRole(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(UserConstants.USER_ROLE, null);
    }

    // category is only apply to a counsellor with the four categories
    public static boolean saveCategory(UserModel.Category category, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(UserConstants.USER_CATEGORY, category.toString());
        prefsEditor.apply();
        return true;
    }

    public static String getCategory(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(UserConstants.USER_CATEGORY, null);
    }
    public static boolean saveBalance(String balance, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(UserConstants.USER_BALANCE, balance);
        prefsEditor.apply();
        return true;
    }

    public static String getBalance(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(UserConstants.USER_BALANCE, null);
    }
}
