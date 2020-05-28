package com.reconciliationhouse.android.loverekindle.ui.chat;

import android.app.Application;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.repository.CounsellorRequestRepo;
import com.reconciliationhouse.android.loverekindle.repository.MediaRepo;

import java.util.List;

public class ChatCategoriesViewModel extends AndroidViewModel {
    private  CounsellorRequestRepo counsellorRequestRepo;
    private MutableLiveData<List<UserModel>> getAllSpiritualCounsellors;
    private MutableLiveData<List<UserModel>> getAllParentingCounsellors;
    private MutableLiveData<List<UserModel>> getAllMarriageCounsellors;
    private MutableLiveData<List<UserModel>> getAllHealthCounsellors;


    public ChatCategoriesViewModel(Application application) {
        super(application);
counsellorRequestRepo=new CounsellorRequestRepo();



    }
    public LiveData<List<UserModel>> getAllSpiritualCounsellors(ProgressBar progressBar, TextView textView) {
        getAllSpiritualCounsellors =counsellorRequestRepo.getAllSpiritualCounsellors(progressBar,textView);
        return getAllSpiritualCounsellors;
    }
    public LiveData<List<UserModel>> getAllParentingCounsellors(ProgressBar progressBar, TextView textView) {
        getAllParentingCounsellors =counsellorRequestRepo.getAllParentingCounsellors(progressBar,textView);
        return getAllParentingCounsellors;
    }
    public LiveData<List<UserModel>> getAllMarriageCounsellors(ProgressBar progressBar, TextView textView) {
        getAllMarriageCounsellors =counsellorRequestRepo.getAllRelationshipCounsellors(progressBar,textView);
        return getAllMarriageCounsellors;
    }
    public LiveData<List<UserModel>> getAllHealthCounsellors(ProgressBar progressBar, TextView textView) {
        getAllHealthCounsellors =counsellorRequestRepo.getAllHealthCounsellors(progressBar,textView);
        return getAllHealthCounsellors;
    }


}
