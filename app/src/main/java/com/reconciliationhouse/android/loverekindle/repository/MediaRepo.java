package com.reconciliationhouse.android.loverekindle.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.reconciliationhouse.android.loverekindle.livedata.MediaItemListLiveData;
import com.reconciliationhouse.android.loverekindle.livedata.MediaItemLiveData;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;

import java.util.Arrays;

public class MediaRepo {

    private static final String TAG = MediaRepo.class.getSimpleName();
    public static final String collectionPath = "Media";
    private static MediaRepo ourInstance;
    private FirebaseFirestore db;
    private CollectionReference mCollectionRef;

    private MediaRepo() {

        this.db = FirebaseFirestore.getInstance();
        mCollectionRef = db.collection(collectionPath);
    }

    public static MediaRepo getInstance() {
        if (ourInstance == null) ourInstance = new MediaRepo();
        return ourInstance;
    }

    public MediaItemListLiveData getFireStoreAllMediaLiveData() {
        // Prepare Query as you like.
        Query query = mCollectionRef
                .whereIn("type", Arrays.asList(MediaItem.MediaType.values()))
                .orderBy("timestamp", Query.Direction.ASCENDING);

        return new MediaItemListLiveData(query);
    }

    public MediaItemListLiveData getFireStoreEbooksLiveData() {
        // Prepare Query as you like.
        Query query = mCollectionRef
                .whereEqualTo("type", MediaItem.MediaType.EBOOK)
                .orderBy("timestamp", Query.Direction.ASCENDING);

        return new MediaItemListLiveData(query);
    }

    public MediaItemListLiveData getFireStoreAudiosLiveData() {
        Query query = mCollectionRef
                .whereIn("type", Arrays.asList(MediaItem.MediaType.AUDIO, MediaItem.MediaType.SERMON))
                .orderBy("timestamp", Query.Direction.ASCENDING);

        return new MediaItemListLiveData(query);
    }

    public MediaItemLiveData getMediaLiveData(String mediaId) {
        DocumentReference mediaRef = mCollectionRef.document(mediaId);

        return new MediaItemLiveData(mediaRef);
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
        Query query = mCollectionRef
                .whereEqualTo("type", mediaType.toString())
                .whereEqualTo("category", category)
                .orderBy("timestamp", Query.Direction.ASCENDING);

        return new MediaItemListLiveData(query);
    }
}
