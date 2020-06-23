package com.reconciliationhouse.android.loverekindle.adapters.counsellor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.CousellorRequestItemBinding;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AllCounsellorAdapter extends RecyclerView.Adapter<AllCounsellorAdapter.AllHolder>  {
    private List<UserModel> mList;
    private Fragment fragment;

    public AllCounsellorAdapter(Fragment fragment) {
        this.fragment=fragment;
        //this.mList = mList;
    }

    @NonNull
    @Override
    public AllHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new AllCounsellorAdapter.AllHolder((CousellorRequestItemBinding) DataBindingUtil.inflate(inflater,
                R.layout.cousellor_request_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllHolder holder, int position) {
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
    public class AllHolder extends RecyclerView.ViewHolder {
        CousellorRequestItemBinding itemBinding;

        public AllHolder(@NonNull CousellorRequestItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;



        }


    }
}
