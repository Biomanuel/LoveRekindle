package com.reconciliationhouse.android.loverekindle.livedata;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.models.MediaReview;
import com.reconciliationhouse.android.loverekindle.repository.UserRepo;

public class MediaReviewLiveData extends MutableLiveData<MediaReview> implements EventListener<DocumentSnapshot> {
    private static final String TAG = MediaReviewLiveData.class.getSimpleName();

    private DocumentReference mDocumentReference;
    private ListenerRegistration mListener;
    private Query mQuery;

    public MediaReviewLiveData(DocumentReference documentReference) {
        mDocumentReference = documentReference;
    }

    //TODO: Incomplete work! I was trying to create a review LiveData that is sync to Firestore
    public MediaReviewLiveData(Query query, final CollectionReference collectionReference) {
        mQuery = query;
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Unable to fetch UserReview: ", task.getException());
                    return;
                }
                assert task.getResult() != null;
                if (task.getResult().getDocuments().size() < 1) {
                    Log.e(TAG, "Unable to fetch UserReview: ", task.getException());
                    return;
                }
                MediaReview review = task.getResult().getDocuments().get(0).toObject(MediaReview.class);
                review.setId(task.getResult().getDocuments().get(0).getId());
                mDocumentReference = collectionReference.document(task.getResult().getDocuments().get(0).getId());
                MediaReviewLiveData.super.postValue(review);
            }
        });
    }


    @Override
    public void setValue(MediaReview value) {
        super.setValue(value);
        if (mDocumentReference != null) mDocumentReference.set(value, SetOptions.merge());
    }

    @Override
    protected void onActive() {
        if (mDocumentReference != null) mListener = mDocumentReference.addSnapshotListener(this);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        if (mListener != null) mListener.remove();
        super.onInactive();
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            Log.e(TAG, "onEvent: error", e);
            return;
        }

        if (documentSnapshot != null && documentSnapshot.exists())
            super.setValue(documentSnapshot.toObject(MediaReview.class));
    }
}
