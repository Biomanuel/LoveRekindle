package com.reconciliationhouse.android.loverekindle.adapters.counsellor;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.recyclerview.widget.RecyclerView;


import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.CousellorRequestItemBinding;
import com.reconciliationhouse.android.loverekindle.dialog.ChatTypeDialog;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class CounsellorRequestAdapters extends RecyclerView.Adapter<CounsellorRequestAdapters.RequestHolder> {
    private List<UserModel> mList;
    private Fragment fragment;


    public CounsellorRequestAdapters(Fragment fragment) {
        this.fragment=fragment;
        //this.mList = mList;
    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new CounsellorRequestAdapters.RequestHolder((CousellorRequestItemBinding) DataBindingUtil.inflate(inflater,
                R.layout.cousellor_request_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequestHolder holder, int position) {
        if (mList != null) {
            final UserModel item = mList.get(position);
            holder.itemBinding.counsellorEmail.setText(item.getEmail());
            holder.itemBinding.counsellorUsername.setText(item.getName());
            if (item.getProfileImageUrl()==null){
                holder.itemBinding.counsellorProfileImage.setImageResource(R.drawable.profile_picture_placeholder);
            }
            Picasso.get().load(item.getProfileImageUrl()).into(holder.itemBinding.counsellorProfileImage);

            holder.itemBinding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(item.getName(),item.getEmail(),item.getUserId(),item.getCategory(),item.getProfileImageUrl());


                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public void setCounsellors(@NonNull List<UserModel> mediaItems) {
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

    public class RequestHolder extends RecyclerView.ViewHolder {
        CousellorRequestItemBinding itemBinding;

        public RequestHolder(@NonNull CousellorRequestItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;



        }


    }

         private void showDialog(String counsellorName, final String counsellorEmail, String id, UserModel.Category category, String profileImageUrl) {

        FragmentManager fm = fragment.getActivity().getSupportFragmentManager();
        ChatTypeDialog custom = new ChatTypeDialog(fragment,counsellorName, counsellorEmail,id,category,profileImageUrl);
        custom.show(fm,"");


    }



}