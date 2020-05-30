package com.reconciliationhouse.android.loverekindle.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.reconciliationhouse.android.loverekindle.R;

import com.reconciliationhouse.android.loverekindle.models.Message;
import com.reconciliationhouse.android.loverekindle.models.UserDetails;

import com.reconciliationhouse.android.loverekindle.models.UserSender;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<Message>mList;

        public ChatAdapter(){


        }

    @Override
    public int getItemViewType(int position) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        UserSender userSender=mList.get(position).getUserSender();

        assert user != null;
        if (userSender.getName().equals(user.getDisplayName())){
            return  1;
        }
       else {
           return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view=inflater.inflate(R.layout.chat_receiver_layout,parent,false);
            return new ReceiverItemHolder(view);
        }
        else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view=inflater.inflate(R.layout.chat_sender_layout,parent,false);
            return new SenderItemHolder(view);
        }



    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()){
                case 0: {
                    UserSender senderItem = mList.get(position).getUserSender();
                    String message = mList.get(position).getMessage();
                    ReceiverItemHolder receiverItemHolder = (ReceiverItemHolder) holder;
                    receiverItemHolder.receiverMessage.setText(message);
                   if (senderItem.getProfileImageUrl()!=null){
                       Picasso.get().load(senderItem.getProfileImageUrl()).into(receiverItemHolder.receiverImage);
                   }
                   else {
                       receiverItemHolder.receiverImage.setImageResource(R.drawable.profile);
                   }
                }
                break;
                case 1:{

                    String message = mList.get(position).getMessage();
                    SenderItemHolder  senderItemHolder = (SenderItemHolder) holder;
                    senderItemHolder.senderMessage.setText(message);
                }
                break;
                default:
                    break;
            }


    }

    @Override
    public int getItemCount() {
          return mList.size();
    }
    public void setMessages(@NonNull List<Message> messages) {
        if (this.mList != null) {
            this.mList.clear();
            this.mList.addAll(messages);
            notifyDataSetChanged();
            return;
        }
        this.mList = new ArrayList<>();
        this.mList.addAll(messages);
        notifyDataSetChanged();
    }



}
class ReceiverItemHolder extends RecyclerView.ViewHolder {
    CircleImageView receiverImage;
    TextView senderMessage,receiverMessage;
    TextView date, time;

    public ReceiverItemHolder(@NonNull View itemView) {
        super(itemView);
        receiverImage=itemView.findViewById(R.id.circleImageView);
        receiverMessage=itemView.findViewById(R.id.receiver_message);
        date=itemView.findViewById(R.id.date);
        time=itemView.findViewById(R.id.time);


    }
}

class SenderItemHolder extends RecyclerView.ViewHolder {
    CircleImageView receiverImage;
    TextView senderMessage,receiverMessage;
    TextView date, time;

    public SenderItemHolder(@NonNull View itemView) {
        super(itemView);

        senderMessage=itemView.findViewById(R.id.sender_message);

        date=itemView.findViewById(R.id.date);
        time=itemView.findViewById(R.id.time);


    }
}
