package com.reconciliationhouse.android.loverekindle.ui.chat;

import android.app.Application;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.reconciliationhouse.android.loverekindle.models.Message;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.repository.ChatMessagesRepo;
import com.reconciliationhouse.android.loverekindle.repository.CounsellorRequestRepo;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private ChatMessagesRepo chatMessagesRepo;
    private MutableLiveData<List<Message>> getSingleChat;
    public ChatViewModel(@NonNull Application application) {
        super(application);
        chatMessagesRepo=new ChatMessagesRepo();
    }
    public LiveData<List<Message>> getAllSingleChat(String counsellorName,String username) {
        getSingleChat =chatMessagesRepo.getAllSingleChatMessages(counsellorName,username);
        return getSingleChat;
    }

}
