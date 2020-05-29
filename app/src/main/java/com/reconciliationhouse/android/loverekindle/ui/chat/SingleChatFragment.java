package com.reconciliationhouse.android.loverekindle.ui.chat;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.SingleChatAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.SigngleChatFragmentBinding;
import com.reconciliationhouse.android.loverekindle.models.ChatModel;
import com.reconciliationhouse.android.loverekindle.models.UserDetails;
import com.reconciliationhouse.android.loverekindle.models.UserModel;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SingleChatFragment extends Fragment {

    private SingleChatViewModel mViewModel;
    SigngleChatFragmentBinding binding;
    private List<ChatModel>mList;


    public static SingleChatFragment newInstance() {
        return new SingleChatFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=SigngleChatFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SingleChatViewModel.class);
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=auth.getCurrentUser();

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        assert firebaseUser != null;
        mList=new ArrayList<>();
//        if(UserDetails.getCategory().equals("counsellor")){
            CollectionReference collectionReference = db.collection("User").document("counsellor").collection("spiritual").document(Objects.requireNonNull(firebaseUser.getDisplayName())).collection("single");
            collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            ChatModel model = document.toObject(ChatModel.class);
                            mList.add(model);


                        }

                        SingleChatAdapter adapter=new SingleChatAdapter();
                        adapter.setCounsellors(mList);
                        binding.allSingleChat.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.allSingleChat.setAdapter(adapter);
                    }

                }
            });

       // }


//            CollectionReference  reference=db.collection("User").document("regular").collection("users").document(firebaseUser.getDisplayName()).collection("single");
//            reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()){
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//
//                            ChatModel model = document.toObject(ChatModel.class);
//                            mList.add(model);
//
//
//                        }
//
//                        SingleChatAdapter adapter=new SingleChatAdapter();
//                        adapter.setCounsellors(mList);
//                        binding.allSingleChat.setLayoutManager(new LinearLayoutManager(getContext()));
//                        binding.allSingleChat.setAdapter(adapter);
//                    }
//
//                }
//            });





        binding.addChat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               NavController controller= Navigation.findNavController(v);
               controller.navigate(R.id.action_chatHomeFragment_to_navigation_chat);
           }
       });
    }

}
