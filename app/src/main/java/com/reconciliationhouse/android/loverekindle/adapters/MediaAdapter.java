package com.reconciliationhouse.android.loverekindle.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.ItemMediaCardLayoutBinding;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.utils.Listeners.MediaItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private List<MediaItem> mMediaItems;
    private MediaItemClickListener mMediaItemClickListener;

    public MediaAdapter(MediaItemClickListener mediaItemClickListener) {
        this.mMediaItemClickListener = mediaItemClickListener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new MediaViewHolder((ItemMediaCardLayoutBinding) DataBindingUtil.inflate(inflater,
                R.layout.item_media_card_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        if (mMediaItems != null) {
            MediaItem mediaItem = mMediaItems.get(position);

            holder.mBinding.setMedia(mediaItem);
        }
    }

    @Override
    public int getItemCount() {
        if (mMediaItems != null) return mMediaItems.size();
        else return 0;
    }

    public void setMediaItems(@NonNull List<MediaItem> mediaItems) {
        if (this.mMediaItems != null) {
            this.mMediaItems.clear();
            this.mMediaItems.addAll(mediaItems);
            notifyDataSetChanged();
            return;
        }
        this.mMediaItems = new ArrayList<>();
        this.mMediaItems.addAll(mediaItems);
        notifyDataSetChanged();
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemMediaCardLayoutBinding mBinding;

        MediaViewHolder(@NonNull ItemMediaCardLayoutBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mMediaItemClickListener.onMediaItemClick(mBinding.getMedia().getId(), mBinding.getMedia().getCategory());
        }
    }
}
