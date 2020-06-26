package com.reconciliationhouse.android.loverekindle.ui.counsellor;

import android.app.Application;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.repository.CounsellorRequestRepo;

import java.util.List;

public class AllCounsellorsViewModel extends AndroidViewModel {
     private  CounsellorRequestRepo counsellorRequestRepo;
    private MutableLiveData<List<UserModel>> getAllCounsellors;
    public AllCounsellorsViewModel(Application application){
        super(application);
        counsellorRequestRepo=new CounsellorRequestRepo();


    }
    public LiveData<List<UserModel>> getAllCounsellors(ProgressBar progressBar) {
        getAllCounsellors =counsellorRequestRepo.getAllCounsellors(progressBar);
        return getAllCounsellors;
    }

}
