package com.reconciliationhouse.android.loverekindle.repo;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.reconciliationhouse.android.loverekindle.models.MedialModel;

import java.util.ArrayList;
import java.util.Objects;

public class MediaRepo {
    MedialModel mMedialModel;
    ArrayList<MedialModel> mList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //this is a function to get All media
    public ArrayList<MedialModel> getAllAudio(final Context context) {

        //make sure there is internet connection
        String audio = "audio";
        mList = new ArrayList<>();
        Query query = db.collection("media").whereEqualTo("mediaType", audio).orderBy("timestamp", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        mMedialModel = document.toObject(MedialModel.class);
                        mList.add(mMedialModel);
                        assert mMedialModel != null;

                    }
                } else {
                    Toast.makeText(context, "Error reading all Audio", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return mList;
    }

    //this is a function to get All Ebooks 
    public ArrayList<MedialModel> getEbookMedia(final Context context) {

        //make sure there is internet connection
        String ebook = "ebook";
        mList = new ArrayList<>();
        Query query = db.collection("media").whereEqualTo("mediaType", ebook).orderBy("timestamp", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        mMedialModel = document.toObject(MedialModel.class);
                        mList.add(mMedialModel);
                        assert mMedialModel != null;

                    }
                } else {
                    Toast.makeText(context, "Error reading all Ebook", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return mList;
    }
    
    //this is a function to get All spiritual media 
    public ArrayList<MedialModel>getSpiritualMedia(final Context context){
        //make sure there is internet connection
        String category="spiritual";
        mList=new ArrayList<>();
        Query query = db.collection("media").whereEqualTo("category",category ).orderBy("timestamp", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                        mMedialModel = document.toObject(MedialModel.class);
                        mList.add(mMedialModel);
                        assert mMedialModel != null;

                    }
                }
                else {
                    Toast.makeText(context,"Error reading all spiritual category",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return mList;
    }
    //this is a function to get All Godly parenting media 
    public ArrayList<MedialModel>getParentingMedia(final Context context){

        //make sure there is internet connection
        String category="parenting";
        mList=new ArrayList<>();
        Query query = db.collection("media").whereEqualTo("category",category ).orderBy("timestamp", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                        mMedialModel = document.toObject(MedialModel.class);
                        mList.add(mMedialModel);
                        assert mMedialModel != null;

                    }
                }
                else {
                    Toast.makeText(context,"Error reading all Godly category",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return mList;
    }
    //this is a function to get All marriage and relationship media 
    public ArrayList<MedialModel>getRelationshipMedia(final Context context){
        //make sure there is internet connection
        String category="relationship";
        mList=new ArrayList<>();
        Query query = db.collection("media").whereEqualTo("category",category ).orderBy("timestamp", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                        mMedialModel = document.toObject(MedialModel.class);
                        mList.add(mMedialModel);
                        assert mMedialModel != null;

                    }
                }
                else {
                    Toast.makeText(context,"Error reading all relaionship category",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return mList;
    }
    
}
