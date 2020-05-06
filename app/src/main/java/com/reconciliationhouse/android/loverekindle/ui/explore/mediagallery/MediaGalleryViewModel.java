package com.reconciliationhouse.android.loverekindle.ui.explore.mediagallery;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.reconciliationhouse.android.loverekindle.livedata.MediaItemListLiveData;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.repository.MediaRepo;

import java.util.List;

public class MediaGalleryViewModel extends AndroidViewModel {

    private MutableLiveData<List<MediaItem>> mAllMedia;
    private MutableLiveData<List<MediaItem>> audios;
    private MutableLiveData<List<MediaItem>> ebooks;
    private MutableLiveData<Boolean> isFiltered;

    public MediaGalleryViewModel(Application application, SavedStateHandle savedStateHandle) {
        super(application);
        mAllMedia = MediaRepo.getInstance().getFireStoreAllMediaLiveData();
        audios = MediaRepo.getInstance().getFireStoreAudiosLiveData();
        ebooks = MediaRepo.getInstance().getFireStoreEbooksLiveData();

        //TODO: use ViewModel SavedInstant
        isFiltered = savedStateHandle.getLiveData("isFiltered", false);
    }

    public LiveData<List<MediaItem>> getAllMedia() {
        return mAllMedia;
    }

    public LiveData<List<MediaItem>> getAudios() {
        return audios;
    }

    public LiveData<List<MediaItem>> getEbooks() {
        return ebooks;
    }

    public MutableLiveData<Boolean> getIsFiltered() {
        return isFiltered;
    }

    public void filter() {
        isFiltered.setValue(true);
    }

    public void unfilter() {
        isFiltered.setValue(false);
    }
}