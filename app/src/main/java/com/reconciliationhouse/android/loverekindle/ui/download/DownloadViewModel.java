package com.reconciliationhouse.android.loverekindle.ui.download;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.repository.MediaRepo;

import java.util.List;

public class DownloadViewModel extends AndroidViewModel {
    private MutableLiveData<List<MediaItem>> mAllMedia;

    public DownloadViewModel(Application application) {
        super(application);
        mAllMedia = MediaRepo.getInstance().getDummyMedia(application);
    }

    public LiveData<List<MediaItem>> getAllMedia() {
        return mAllMedia;
    }
}
