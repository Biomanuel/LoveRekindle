package com.reconciliationhouse.android.loverekindle.repository;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.reconciliationhouse.android.loverekindle.MainActivity;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.preferences.UserPreferences;
import com.reconciliationhouse.android.loverekindle.utils.ProgressBarHandler;

public class UserRepo {
    public static final String collectionPath = "User";
    private static final String TAG = UserRepo.class.getSimpleName();
    private static UserRepo ourInstance;
    private UserModel user;
    private String userId;
    private FirebaseFirestore db;
    private CollectionReference mCollectionRef;

    private UserRepo() {

    }

    public static void initInstance() {
        ourInstance = new UserRepo();
    }

    public static void initializeWithUser(Activity startActivity) {
        String userId = UserPreferences.getUserId(startActivity);
        ourInstance.initialize(userId, startActivity);
    }

    public static UserRepo getInstance() {
        // TODO: get the user Id from the UserAuth which may probably be merged with this class later
        if (ourInstance == null) throw new NullPointerException();
        return ourInstance;
    }

    private void initialize(String userId, final Activity activity) {
        this.userId = userId;
        this.db = FirebaseFirestore.getInstance();
        mCollectionRef = db.collection(collectionPath);
        mCollectionRef.document("user2@gmail.com").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(activity, "User not found", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "getUser().onComplete: ", task.getException());
//                    Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.loginFragment);
                } else {
                    user = task.getResult().toObject(UserModel.class);
                }
            }
        });
    }

    public String getUserId() {
        return userId;
    }

    public UserModel getUser() {
        return user;
    }
}
