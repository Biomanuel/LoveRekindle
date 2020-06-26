package com.reconciliationhouse.android.loverekindle.adapters.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.ChatTypeItemBinding;
import com.reconciliationhouse.android.loverekindle.models.ChatItem;
import com.reconciliationhouse.android.loverekindle.models.ChatModel;
import com.reconciliationhouse.android.loverekindle.ui.chat.ChatHomeFragmentDirections;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ItemHolder> {
    private List<ChatItem> mList;
    private ChatItem.ChatType chatType;

    public ChatListAdapter() {

    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ChatListAdapter.ItemHolder((ChatTypeItemBinding) DataBindingUtil.inflate(inflater,
                R.layout.chat_type_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        if (mList != null) {
            final ChatItem item = mList.get(position);

            if (mList.get(position).getChatType().equals(ChatItem.ChatType.Single_Chat)) {
                holder.itemBinding.username.setText(item.getName());
                String date = new SimpleDateFormat("hh:mm:aa").format(mList.get(position).getDateCreated());
                holder.itemBinding.dateCreated.setText(date);

                holder.itemBinding.dateCreated.setText(String.valueOf(item.getDateCreated()));
                if (item.getImageUrl() == null) {
                    holder.itemBinding.counsellorProfileImage.setImageResource(R.drawable.profile_picture_placeholder);
                }
                Picasso.get().load(item.getImageUrl()).into(holder.itemBinding.counsellorProfileImage);

                holder.itemBinding.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NavController controller = Navigation.findNavController(v);

                        Gson gson = new Gson();

                        ChatItem model = new ChatItem(item.getChatId(), ChatItem.ChatType.Single_Chat, item.getName(), item.getImageUrl());
                        String jsonString = gson.toJson(model);
                        ChatHomeFragmentDirections.ActionChatHomeFragmentToChatFragment chat = ChatHomeFragmentDirections.actionChatHomeFragmentToChatFragment().setCounsellorData(jsonString);

                        controller.navigate(chat);


                    }
                });
            }
            else  {
                holder.itemBinding.username.setText(item.getChatId());
                String date = new SimpleDateFormat("hh:mm:aa").format(mList.get(position).getDateCreated());
                holder.itemBinding.dateCreated.setText(date);

                holder.itemBinding.dateCreated.setText(String.valueOf(item.getDateCreated()));

                    holder.itemBinding.counsellorProfileImage.setImageResource(R.drawable.ic_males_group);


                holder.itemBinding.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        NavController controller = Navigation.findNavController(v);

                        Gson gson = new Gson();

                        ChatItem model = new ChatItem(item.getChatId(), ChatItem.ChatType.Group_Chat, item.getName(), item.getImageUrl());
                        String jsonString = gson.toJson(model);
                        ChatHomeFragmentDirections.ActionChatHomeFragmentToChatFragment chat = ChatHomeFragmentDirections.actionChatHomeFragmentToChatFragment().setCounsellorData(jsonString);

                        controller.navigate(chat);


                    }
                });
            }

        }
    }
    public void setChatItems(@NonNull List<ChatItem> chatModelList) {
        if (this.mList != null) {
            this.mList.clear();
            this.mList.addAll(chatModelList);
            notifyDataSetChanged();
            return;
        }
        this.mList = new ArrayList<>();
        this.mList.addAll(chatModelList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ChatTypeItemBinding itemBinding;

        public ItemHolder(@NonNull ChatTypeItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;


        }
    }
}
