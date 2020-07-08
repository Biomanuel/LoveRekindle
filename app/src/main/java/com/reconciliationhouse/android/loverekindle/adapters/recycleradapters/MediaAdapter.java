package com.reconciliationhouse.android.loverekindle.adapters.recycleradapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.databinding.ItemMediaCardLayoutBinding;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.utils.ItemAnimation;
import com.reconciliationhouse.android.loverekindle.utils.Listeners.MediaItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private List<MediaItem> mMediaItems;
    private MediaItemClickListener mMediaItemClickListener;
    private int animation_type = 0;

    public MediaAdapter(MediaItemClickListener mediaItemClickListener) {
        this.mMediaItemClickListener = mediaItemClickListener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new MediaViewHolder(ItemMediaCardLayoutBinding.inflate(inflater,
                parent, false));
    }

    private int lastPosition = -1;

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

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        if (mMediaItems != null) {
            MediaItem mediaItem = mMediaItems.get(position);

            holder.mBinding.setMedia(mediaItem);
            setAnimation(holder.itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        if (mMediaItems != null) return mMediaItems.size();
        else return 0;
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

    private boolean on_attach = true;

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, animation_type);
            lastPosition = position;
        }
    }
}
