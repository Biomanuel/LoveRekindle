package com.reconciliationhouse.android.loverekindle.ui.explore.mediapreview;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.repository.MediaRepo;

public class MediaPreviewViewModel extends ViewModel {
    public String mediaId;
    private MutableLiveData<MediaItem> mediaItem;

    private MediaPreviewViewModel(String mediaId) {
        mediaItem = MediaRepo.getInstance().getMediaLiveData(mediaId);
        this.mediaId = mediaId;
    }

    public LiveData<MediaItem> getMediaItem() {
        return mediaItem;
    }

    public void setMediaItem(MutableLiveData<MediaItem> mediaItem) {
        this.mediaItem = mediaItem;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final String mediaId;

        public Factory(@NonNull String plantId) {
            mediaId = plantId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MediaPreviewViewModel(mediaId);
        }
    }
}
