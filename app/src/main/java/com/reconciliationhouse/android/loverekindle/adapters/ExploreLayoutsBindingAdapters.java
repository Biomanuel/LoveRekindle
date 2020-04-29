package com.reconciliationhouse.android.loverekindle.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;
import com.squareup.picasso.Picasso;

public abstract class ExploreLayoutsBindingAdapters {

    @BindingAdapter("app:isActive")
    public static void viewIsActive(View view, boolean state) {
        Context vContext = view.getContext();
        if (state) {
            view.setEnabled(true);
            if (view instanceof TextView)
                ((TextView) view).setTextColor(vContext.getResources().getColor(R.color.colorPrimary));
        } else {
            view.setEnabled(false);
            if (view instanceof TextView)
                ((TextView) view).setTextColor(vContext.getResources().getColor(R.color.passiveGrey));
        }
    }
}
