package com.reconciliationhouse.android.loverekindle.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.reconciliationhouse.android.loverekindle.livedata.MediaItemListLiveData;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MediaRepo {

    private static final String TAG = MediaRepo.class.getSimpleName();
    private static MediaRepo ourInstance;
    ArrayList<MediaItem> mList;
    private FirebaseFirestore db;

    private MediaRepo() {
        this.db = FirebaseFirestore.getInstance();
    }

    public static MediaRepo getInstance() {
        if (ourInstance == null) ourInstance = new MediaRepo();
        return ourInstance;
    }

    //this is a function to get All media
    public MutableLiveData<List<MediaItem>> getAllMedia(final Context context) {

        //make sure there is internet connection
        final MutableLiveData<List<MediaItem>> mediaListLiveData = new MutableLiveData<>((List<MediaItem>) new ArrayList<MediaItem>());

        Task audiosQuery = db.collection("Audio").orderBy("timestamp", Query.Direction.ASCENDING).get();
        Task ebooksQuery = db.collection("Ebooks").orderBy("timestamp", Query.Direction.ASCENDING).get();
        Task<List<QuerySnapshot>> allMediaTask = Tasks.whenAllSuccess(audiosQuery, ebooksQuery);

        allMediaTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                for (QuerySnapshot querySnapshot : querySnapshots) {
                    for (DocumentSnapshot document : querySnapshot) {
                        List<MediaItem> current = mediaListLiveData.getValue();
                        MediaItem mediaItem = document.toObject(MediaItem.class);
                        current.add(mediaItem);
                        assert mediaItem != null;
                        mediaListLiveData.postValue(current);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error reading all Media", Toast.LENGTH_LONG).show();
                Log.e(TAG, "onFailure: Read All Media Error", e);
            }
        });

        return mediaListLiveData;
    }


    public MediaItemListLiveData getFireStoreEbooksLiveData() {
        // Prepare Query as you like.
        Query query = db.collection("Ebooks").orderBy("timestamp", Query.Direction.ASCENDING);

        return new MediaItemListLiveData(query);
    }

    public MediaItemListLiveData getFireStoreAudiosLiveData() {
        Query query = db.collection("Audio").orderBy("timestamp", Query.Direction.ASCENDING);

        return new MediaItemListLiveData(query);
    }

    /**
     * Gets all media of the supplied category.
     *
     * @param category  the category to query
     * @param mediaType the media group to query from
     * @return LiveData of the list
     */
    public MediaItemListLiveData getMediaListOfCategory(String category, MediaItem.MediaType mediaType) {
        //make sure there is internet connection
        Query query;
        if (mediaType == MediaItem.MediaType.EBOOK)
            query = db.collection("Ebooks").whereEqualTo("category", category).orderBy("timestamp", Query.Direction.ASCENDING);
        else
            query = db.collection("Audio").whereEqualTo("category", category).orderBy("timestamp", Query.Direction.ASCENDING);

        return new MediaItemListLiveData(query);
    }
}
