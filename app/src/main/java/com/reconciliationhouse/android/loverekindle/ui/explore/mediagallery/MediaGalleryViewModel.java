package com.reconciliationhouse.android.loverekindle.ui.explore.mediagallery;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.savedstate.SavedStateRegistryOwner;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.livedata.MediaItemListLiveData;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.repository.MediaRepo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MediaGalleryViewModel extends AndroidViewModel {

    public static final String IS_FILTERED = "isFiltered";
    private final SavedStateHandle state;
    private MutableLiveData<List<MediaItem>> mAllMedia;
    private MutableLiveData<List<MediaItem>> audios;
    private MutableLiveData<List<MediaItem>> ebooks;
    private MutableLiveData<Boolean> isFiltered;

    public MediaGalleryViewModel(Application application, SavedStateHandle savedStateHandle) {
        super(application);
        mAllMedia = MediaRepo.getInstance().getFireStoreAllMediaLiveData();
        audios = MediaRepo.getInstance().getFireStoreAudiosLiveData();
        ebooks = MediaRepo.getInstance().getFireStoreEbooksLiveData();

        // ViewModel SavedInstant
        state = savedStateHandle;
        isFiltered = state.getLiveData(IS_FILTERED, false);
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
        state.set(IS_FILTERED, true);
    }

    public HashMap<String, List<MediaItem>> categorise(List<MediaItem> mediaItemList) {
        HashMap<String, List<MediaItem>> categoryMap = new HashMap<>();

        for (MediaItem media :
                mediaItemList) {
            if (categoryMap.containsKey(media.getCategory()))
                categoryMap.get(media.getCategory()).add(media);
            else
                categoryMap.put(media.getCategory(), new ArrayList<MediaItem>(Collections.singletonList(media)));
        }

        return categoryMap;
    }

    public void unfilter() {
        isFiltered.setValue(false);
        state.set(IS_FILTERED, false);
    }

    public static class MediaGallerySharedStateViewModelFactory extends ViewModelProvider.NewInstanceFactory {

        private static SavedStateViewModelFactory mFactory = null;

        public static SavedStateViewModelFactory getFactory(@NonNull Application application, @NonNull SavedStateRegistryOwner owner) {
            if (mFactory == null) mFactory = new SavedStateViewModelFactory(application, owner);
            return mFactory;
        }
    }
}