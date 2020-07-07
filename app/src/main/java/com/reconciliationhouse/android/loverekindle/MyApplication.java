package com.reconciliationhouse.android.loverekindle;

import android.app.Application;

import com.reconciliationhouse.android.loverekindle.repository.UserRepo;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UserRepo.initInstance();

    }
}
