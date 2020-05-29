package com.reconciliationhouse.android.loverekindle.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.reconciliationhouse.android.loverekindle.models.UserModel;

public class UserRepo {
    public static final String collectionPath = "User";
    private static final String TAG = UserRepo.class.getSimpleName();
    private static UserRepo ourInstance;
    private UserModel user;
    private String userId;
    private FirebaseFirestore db;
    private CollectionReference mCollectionRef;

    private UserRepo(String userId) {
        this.userId = userId;
        this.db = FirebaseFirestore.getInstance();
        mCollectionRef = db.collection(collectionPath);
    }

    public static UserRepo getInstance() {
        // TODO: get the user Id from the UserAuth which may probably be merged with this class later
        if (ourInstance == null) ourInstance = new UserRepo("DemoUser");
        return ourInstance;
    }

    public String getUserId() {
        return userId;
    }
}
