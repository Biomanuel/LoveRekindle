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
import com.reconciliationhouse.android.loverekindle.utils.Listeners;

import java.util.List;


public class LibraryMediaAdapter extends RecyclerView.Adapter<LibraryMediaAdapter.MediaViewHolder> {
    private List<MediaItem> mMediaItems;
    private Listeners.MediaItemClickListener mMediaItemClickListener;

    public LibraryMediaAdapter(Listeners.MediaItemClickListener mediaItemClickListener) {
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
        if (mMediaItems != null) {
            if (mMediaItems.size() > 4) {
                return 4;
            } else {

            }
            return mMediaItems.size();
        } else return 0;

    }

    public void setMediaItems(@NonNull List<MediaItem> mediaItems) {
        if (mediaItems.size() > 0) {
            if (mMediaItems != null) mMediaItems.clear();
            mMediaItems = mediaItems;
            notifyDataSetChanged();
        }
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
