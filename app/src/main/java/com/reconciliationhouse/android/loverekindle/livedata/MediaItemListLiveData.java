package com.reconciliationhouse.android.loverekindle.livedata;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MediaItemListLiveData extends MutableLiveData<List<MediaItem>> implements EventListener<QuerySnapshot> {

    private static final String TAG = MediaItemListLiveData.class.getSimpleName();
    private Query mQuery;
    private CollectionReference mCollectionRef;
    private List<MediaItem> tempMediaList = null;
    private MutableLiveData<List<MediaItem>> mediaItemLiveData = new MutableLiveData<>();
    private ListenerRegistration listenerRegistration = null;

    public MediaItemListLiveData(CollectionReference collectionReference) {
        mCollectionRef = collectionReference;
        tempMediaList = new ArrayList<>();
    }

    /**
     * <p>
     * This class is for getting a live list of mediaItems from Firestore just to display in a
     * recycler view or other mediaItem Overview cards. It will only update changes to fields that
     * get displayed in mediaItem cards not all the properties of the mediaItem will be monitored
     * live.</p>
     */
    public MediaItemListLiveData(Query query) {
        mQuery = query;
    }

    @Override
    protected void onActive() {
        if (mCollectionRef != null) listenerRegistration = mCollectionRef.addSnapshotListener(this);
        if (mQuery != null) listenerRegistration = mQuery.addSnapshotListener(this);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        listenerRegistration.remove();
        super.onInactive();
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "Media SnapshotListener:error", e);
            return;
        }
        if (tempMediaList == null) loadAll(queryDocumentSnapshots);
        else if (queryDocumentSnapshots != null) handleChangesOnly(queryDocumentSnapshots);
    }

    private void loadAll(@Nullable QuerySnapshot queryDocumentSnapshots) {
        if (queryDocumentSnapshots != null) {
            tempMediaList = new ArrayList<>();
            tempMediaList.clear();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                MediaItem mediaItem = documentSnapshot.toObject(MediaItem.class);
                tempMediaList.add(mediaItem);
            }
            mediaItemLiveData.setValue(tempMediaList);
            if (tempMediaList != getValue()) this.postValue(tempMediaList);
        }
    }

    private void handleChangesOnly(@NonNull QuerySnapshot queryDocumentSnapshots) {
        for (DocumentChange docChanges : queryDocumentSnapshots.getDocumentChanges()) {
            MediaItem mediaItem = docChanges.getDocument().toObject(MediaItem.class);
            switch (docChanges.getType()) {
                case ADDED:
                    if (tempMediaList.size() > 0 && docChanges.getOldIndex() != -1) {
                    } else if (!tempMediaList.contains(mediaItem)) tempMediaList.add(mediaItem);
                    break;
                case MODIFIED:
                    updateList(docChanges, mediaItem);
                    break;
                case REMOVED:
                    //TODO: Remove the mediaItem manually.
                    if (!tempMediaList.remove(mediaItem)) {
                        for (MediaItem media : tempMediaList)
                            if (mediaItem.getId().equals(media.getId()))
                                tempMediaList.remove(media);
                    }
                    break;
            }
            if (getValue() != tempMediaList) this.postValue(tempMediaList);
        }

    }

    private void updateList(DocumentChange docChanges, MediaItem mediaItem) {
        int oldIndex = docChanges.getOldIndex();
        int newIndex = docChanges.getNewIndex();
        MediaItem oldItem = null;
        int updateIndex = -1;

        try {
            oldItem = tempMediaList.get(oldIndex);
            updateIndex = oldIndex;
        } catch (IndexOutOfBoundsException e) {
            for (MediaItem item : tempMediaList) {
                if (item.getId().equals(mediaItem.getId())) {
                    oldItem = item;
                    updateIndex = tempMediaList.indexOf(oldItem);
                }
            }
            if (oldItem == null) return;
        }
        if (shouldUpdate(oldItem, mediaItem)) {
            oldItem.setAuthor(mediaItem.getAuthor());
            oldItem.setCategory(mediaItem.getCategory());
            oldItem.setImage_url(mediaItem.getImage_url());
            oldItem.setTitle(mediaItem.getTitle());
            oldItem.setPrice(mediaItem.getPrice());
            oldItem.setType(mediaItem.getType());
            tempMediaList.set(updateIndex, oldItem);
            setValue(tempMediaList);
        }
    }

    private boolean shouldUpdate(MediaItem old, MediaItem newItem) {
        return !old.getAuthor().equals(newItem.getAuthor()) ||
                !old.getCategory().equals(newItem.getCategory()) ||
                !old.getImage_url().equals(newItem.getImage_url()) ||
                !old.getTitle().equals(newItem.getTitle()) ||
                old.getPrice() != newItem.getPrice() ||
                old.getType() != newItem.getType();
    }
}
