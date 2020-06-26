package com.reconciliationhouse.android.loverekindle.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.models.ChatItem;
import com.reconciliationhouse.android.loverekindle.models.ChatType;
import com.reconciliationhouse.android.loverekindle.preferences.UserPreferences;
import com.reconciliationhouse.android.loverekindle.ui.chat.ChatCategoriesFragmentDirections;

import java.util.Objects;

public class CreateGroupDialog extends DialogFragment {
    private Fragment fragment;
   public EditText groupName;
   private ProgressBar progressBar;
   private String counsellorEmail;

    public CreateGroupDialog(Fragment fragment,String counsellorEmail) {
        this.fragment = fragment;
        this.counsellorEmail=counsellorEmail;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@androidx.annotation.Nullable Bundle savedInstanceState) {


        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.group_chat_name_dialog, null);


        Button create=dialogView.findViewById(R.id.create_group);
        Button cancel=dialogView.findViewById(R.id.cancel_group);
         groupName=dialogView.findViewById(R.id.group);
         progressBar=dialogView.findViewById(R.id.progress_bar);
        create.setOnClickListener(createGroup);
        cancel.setOnClickListener(cancelGroup);

        MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getActivity());
        builder.setView(dialogView);



        return builder.create();
    }
    private  View.OnClickListener createGroup=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String group=groupName.getText().toString();
            if (TextUtils.isEmpty(group)){
                groupName.setError("Enter a group Name");
            }
            else {
                groupName.setError(null);
                progressBar.setVisibility(View.VISIBLE);
                final FirebaseFirestore db=FirebaseFirestore.getInstance();
                CollectionReference reference=db.collection("Chat").document("group").collection(group);
                 reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         if(task.getResult().size() > 0) {
                             progressBar.setVisibility(View.GONE);
                             Toast.makeText(getContext(),"Group Name Already exist ",Toast.LENGTH_SHORT).show();
                         } else {

                             DocumentReference reference = db.collection("User").document(counsellorEmail).collection("chat").document(group);
                             reference.set(new ChatItem(group, ChatItem.ChatType.Group_Chat,null))
                                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                                         @Override
                                         public void onComplete(@NonNull Task<Void> task) {
                                             if (task.isSuccessful()){
                                                 DocumentReference reference = db.collection("User").document(UserPreferences.getEmail(getContext())).collection("chat").document(group);
                                                 reference.set(new ChatItem(group, ChatItem.ChatType.Group_Chat,null))
                                                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task) {
                                                                 if (task.isSuccessful()){

                                                                    ChatItem chatItem= new ChatItem(group, ChatItem.ChatType.Group_Chat,"");
                                                                     Gson gson = new Gson();
                                                                     final String jsonString = gson.toJson(chatItem);
                                                                     progressBar.setVisibility(View.GONE);
                                                                     final NavController navController = NavHostFragment.findNavController(fragment);
                                                                     ChatCategoriesFragmentDirections.ActionNavigationChatToChatFragment actionNavigationChatToChatFragment = ChatCategoriesFragmentDirections.actionNavigationChatToChatFragment();
                                                                     actionNavigationChatToChatFragment.setCounsellorData(jsonString);
                                                                     navController.navigate(actionNavigationChatToChatFragment);
                                                                     Objects.requireNonNull(getDialog()).dismiss();

                                                                 }

                                                             }
                                                         });
                                             }
                                             else {
                                                 progressBar.setVisibility(View.GONE);
                                                 Snackbar.make(fragment.getView(),"Failed to Create GroupChat",Snackbar.LENGTH_INDEFINITE).show();
                                                 Objects.requireNonNull(getDialog()).dismiss();
                                             }
                                         }
                                     });

                         }
                     }
                 });


            }


        }
    };
    private  View.OnClickListener cancelGroup=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Objects.requireNonNull(getDialog()).dismiss();
        }
    };
}
