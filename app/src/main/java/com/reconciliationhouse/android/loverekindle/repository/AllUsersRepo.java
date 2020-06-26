package com.reconciliationhouse.android.loverekindle.repository;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.models.UserModel;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AllUsersRepo {
    public static AllUsersRepo allUsersRepo;

    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    public AllUsersRepo() {
        db=FirebaseFirestore.getInstance();
    }
    public static AllUsersRepo getInstance() {
        if (allUsersRepo == null) allUsersRepo = new AllUsersRepo();
        return allUsersRepo;
    }
    public MutableLiveData<List<UserModel>> getAllRegularUsers(final ProgressBar progressBar, final TextView textView) {
        progressBar.setVisibility(View.VISIBLE);
        final List<UserModel> models=new ArrayList<>();
        final MutableLiveData<List<UserModel>> listMutableLiveData = new MutableLiveData<>((List<UserModel>) new ArrayList<UserModel>());
        // Prepare Query as you like.
        collectionReference=db.collection("User");
        Query query = collectionReference
                .whereEqualTo("role", UserModel.Role.Regular);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                UserModel model = document.toObject(UserModel.class);
                                models.add(model);


                            }
                            listMutableLiveData.setValue(models);
                            progressBar.setVisibility(View.GONE);
                            if (models.size()==0){
                                textView.setText(R.string.no_counsellor);
                            }
                            else {
                                textView.setText("Select a Counsellor");
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });




        return  listMutableLiveData;
    }
}
