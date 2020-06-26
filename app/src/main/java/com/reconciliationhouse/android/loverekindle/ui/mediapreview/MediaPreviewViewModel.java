package com.reconciliationhouse.android.loverekindle.ui.mediapreview;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.data.LocalDatabase;
import com.reconciliationhouse.android.loverekindle.data.LocalMedia;
import com.reconciliationhouse.android.loverekindle.data.LocalMediaDao;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.models.MediaReview;
import com.reconciliationhouse.android.loverekindle.repository.LocalMediaRepository;
import com.reconciliationhouse.android.loverekindle.repository.MediaRepo;
import com.reconciliationhouse.android.loverekindle.repository.MediaReviewRepo;
import com.reconciliationhouse.android.loverekindle.repository.UserRepo;

import java.io.FileNotFoundException;
import java.util.List;

public class MediaPreviewViewModel extends AndroidViewModel {
    private static final String TAG = MediaPreviewViewModel.class.getSimpleName();
    public String mediaId;
    private MutableLiveData<List<MediaItem>> relatedMedia;
    private MutableLiveData<List<MediaReview>> reviews;
    private MutableLiveData<MediaReview> userReview;
    private LiveData<MediaReview> latestReview;
    private final LocalMediaRepository mLocalMediaRepo;
    private MutableLiveData<MediaItem> mediaItem;
    private String mMediaFileName = "";
    private LiveData<LocalMedia> mLocalMedia;
    private boolean mStartMediaPlayer;

    public MediaPreviewViewModel(@NonNull Application application, String mediaId, String category) {
        super(application);
        this.mediaId = mediaId;
        mLocalMediaRepo = new LocalMediaRepository(application);
        mediaItem = MediaRepo.getInstance().getMediaLiveData(mediaId);
        relatedMedia = MediaRepo.getInstance().getLiveListOfMediaInCategory(category);
        reviews = MediaReviewRepo.getInstance().getMediaReviews(mediaId);
        userReview = MediaReviewRepo.getInstance().getUserReview(mediaId);
        latestReview = MediaReviewRepo.getInstance().getLatestReview(mediaId);
        mLocalMedia = mLocalMediaRepo.getMedia(mediaId);
    }

    public LiveData<MediaItem> getMediaItem() {
        return mediaItem;
    }

    public LiveData<List<MediaItem>> getRelatedMedia() {
        return relatedMedia;
    }

    public MutableLiveData<List<MediaReview>> getReviews() {
        return reviews;
    }

    public MutableLiveData<MediaReview> getUserReview() {
        return userReview;
    }

    public LiveData<MediaReview> getLatestReview() {
        return latestReview;
    }

    public void updateUserReview(String comment) {
        if (userReview.getValue() == null) {
            MediaReview review = new MediaReview(mediaId, UserRepo.getInstance().getUserId(), comment);
            MediaReviewRepo.getInstance().uploadReview(review);
            userReview.setValue(review);
        } else {
            userReview.getValue().setReview(comment);
            MediaReviewRepo.getInstance().updateReview(userReview.getValue());
        }
    }

    public String getMediaFileName() throws FileNotFoundException {

        if (mediaItem.getValue() != null && mMediaFileName.equals("")) {
            try {
                StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(mediaItem.getValue().getMedia_url());
                mMediaFileName = reference.getName();
            } catch (IllegalArgumentException e) {
                throw new FileNotFoundException();
            }
        }
        return mMediaFileName;
    }

    public void updateLocalMediaUri(String uri) {
        if (mLocalMedia.getValue() != null) {
            mLocalMedia.getValue().setMedia_uri(uri);
            mLocalMedia.getValue().setDownloaded(true);
            mLocalMediaRepo.updateMedia(mLocalMedia.getValue());
            updateDownloadCount();
        } else {
            mLocalMedia = mLocalMediaRepo.getMedia(mediaId);
        }
    }

    public void saveCurrentPosition(int pos) {
        if (mLocalMedia.getValue() != null) {
            mLocalMedia.getValue().setCurrent_position(pos);
            mLocalMediaRepo.updateMedia(mLocalMedia.getValue());
        }
    }

    public void insertLocalMedia() {
        mLocalMediaRepo.insertMedia(new LocalMedia(mediaItem.getValue()));
    }

    public LiveData<LocalMedia> getLocalMedia() {
        if (mLocalMedia.getValue() == null) mLocalMedia = mLocalMediaRepo.getMedia(mediaId);
        return mLocalMedia;
    }

    public void updateDownloadCount() {
        MediaRepo.getInstance().updateDownloadCount(mediaItem.getValue());
        mediaItem.getValue().downloaded();
    }

    public void startMediaPlayerImmediately(boolean startMediaPlayer) {
        mStartMediaPlayer = startMediaPlayer;
    }

    /**
     * You can only use once
     */
    public final boolean shouldStartMediaPlayer() {
        if (mStartMediaPlayer) {
            mStartMediaPlayer = false;
            return true;
        } else return false;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final Application mApplication;
        private final String mediaId;
        private final String category;

        public Factory(@NonNull Application application, @NonNull String mediaId, @NonNull String category) {
            mApplication = application;
            this.mediaId = mediaId;
            this.category = category;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MediaPreviewViewModel(mApplication, mediaId, category);
        }
    }

}
