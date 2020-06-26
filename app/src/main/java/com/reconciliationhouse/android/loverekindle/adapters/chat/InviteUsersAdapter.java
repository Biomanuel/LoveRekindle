package com.reconciliationhouse.android.loverekindle.adapters.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.InviteFriendItemBinding;
import com.reconciliationhouse.android.loverekindle.models.ChatItem;
import com.reconciliationhouse.android.loverekindle.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class InviteUsersAdapter extends RecyclerView.Adapter<InviteUsersAdapter.ItemHolder> {
    private List<UserModel> mList;

    private String groupName;
    private ChatItem.ChatType chatType;
    private String imageUrl;

    public InviteUsersAdapter(String groupName, ChatItem.ChatType chatType, String imageUrl) {
        this.groupName = groupName;
        this.chatType = chatType;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new InviteUsersAdapter.ItemHolder((InviteFriendItemBinding) DataBindingUtil.inflate(inflater,
                R.layout.invite_friend_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, int position) {
        if (mList!=null){
            final UserModel item = mList.get(position);
            holder.binding.counsellorEmail.setText(item.getEmail());
            holder.binding.counsellorUsername.setText(item.getName());
            holder.binding.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), " I was clicked", Toast.LENGTH_SHORT).show();
                   createDialog(item.getName(),item.getEmail(),holder.binding.cardView.getContext());

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public void setList(@NonNull List<UserModel> mediaItems) {
        if (this.mList != null) {
            this.mList.clear();
            this.mList.addAll(mediaItems);
            notifyDataSetChanged();
            return;
        }
        this.mList = new ArrayList<>();
        this.mList.addAll(mediaItems);
        notifyDataSetChanged();
    }

     class ItemHolder extends RecyclerView.ViewHolder {
        InviteFriendItemBinding binding;
        public ItemHolder(@NonNull InviteFriendItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    private void createDialog(String name, final String email, final Context context){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("");
        builder.setMessage("Do you Want to invite "+name+" to the chat");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                FirebaseFirestore  db=FirebaseFirestore.getInstance();

                final DocumentReference documentReference=db.collection("User").document(email).collection("chat").document(groupName);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot.exists()) {
                               Toast.makeText(context,"User is already Added", Toast.LENGTH_SHORT).show();
                        } else {
                            documentReference.set(new ChatItem(groupName, ChatItem.ChatType.Group_Chat,imageUrl)).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(context, "User Successfully Added to the Chat", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                            else {
                                                Toast.makeText(context, "User Failed to be Added", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                            );
                        }

                    }

                    

                });
//


            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.cancel();

            }
        });
        builder.show();

    }
}
