package com.reconciliationhouse.android.loverekindle.ui.chat;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.reconciliationhouse.android.loverekindle.MainActivity;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.adapters.chat.ChatListAdapter;
import com.reconciliationhouse.android.loverekindle.databinding.ChatHomeFragmentBinding;
import com.reconciliationhouse.android.loverekindle.models.ChatItem;
import com.reconciliationhouse.android.loverekindle.models.ChatModel;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.preferences.UserPreferences;
import com.reconciliationhouse.android.loverekindle.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatHomeFragment extends Fragment {
    ChatHomeFragmentBinding binding ;
    private List<ChatItem> mList;

    @Override
    public void onStart() {
        super.onStart();
        UserRepo.initializeWithUser((MainActivity) requireActivity());
        Toast.makeText(getContext(),UserRepo.user.getName(),Toast.LENGTH_SHORT).show();
    }

    public static ChatHomeFragment newInstance() {
        return new ChatHomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=ChatHomeFragmentBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (UserRepo.user.getRole()!=null){

            if (String.valueOf(UserRepo.user.getRole()).equals(String.valueOf(UserModel.Role.Counsellor))) {
                binding.addChat.setVisibility(View.GONE);

            }
        }
        else  {
            binding.addChat.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=auth.getCurrentUser();

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        assert firebaseUser != null;
        mList=new ArrayList<>();





        CollectionReference reference = db.collection("User").document(Objects.requireNonNull(firebaseUser.getEmail())).collection("chat");

        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    binding.progressBar.setVisibility(View.GONE);
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        ChatItem model = document.toObject(ChatItem.class);
                        mList.add(model);


                    }
                    ChatListAdapter adapter=new ChatListAdapter();

                    adapter.setChatItems(mList);
                    binding.allSingleChat.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.allSingleChat.setAdapter(adapter);
                }

            }
        });






        binding.addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller= Navigation.findNavController(v);
                controller.navigate(R.id.action_chatHomeFragment_to_navigation_chat);
            }
        });

    }

}
