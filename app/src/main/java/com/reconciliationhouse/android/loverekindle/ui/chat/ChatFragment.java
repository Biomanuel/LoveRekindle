package com.reconciliationhouse.android.loverekindle.ui.chat;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.reconciliationhouse.android.loverekindle.models.Message;
import com.reconciliationhouse.android.loverekindle.databinding.ChatFragmentBinding;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.models.UserSender;

import java.util.Objects;

public class ChatFragment extends Fragment {

    private ChatViewModel mViewModel;
    private ChatFragmentBinding binding;
    private FirebaseFirestore db;
    private UserModel userModel;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    String name;


    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ChatFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        if (getArguments() != null) {


            ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());
            Gson gson = new Gson();
            final UserModel model = gson.fromJson(args.getCounsellorData(), UserModel.class);
            String category=model.getCategory();
             name=model.getName();
           String id = model.getUserId();
            userModel=new UserModel(id,name,category);
            binding.counsellorUsername.setText(model.getName());
            mAuth=FirebaseAuth.getInstance();
            firebaseUser=mAuth.getCurrentUser();






        }
        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text= Objects.requireNonNull(binding.messagesText.getText()).toString();
                if (!(TextUtils.isEmpty(text))){
                    Message message=new Message(text,new UserSender(firebaseUser.getUid(),firebaseUser.getDisplayName(),String.valueOf(firebaseUser.getPhotoUrl())));
                    db=FirebaseFirestore.getInstance();

                    CollectionReference reference=db.collection("Chat").document("single") .collection("Chat with "+name);

                    reference.add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                           if (task.isSuccessful()){
                               binding.messagesText.setText("");
                           }
                        }
                    });

                }


            }
        });

    }
}
