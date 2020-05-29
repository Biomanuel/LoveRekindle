package com.reconciliationhouse.android.loverekindle.adapters.bindingadapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.models.MediaReview;

public abstract class MediaReviewBindingAdapter {

    @BindingAdapter("android:votes")
    public static void setVote(TextView view, MediaReview review) {
        Context vContext = view.getContext();
        view.setText(vContext.getResources().getString(R.string.votes_text, 224));
    }
}
