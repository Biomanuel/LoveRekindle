package com.reconciliationhouse.android.loverekindle.adapters.recycleradapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reconciliationhouse.android.loverekindle.databinding.TagLayoutBinding;
import com.reconciliationhouse.android.loverekindle.utils.Listeners;

import java.util.ArrayList;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private List<String> tags;
    private Listeners.MediaTagClickListener mTagClickListener;

    public TagsAdapter(Listeners.MediaTagClickListener listener) {
        this.mTagClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ViewHolder(TagLayoutBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (tags != null) holder.binding.setTag(tags.get(position));
    }

    public void setTags(List<String> tags) {
        if (this.tags != null) {
            this.tags.clear();
            this.tags.addAll(tags);
            notifyDataSetChanged();
            return;
        }
        this.tags = new ArrayList<>();
        this.tags.addAll(tags);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (tags == null) return 0;
        else return tags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TagLayoutBinding binding;

        public ViewHolder(@NonNull TagLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
