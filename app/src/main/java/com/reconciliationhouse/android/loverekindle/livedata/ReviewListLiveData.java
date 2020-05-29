package com.reconciliationhouse.android.loverekindle.livedata;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.reconciliationhouse.android.loverekindle.models.MediaReview;

import java.util.ArrayList;
import java.util.List;

public class ReviewListLiveData extends MutableLiveData<List<MediaReview>> implements EventListener<QuerySnapshot> {
    private static final String TAG = ReviewListLiveData.class.getSimpleName();
    private Query mQuery;
    private List<MediaReview> tempList = null;
    private ListenerRegistration listenerRegistration = null;

    public ReviewListLiveData(Query query) {
        mQuery = query;
    }

    @Override
    protected void onActive() {
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
            Log.w(TAG, "Review SnapshotListener:error", e);
            return;
        }
        if (tempList == null) loadAll(queryDocumentSnapshots);
        else if (queryDocumentSnapshots != null) handleChangesOnly(queryDocumentSnapshots);
    }

    private void loadAll(@Nullable QuerySnapshot queryDocumentSnapshots) {
        if (queryDocumentSnapshots != null) {
            tempList = new ArrayList<>();
            tempList.clear();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                MediaReview review = documentSnapshot.toObject(MediaReview.class);
                review.setId(documentSnapshot.getId());
                tempList.add(review);
            }
            if (tempList != getValue()) this.postValue(tempList);
        }
    }

    private void handleChangesOnly(@NonNull QuerySnapshot queryDocumentSnapshots) {
        for (DocumentChange docChanges : queryDocumentSnapshots.getDocumentChanges()) {
            MediaReview changeReview = docChanges.getDocument().toObject(MediaReview.class);
            changeReview.setId(docChanges.getDocument().getId());
            switch (docChanges.getType()) {
                case ADDED:
                    if (tempList.size() > 0 && docChanges.getOldIndex() != -1) {
                    } else if (!tempList.contains(changeReview)) tempList.add(changeReview);
                    break;
                case MODIFIED:
                    for (MediaReview item : tempList) {
                        if (item.getId().equals(changeReview.getId()))
                            tempList.set(tempList.indexOf(item), changeReview);
                    }
                    break;
                case REMOVED:
                    //TODO: Remove the review manually.
                    if (!tempList.remove(changeReview)) {
                        for (MediaReview review : tempList)
                            if (changeReview.getId().equals(review.getId()))
                                tempList.remove(review);
                    }
                    break;
            }
            if (getValue() != tempList) this.postValue(tempList);
        }

    }


}
