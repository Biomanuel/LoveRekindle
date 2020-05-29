package com.reconciliationhouse.android.loverekindle.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.reconciliationhouse.android.loverekindle.livedata.ReviewListLiveData;
import com.reconciliationhouse.android.loverekindle.models.MediaReview;

public class MediaReviewRepo {
    public static final String collectionPath = "MediaReview";
    private static final String TAG = MediaReviewRepo.class.getSimpleName();
    private static MediaReviewRepo ourInstance;
    private FirebaseFirestore db;
    private CollectionReference mCollectionRef;

    private MediaReviewRepo() {
        this.db = FirebaseFirestore.getInstance();
        mCollectionRef = db.collection(collectionPath);
    }

    public static MediaReviewRepo getInstance() {
        if (ourInstance == null) ourInstance = new MediaReviewRepo();
        return ourInstance;
    }

    /**
     * @param mediaId the id of the media whose reviews are to be fetched
     */
    public ReviewListLiveData getMediaReviews(String mediaId) {
        // Prepare Query as you like.
        Query query = mCollectionRef
                .whereEqualTo("mediaId", mediaId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        return new ReviewListLiveData(query);
    }

    public void uploadReview(final MediaReview review) {
        mCollectionRef.add(review).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Review Upload Unsuccessful: ", task.getException());
                    return;
                }
                assert task.getResult() != null;
                review.setId(task.getResult().getId());
            }
        });
    }

    public MutableLiveData<MediaReview> getReview(String reviewId) {
        final MutableLiveData<MediaReview> result = new MutableLiveData<>();
        mCollectionRef.document(reviewId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Review Upload Unsuccessful: ", task.getException());
                    return;
                }
                assert task.getResult() != null;
                MediaReview review = task.getResult().toObject(MediaReview.class);
                review.setId(task.getResult().getId());
                result.postValue(review);
            }
        });
        return result;
    }

    public MutableLiveData<MediaReview> getLatestReview(String mediaId) {
        final MutableLiveData<MediaReview> result = new MutableLiveData<>();
        mCollectionRef.whereEqualTo("mediaId", mediaId)
                .orderBy("timestamp", Query.Direction.DESCENDING).limit(2)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Review Upload Unsuccessful: ", task.getException());
                    return;
                }
                assert task.getResult() != null;
                MediaReview review = null;
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    assert doc != null;
                    MediaReview prospect = doc.toObject(MediaReview.class);
                    if (!prospect.getUserId().equals(UserRepo.getInstance().getUserId())) {
                        review = prospect;
                        review.setId(doc.getId());
                    }
                }
                result.postValue(review);
            }
        });
        return result;
    }

    public void updateReview(MediaReview review) {
        mCollectionRef.document(review.getId()).set(review, SetOptions.merge());
    }

    public void updateReviewVote(MediaReview review) {
        mCollectionRef.document(review.getId()).update("votes", review.getVotes());
    }

    public void voteReview(MediaReview review, boolean inSupport) {
        String voterId = UserRepo.getInstance().getUserId();
        review.vote(voterId, inSupport);
        mCollectionRef.document(review.getId()).update("votes", review.getVotes());
    }

    public MutableLiveData<MediaReview> getUserReview(String mediaId) {
        final MutableLiveData<MediaReview> result = new MutableLiveData<>();
        mCollectionRef.whereEqualTo("mediaId", mediaId)
                .whereEqualTo("userId", UserRepo.getInstance().getUserId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Review Upload Unsuccessful: ", task.getException());
                    return;
                }
                assert task.getResult() != null;
                if (task.getResult().getDocuments().size() < 1) return;
                MediaReview review = task.getResult().getDocuments().get(0).toObject(MediaReview.class);
                review.setId(task.getResult().getDocuments().get(0).getId());
                result.postValue(review);
            }
        });
        return result;
    }
}
