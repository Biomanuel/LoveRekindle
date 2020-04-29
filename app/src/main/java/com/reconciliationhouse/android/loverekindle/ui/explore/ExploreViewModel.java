package com.reconciliationhouse.android.loverekindle.ui.explore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.reconciliationhouse.android.loverekindle.models.MediaItem;

import java.util.List;

public class ExploreViewModel extends ViewModel {

    private MutableLiveData<List<MediaItem>> mAllMedia;

    public ExploreViewModel() {
        mAllMedia = new MutableLiveData<>();
    }

    public LiveData<List<MediaItem>> getAllMedia() {
        return mAllMedia;
    }
}