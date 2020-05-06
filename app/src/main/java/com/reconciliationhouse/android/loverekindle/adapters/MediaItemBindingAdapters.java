package com.reconciliationhouse.android.loverekindle.adapters;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
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

    @BindingAdapter("android:userReview")
    public static void setUserReview(TextView view, String comment) {
        Context vContext = view.getContext();
        // TODO: Write procedures here to get user review and set it in the textView
        view.setText(R.string.single_review_comment);
        view.setTextColor(vContext.getResources().getColor(R.color.deeper_grey));
    }

    @BindingAdapter("android:bookButton")
    public static void setButtonText(Button button, MediaItem.MediaType mediaType) {
        if (mediaType != null && (mediaType == MediaItem.MediaType.AUDIO || mediaType == MediaItem.MediaType.SERMON))
            button.setText(R.string.listen_now);
        else button.setText(R.string.read_now);
    }
}
