package com.reconciliationhouse.android.loverekindle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> type = new MutableLiveData<>();

    public void setText(String item) {
        type.setValue(item);
    }

    public LiveData<String> getType() {
        return type;
    }
}
