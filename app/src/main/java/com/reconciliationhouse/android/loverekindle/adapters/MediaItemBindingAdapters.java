package com.reconciliationhouse.android.loverekindle.adapters;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.squareup.picasso.Picasso;

public abstract class MediaItemBindingAdapters {

    @BindingAdapter("app:image")
    public static void loadImage(ImageView imageView, MediaItem media) {
        if (media.getImage_url() != null) Picasso.get().load(media.getImage_url()).into(imageView);
        else imageView.setImageResource(R.drawable.opened_book);
    }

    @BindingAdapter("app:mediaType")
    public static void setMediaType(TextView tv, MediaItem media) {
        if (media.getType() != null) tv.setText(media.getType().toString());
    }

    @BindingAdapter("app:price")
    public static void setPrice(TextView tv, MediaItem media) {
        Context tvContext = tv.getContext();
        if (media.getPrice() != 0 && media.getPrice() > 1)
            tv.setText(tvContext.getResources().getString(R.string.price_with_sign, media.getPrice()));
        else {
            tv.setText(R.string.txt_free);
            tv.setTextColor(tvContext.getResources().getColor(R.color.colorPrimary));
        }
    }
}
