package com.reconciliationhouse.android.loverekindle.repository;

import android.content.Context;
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
import com.reconciliationhouse.android.loverekindle.preferences.UserPreferences;

import java.util.ArrayList;
import java.util.List;

public class ChatMessagesRepo {
    private static ChatMessagesRepo chatMessagesRepo;
    private FirebaseFirestore db;
    private CollectionReference messagesReference;
    private Context context;

    public ChatMessagesRepo(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.context=context;
    }
    public static ChatMessagesRepo getInstance() {
        if (chatMessagesRepo == null) chatMessagesRepo = new ChatMessagesRepo(chatMessagesRepo.context);

        return chatMessagesRepo;
    }

    public MutableLiveData<List<Message>> getAllSingleChatMessages(String counsellorName, String username){
       final List<Message>messageList=new ArrayList<>();
        final MutableLiveData<List<Message>> listMutableLiveData = new MutableLiveData<>((List<Message>) new ArrayList<Message>());




        if (UserPreferences.getRole(context).equals("regular")){
            messagesReference=db.collection("Chat").document("single") .collection(counsellorName+" and "+username);
        }
        else {
            messagesReference=db.collection("Chat").document("single") .collection(username+" and "+counsellorName);
        }


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
