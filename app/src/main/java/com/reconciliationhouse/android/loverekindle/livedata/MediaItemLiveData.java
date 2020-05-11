package com.reconciliationhouse.android.loverekindle.livedata;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;

import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;

public class MediaItemLiveData extends MutableLiveData<MediaItem> implements EventListener<DocumentSnapshot> {
    private static final String TAG = MediaItemLiveData.class.getSimpleName();

    private DocumentReference mDocumentReference;
    private ListenerRegistration mListener;

    public MediaItemLiveData(DocumentReference documentReference) {
        mDocumentReference = documentReference;
    }

    @Override
    public void setValue(MediaItem value) {
        super.setValue(value);
        mDocumentReference.set(value, SetOptions.merge());
    }

    @Override
    protected void onActive() {
        mListener = mDocumentReference.addSnapshotListener(this);
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
            super.setValue(documentSnapshot.toObject(MediaItem.class));
    }
}
