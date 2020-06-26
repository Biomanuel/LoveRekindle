package com.reconciliationhouse.android.loverekindle.ui.mediareader;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.repository.MediaRepo;
import com.reconciliationhouse.android.loverekindle.ui.mediapreview.MediaPreviewViewModel;

public class AudioPlayerViewModel extends AndroidViewModel {

    private MutableLiveData<MediaItem> mediaItem;

    public AudioPlayerViewModel(@NonNull Application application, String mediaId) {
        super(application);
        mediaItem = MediaRepo.getInstance().getMediaLiveData(mediaId);
    }

    public LiveData<MediaItem> getMediaItem() {
        return mediaItem;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final String mediaId;
        private final Application mApplication;

        public Factory(@NonNull Application application, @NonNull String mediaId) {
            this.mediaId = mediaId;
            this.mApplication = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new AudioPlayerViewModel(mApplication, mediaId);
        }
    }

}
