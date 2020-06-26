package com.reconciliationhouse.android.loverekindle.adapters.counsellor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.CousellorRequestItemBinding;
import com.reconciliationhouse.android.loverekindle.models.CounsellorModel;
import com.reconciliationhouse.android.loverekindle.models.UserModel;
import com.reconciliationhouse.android.loverekindle.ui.counsellor.ListOfRequestFragmentDirections;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CounsellorRegAdapters extends RecyclerView.Adapter<CounsellorRegAdapters.RequestHolder>{
    private List<CounsellorModel> mList;
    Fragment fragment;

    public CounsellorRegAdapters(Fragment fragment) {
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new CounsellorRegAdapters.RequestHolder((CousellorRequestItemBinding) DataBindingUtil.inflate(inflater,
                R.layout.cousellor_request_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequestHolder holder, int position) {
        final CounsellorModel item=mList.get(position);
        holder.itemBinding.counsellorEmail.setText(item.getCounsellorEmail());
        holder.itemBinding.counsellorUsername.setText(item.getCounsellorName());
        if (item.getCounsellorProfileImage()==null){
            holder.itemBinding.counsellorProfileImage.setImageResource(R.drawable.profile_picture_placeholder);
        }
        Picasso.get().load(item.getCounsellorProfileImage()).into(holder.itemBinding.counsellorProfileImage);

        holder.itemBinding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson=new Gson();
                String jsonString=gson.toJson(item);
                NavController controller= NavHostFragment.findNavController(fragment);
                ListOfRequestFragmentDirections.ActionListOfRequestFragmentToRequestDetailsFragment action=ListOfRequestFragmentDirections.actionListOfRequestFragmentToRequestDetailsFragment("");
                action.setRequestDetails(jsonString);
                controller.navigate(action);



            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public void setCounsellors(@NonNull List<CounsellorModel> modelList) {
        if (this.mList != null) {
            this.mList.clear();
            this.mList.addAll(modelList);
            notifyDataSetChanged();
            return;
        }
        this.mList = new ArrayList<>();
        this.mList.addAll(modelList);
        notifyDataSetChanged();
    }

    public class RequestHolder extends RecyclerView.ViewHolder {
        CousellorRequestItemBinding itemBinding;

        public RequestHolder(@NonNull CousellorRequestItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;



        }


    }
}
