package com.reconciliationhouse.android.loverekindle.adapters.bindingadapters;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;

import com.google.android.material.textview.MaterialTextView;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.reconciliationhouse.android.loverekindle.models.MediaReview;
import com.squareup.picasso.Picasso;

public abstract class MediaItemBindingAdapters {

    @BindingAdapter("android:image")
    public static void loadImage(ImageView imageView, MediaItem media) {
        if (media.getImage_url() != null) Picasso.get().load(media.getImage_url()).into(imageView);
        else imageView.setImageResource(R.drawable.opened_book);
    }

    @BindingAdapter("android:mediaType")
    public static void setMediaType(TextView tv, MediaItem media) {
        if (media.getType() != null) tv.setText(media.getType().toString());
    }

    @BindingAdapter("android:price")
    public static void setPrice(TextView tv, MediaItem media) {
        Context tvContext = tv.getContext();
        if (media != null && media.getPrice() != 0 && media.getPrice() > 1) {
            tv.setText(tvContext.getResources().getString(R.string.price_with_sign, media.getPrice()));
            tv.setTextColor(tvContext.getResources().getColor(R.color.deeper_grey));
        } else {
            tv.setText(R.string.txt_free);
            tv.setTextColor(tvContext.getResources().getColor(R.color.colorPrimary));
        }
    }

    @BindingAdapter("android:userReviewLink")
    public static void setUserReviewLink(TextView view, MediaReview review) {
        Context vContext = view.getContext();
        if (review == null || review.getReview().isEmpty()) {
            view.setText("Write your review for this book");
        } else {
            // TODO: Write procedures here to get user review and set it in the textView
            view.setText("Edit your review");
        }
    }

    @BindingAdapter("android:bookButton")
    public static void setButtonText(Button button, MediaItem.MediaType mediaType) {
        if (mediaType != null && (mediaType == MediaItem.MediaType.AUDIO || mediaType == MediaItem.MediaType.SERMON))
            button.setText(R.string.listen_now);
        else button.setText(R.string.read_now);
    }

    @BindingAdapter("android:downloaded")
    public static void isDownloaded(TextView view, boolean downloaded) {
        Context vContext = view.getContext();
        if (downloaded) {
            final Drawable downloadedIcon = vContext.getResources().getDrawable(R.drawable.ic_done);
            downloadedIcon.setBounds(0, 0, 96, 96);
            view.setCompoundDrawables(null, downloadedIcon, null, null);
            view.setCompoundDrawablePadding(4);
            view.setText("Downloaded");
        }
    }
}
