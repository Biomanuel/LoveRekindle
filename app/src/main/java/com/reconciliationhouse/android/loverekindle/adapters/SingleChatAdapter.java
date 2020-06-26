package com.reconciliationhouse.android.loverekindle.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.gson.Gson;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.ChatTypeItemBinding;
import com.reconciliationhouse.android.loverekindle.databinding.CousellorRequestItemBinding;
import com.reconciliationhouse.android.loverekindle.models.ChatModel;
import com.reconciliationhouse.android.loverekindle.models.CounsellorModel;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.models.UserSender;
import com.reconciliationhouse.android.loverekindle.ui.chat.ChatCategoriesFragmentDirections;
import com.reconciliationhouse.android.loverekindle.ui.chat.ChatHomeFragmentDirections;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SingleChatAdapter extends RecyclerView.Adapter<SingleChatAdapter.ItemHolder> {
    private List<ChatModel> mList;


    public SingleChatAdapter(){

    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new SingleChatAdapter.ItemHolder((ChatTypeItemBinding) DataBindingUtil.inflate(inflater,
                R.layout.chat_type_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if (mList != null) {
            final ChatModel item = mList.get(position);
            holder.itemBinding.username.setText(item.getName());
//            Timestamp timestamp=item.getDateCreated();
//            Date date=new Date(item.getDateCreated()*1000);
//            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//            sfd.format(date);

            holder.itemBinding.dateCreated.setText(String.valueOf(item.getDateCreated()));
            if (item.getImageUrl()==null){
                holder.itemBinding.counsellorProfileImage.setImageResource(R.drawable.profile_picture_placeholder);
            }
            Picasso.get().load(item.getImageUrl()).into(holder.itemBinding.counsellorProfileImage);

            holder.itemBinding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    NavController controller = Navigation.findNavController(v);

                    Gson gson = new Gson();
//                    UserModel model=new UserModel(item.getId(),item.getName(),item.getImageUrl());
//                    String jsonString=gson.toJson(model);
//                    ChatHomeFragmentDirections.ActionChatHomeFragmentToChatFragment chat=ChatHomeFragmentDirections.actionChatHomeFragmentToChatFragment().setCounsellorData(jsonString);

//                    controller.navigate(chat);


                }
            });

        }


    }
    public void setCounsellors(@NonNull List<ChatModel> chatModelList) {
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
