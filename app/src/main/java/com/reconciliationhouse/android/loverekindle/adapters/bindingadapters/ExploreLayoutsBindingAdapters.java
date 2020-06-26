package com.reconciliationhouse.android.loverekindle.adapters.bindingadapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.models.MediaReview;
import com.reconciliationhouse.android.loverekindle.ui.mediapreview.DialogMediaPreview;
import com.reconciliationhouse.android.loverekindle.ui.mediapreview.MediaPreviewViewModel;

import java.util.List;

public abstract class ExploreLayoutsBindingAdapters {

    @BindingAdapter(value = {"isActive", "activeColor"}, requireAll = false)
    public static void viewIsActive(View view, boolean state, int colorRef) {
        Context vContext = view.getContext();
//        int color = (colorRef == 0) ? vContext.getResources().getColor(R.color.colorPrimary) : vContext.getResources().getColor(colorRef);
        if (state) {
            view.setEnabled(true);
            if (view instanceof TextView)
                ((TextView) view).setTextColor(colorRef);
        } else {
            view.setEnabled(false);
            if (view instanceof TextView)
                ((TextView) view).setTextColor(vContext.getResources().getColor(R.color.passiveGrey));
        }
    }

    @BindingAdapter("app:isVisible")
    public static void setVisibility(View view, boolean isVisible) {
        if (isVisible) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);
    }

    @BindingAdapter("android:reviewTitle")
    public static void setReviewsSectionTitle(TextView view, List<MediaReview> reviews) {
        Context vContext = view.getContext();
        int count = (reviews != null) ? reviews.size() : 0;
        view.setText(vContext.getResources().getString(R.string.txt_reviews_title, count));
    }

    @BindingAdapter(value = {"viewModel", "purpose"}, requireAll = false)
    public static void setDialogTitle(TextView view, MediaPreviewViewModel viewModel, DialogMediaPreview.Assignment purpose) {
        Context vContext = view.getContext();
        switch (purpose) {
            case MORE_REVIEWS:
                int reviewsCount = (viewModel.getReviews().getValue() != null) ? viewModel.getReviews().getValue().size() : 0;
                view.setText(vContext.getResources().getString(R.string.txt_reviews_title, reviewsCount));
                break;
            case RELATED_MEDIA:
                int relatedMediaCount = (viewModel.getRelatedMedia().getValue() != null) ? viewModel.getRelatedMedia().getValue().size() : 0;
                view.setText(vContext.getResources().getString(R.string.txt_related_media_title, relatedMediaCount));
                break;
            case EDIT_REVIEW:
                view.setText(vContext.getResources().getString(R.string.txt_edit_review_title));
                break;
            case MORE_ABOUT:
                view.setText(vContext.getResources().getString(R.string.txt_about_book_title));
                break;
        }
    }

    @BindingAdapter("android:purpose")
    public static void setDrawableSrc(ImageButton view, DialogMediaPreview.Assignment purpose) {
        if (purpose == DialogMediaPreview.Assignment.EDIT_REVIEW)
            view.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.ic_close));
    }

}
