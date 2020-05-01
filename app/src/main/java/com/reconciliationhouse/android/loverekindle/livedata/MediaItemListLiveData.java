package com.reconciliationhouse.android.loverekindle.livedata;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;

import java.util.ArrayList;
import java.util.List;

public class MediaItemListLiveData extends LiveData<List<MediaItem>> implements EventListener<QuerySnapshot> {

    private static final String TAG = MediaItemListLiveData.class.getSimpleName();
    private boolean doubleRefs = false;
    private Query mQuery;
    private CollectionReference mCollectionReference;
    private CollectionReference mCollectionTwoReference;
    private List<MediaItem> tempMediaList = new ArrayList<>();
    private MutableLiveData<List<MediaItem>> mediaItemLiveData = new MutableLiveData<>();
    private ListenerRegistration listenerRegistration = null;

    public MediaItemListLiveData(CollectionReference collectionReference) {
        mCollectionReference = collectionReference;
    }

    public MediaItemListLiveData(Query query) {
        mQuery = query;
    }

    public MediaItemListLiveData(CollectionReference collectionOneReference, CollectionReference collectionTwoReference) {
        this.doubleRefs = true;
        mCollectionReference = collectionOneReference;
        mCollectionTwoReference = collectionTwoReference;
    }

    @Override
    protected void onActive() {
        if (mCollectionReference != null)
            listenerRegistration = mCollectionReference.addSnapshotListener(this);
        if (mQuery != null) listenerRegistration = mQuery.addSnapshotListener(this);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        listenerRegistration.remove();
        super.onInactive();
    }

    @Override
    protected void setValue(List<MediaItem> value) {
        super.setValue(value);
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "Media SnapshotListener:error", e);
            return;
        }

        if (queryDocumentSnapshots != null) {
            if (!doubleRefs) {
                tempMediaList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    MediaItem mediaItem = documentSnapshot.toObject(MediaItem.class);
                    tempMediaList.add(mediaItem);
                }
                mediaItemLiveData.setValue(tempMediaList);
                this.setValue(tempMediaList);
            } else {
                handleChangesManually(queryDocumentSnapshots);
            }
        }

    }

    private void handleChangesManually(@NonNull QuerySnapshot queryDocumentSnapshots) {
        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
            MediaItem mediaItem = doc.getDocument().toObject(MediaItem.class);
            switch (doc.getType()) {
                case ADDED:
                    //TODO: Add new mediaItem to the list manually.
                    break;
                case MODIFIED:
                    //TODO: Modify the mediaItem manually.
                    break;
                case REMOVED:
                    //TODO: Remove the mediaItem manually.
                    break;
            }
        }
    }
}
