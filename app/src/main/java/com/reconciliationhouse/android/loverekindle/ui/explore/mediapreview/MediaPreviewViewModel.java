package com.reconciliationhouse.android.loverekindle.ui.explore.mediapreview;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.repository.MediaRepo;

import java.util.List;
import java.util.Objects;

public class MediaPreviewViewModel extends ViewModel {
    public String mediaId;
    private MutableLiveData<MediaItem> mediaItem;
    private MutableLiveData<List<MediaItem>> relatedMedia;

    private MediaPreviewViewModel(String mediaId, String category) {
        mediaItem = MediaRepo.getInstance().getMediaLiveData(mediaId);
        relatedMedia = MediaRepo.getInstance().getLiveListOfMediaInCategory(category);
        this.mediaId = mediaId;
    }

    public LiveData<MediaItem> getMediaItem() {
        return mediaItem;
    }

    public void setMediaItem(MutableLiveData<MediaItem> mediaItem) {
        this.mediaItem = mediaItem;
    }

    public LiveData<List<MediaItem>> getRelatedMedia() {
        return relatedMedia;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final String mediaId;
        private final String category;

        public Factory(@NonNull String mediaId, @NonNull String category) {
            this.mediaId = mediaId;
            this.category = category;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MediaPreviewViewModel(mediaId, category);
        }
    }
}
