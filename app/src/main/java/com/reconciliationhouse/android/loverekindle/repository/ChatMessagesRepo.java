package com.reconciliationhouse.android.loverekindle.repository;

import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reconciliationhouse.android.loverekindle.models.Message;
import com.reconciliationhouse.android.loverekindle.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ChatMessagesRepo {
    private static ChatMessagesRepo chatMessagesRepo;
    private FirebaseFirestore db;
    private CollectionReference messagesReference;

    public ChatMessagesRepo() {
        this.db = FirebaseFirestore.getInstance();
    }
    public static ChatMessagesRepo getInstance() {
        if (chatMessagesRepo == null) chatMessagesRepo = new ChatMessagesRepo();

        return chatMessagesRepo;
    }

    public MutableLiveData<List<Message>> getAllSingleChatMessages(String counsellorName, String username){
       final List<Message>messageList=new ArrayList<>();
        final MutableLiveData<List<Message>> listMutableLiveData = new MutableLiveData<>((List<Message>) new ArrayList<Message>());



       // messagesReference=db.collection("Chat").document("single") .collection(counsellorName+" and "+username);
        messagesReference=db.collection("Chat").document("single") .collection(username+" and "+counsellorName);
        Query query= messagesReference.orderBy("dateCreated", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Message message = document.toObject(Message.class);
                        messageList.add(message);
                    }
                    listMutableLiveData.setValue(messageList);
                }
            }
        });
//        messagesReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//
//
//                }
//
//            }
//        });

        return listMutableLiveData;

    }

}
