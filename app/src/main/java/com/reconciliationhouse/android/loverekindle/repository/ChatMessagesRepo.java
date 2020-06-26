package com.reconciliationhouse.android.loverekindle.repository;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reconciliationhouse.android.loverekindle.models.Message;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.preferences.UserPreferences;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ChatMessagesRepo {
    private static ChatMessagesRepo chatMessagesRepo;
    private FirebaseFirestore db;
    private ListenerRegistration messagesReference;
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





            messagesReference=db.collection("Chat").document("single") .collection(counsellorName+" and "+username)
                    .orderBy("dateCreated", Query.Direction.ASCENDING)

                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);

                                return;
                            }
                            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                                switch (dc.getType()) {
                                    case ADDED:
                                        Message message = dc.getDocument().toObject(Message.class);
                                        messageList.add(message);
                                        Log.d(TAG, "New city: " + dc.getDocument().getData());
                                        break;
                                    case MODIFIED:
                                        Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                        break;
                                    case REMOVED:
                                        Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                        break;

                                }
                            }

//                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                                if (document.get("message") != null) {
//
//                                }
//
//
//                            }
                            listMutableLiveData.setValue(messageList);
                            Log.d(TAG, "Error while reading messages: " + messageList);
                        }
                    });


//        else {
//            messagesReference=db.collection("Chat").document("single") .collection(username+" and "+counsellorName);
//        }


//        Query query= messagesReference.orderBy("dateCreated", Query.Direction.ASCENDING);
//        query.get().add
                //addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Message message = document.toObject(Message.class);
//                        messageList.add(message);
//                    }
//                    listMutableLiveData.setValue(messageList);
//                }
//            }
//        });
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
    public MutableLiveData<List<Message>> getAllGroupChatMessages(String groupName){
        final List<Message>messageList=new ArrayList<>();
        final MutableLiveData<List<Message>> listMutableLiveData = new MutableLiveData<>((List<Message>) new ArrayList<Message>());





        messagesReference=db.collection("Chat").document("group") .collection(groupName)
                .orderBy("dateCreated", Query.Direction.ASCENDING)

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);

                            return;
                        }
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                            switch (dc.getType()) {
                                case ADDED:
                                    Message message = dc.getDocument().toObject(Message.class);
                                    messageList.add(message);
                                    Log.d(TAG, "New city: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                    break;

                            }
                        }

//                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                                if (document.get("message") != null) {
//
//                                }
//
//
//                            }
                        listMutableLiveData.setValue(messageList);
                        Log.d(TAG, "Error while reading messages: " + messageList);
                    }
                });


//        else {
//            messagesReference=db.collection("Chat").document("single") .collection(username+" and "+counsellorName);
//        }


//        Query query= messagesReference.orderBy("dateCreated", Query.Direction.ASCENDING);
//        query.get().add
        //addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Message message = document.toObject(Message.class);
//                        messageList.add(message);
//                    }
//                    listMutableLiveData.setValue(messageList);
//                }
//            }
//        });
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
