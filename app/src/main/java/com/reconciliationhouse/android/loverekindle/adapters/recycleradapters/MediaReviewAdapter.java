package com.reconciliationhouse.android.loverekindle.adapters.recycleradapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reconciliationhouse.android.loverekindle.databinding.MediaReviewCardLayoutBinding;
import com.reconciliationhouse.android.loverekindle.models.MediaReview;
import com.reconciliationhouse.android.loverekindle.repository.MediaReviewRepo;
import com.reconciliationhouse.android.loverekindle.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;

public class MediaReviewAdapter extends RecyclerView.Adapter<MediaReviewAdapter.ReviewViewHolder> {

    private List<MediaReview> mReviews;

    public MediaReviewAdapter() {
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ReviewViewHolder(MediaReviewCardLayoutBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        if (mReviews != null) {
            MediaReview review = mReviews.get(position);

            holder.mBinding.setReview(review);
        }
    }

    @Override
    public int getItemCount() {
        if (mReviews != null) return mReviews.size();
        else return 0;
    }

    public void setReviews(@NonNull List<MediaReview> reviews) {
        if (this.mReviews != null) {
            this.mReviews.clear();
            this.mReviews.addAll(reviews);
            notifyDataSetChanged();
            return;
        }
        this.mReviews = new ArrayList<>();
        this.mReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MediaReviewCardLayoutBinding mBinding;

        ReviewViewHolder(@NonNull MediaReviewCardLayoutBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.btnSupportReview.setOnClickListener(this);
            mBinding.btnVoteDownReview.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == mBinding.btnSupportReview.getId()) {
                MediaReviewRepo.getInstance().voteReview(mBinding.getReview(), true);
            } else if (view.getId() == mBinding.btnVoteDownReview.getId()) {
                MediaReviewRepo.getInstance().voteReview(mBinding.getReview(), false);
            }
        }
    }
}
