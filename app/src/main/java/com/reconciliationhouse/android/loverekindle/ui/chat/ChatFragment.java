package com.reconciliationhouse.android.loverekindle.ui.chat;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.reconciliationhouse.android.loverekindle.adapters.ChatAdapter;
import com.reconciliationhouse.android.loverekindle.models.Message;
import com.reconciliationhouse.android.loverekindle.databinding.ChatFragmentBinding;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.models.UserSender;
import com.reconciliationhouse.android.loverekindle.preferences.UserPreferences;

import java.util.List;
import java.util.Objects;

public class ChatFragment extends Fragment {

    private ChatViewModel mViewModel;
    private ChatFragmentBinding binding;
    private FirebaseFirestore db;
    private UserModel userModel;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    String name;
    private ChatAdapter adapter;
   CollectionReference messagesReference;
    CollectionReference reference;



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
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;

        mViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        if (getArguments() != null) {


            ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());
            Gson gson=new Gson();
            UserModel model = gson.fromJson(args.getCounsellorData(), UserModel.class);
            String category = model.getCategory();
            name = model.getName();
            String id = model.getUserId();
            userModel = new UserModel(id, name, category);
            binding.counsellorUsername.setText(model.getName());




            }


              adapter = new ChatAdapter();

                mViewModel.getAllSingleChat(name,firebaseUser.getDisplayName()).observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
                @Override
                public void onChanged(List<Message> messages) {


                    adapter.setMessages(messages);
                    Toast.makeText(getContext(),String.valueOf(messages.size()),Toast.LENGTH_SHORT).show();
                }

            });

            binding.messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.messagesRecyclerView.setAdapter(adapter);
            binding.messagesRecyclerView.setAdapter(adapter);

            binding.sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String text = Objects.requireNonNull(binding.messagesText.getText()).toString();
                    if (!(TextUtils.isEmpty(text))) {

                        Message message = new Message(text, new UserSender(firebaseUser.getUid(), firebaseUser.getDisplayName(), String.valueOf(firebaseUser.getPhotoUrl())));
                        db = FirebaseFirestore.getInstance();
                        Toast.makeText(getContext(),UserPreferences.getRole(getContext()),Toast.LENGTH_SHORT).show();


                        if (UserPreferences.getRole(getContext()).equals("regular")){
                            reference = db.collection("Chat").document("single").collection(name+" and "+firebaseUser.getDisplayName());
                        }
                        else {
                             reference = db.collection("Chat").document("single").collection(firebaseUser.getDisplayName()+" and "+name);

                        }

                        reference.add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    binding.messagesText.setText("");
                                    mViewModel.getAllSingleChat(name,firebaseUser.getDisplayName()).observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
                                        @Override
                                        public void onChanged(List<Message> messages) {


                                            adapter.setMessages(messages);
                                            Toast.makeText(getContext(),String.valueOf(messages.size()),Toast.LENGTH_SHORT).show();
                                        }

                                    });


                                    binding.messagesRecyclerView.setAdapter(adapter);

                                }
                            }
                        });

                    }


                }
            });

        }}

