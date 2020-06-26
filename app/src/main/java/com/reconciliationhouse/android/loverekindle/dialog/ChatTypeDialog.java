package com.reconciliationhouse.android.loverekindle.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.models.ChatItem;
import com.reconciliationhouse.android.loverekindle.models.ChatModel;
import com.reconciliationhouse.android.loverekindle.models.ChatType;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.ui.chat.ChatCategoriesFragmentDirections;

import java.util.Objects;

public class ChatTypeDialog extends DialogFragment {
    Fragment fragment;
    String counsellorEmail, counsellorName;
    UserModel.Category category;
    String counsellorId;
    String profileImageUrl;
    private View.OnClickListener navigateToGroupChat = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Objects.requireNonNull(getDialog()).dismiss();
            FragmentManager fm = fragment.getActivity().getSupportFragmentManager();
          CreateGroupDialog custom=new CreateGroupDialog(fragment,counsellorEmail);
            custom.show(fm,"");

        }
    };
    private View.OnClickListener navigateToSingleChat = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Objects.requireNonNull(getDialog()).dismiss();
            final NavController navController = NavHostFragment.findNavController(fragment);
            ChatType chatType=new ChatType(counsellorId, counsellorName, category, profileImageUrl, ChatItem.ChatType.Single_Chat);

            Gson gson = new Gson();
            final String jsonString = gson.toJson(chatType);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String username = null;
            final FirebaseUser firebaseUser = auth.getCurrentUser();
            if (firebaseUser != null) {
                username = firebaseUser.getDisplayName();
            }
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            assert username != null;

            DocumentReference reference = db.collection("User").document(firebaseUser.getEmail()).collection("chat").document(counsellorEmail + " and " + firebaseUser.getEmail());

            reference.set(new ChatItem(counsellorEmail + " and " + firebaseUser.getEmail(), ChatItem.ChatType.Single_Chat, counsellorName, profileImageUrl))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                DocumentReference reference = db.collection("User").document(counsellorEmail).collection("chat").document(counsellorEmail + " and " + firebaseUser.getEmail());
                                reference.set(new ChatItem(counsellorEmail + " and " + firebaseUser.getEmail(), ChatItem.ChatType.Single_Chat, firebaseUser.getDisplayName(), String.valueOf(firebaseUser.getPhotoUrl()))).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        ChatCategoriesFragmentDirections.ActionNavigationChatToChatFragment actionNavigationChatToChatFragment = ChatCategoriesFragmentDirections.actionNavigationChatToChatFragment();
                                        actionNavigationChatToChatFragment.setCounsellorData(jsonString);
                                        navController.navigate(actionNavigationChatToChatFragment);
                                    }
                                });
                            }

                        }
                    });


        }
    };

    public ChatTypeDialog(Fragment fragment, String counsellorName, String counsellorEmail, String counsellorId, UserModel.Category category, String profileImageUrl) {
        this.fragment = fragment;
        this.counsellorEmail = counsellorEmail;
        this.counsellorId = counsellorId;
        this.category = category;
        this.profileImageUrl = profileImageUrl;
        this.counsellorName = counsellorName;


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@androidx.annotation.Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.chat_type_dialog, null);


        Button groupChat = dialogView.findViewById(R.id.group_chat);
        Button singleChat = dialogView.findViewById(R.id.single_chat);
        singleChat.setOnClickListener(navigateToSingleChat);
        groupChat.setOnClickListener(navigateToGroupChat);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setView(dialogView);


        return builder.create();
    }
}

