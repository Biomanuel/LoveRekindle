package com.reconciliationhouse.android.loverekindle.utils;

import com.reconciliationhouse.android.loverekindle.models.MediaItem;

public abstract class Listeners {

    public interface MediaItemClickListener {
        void onMediaItemClick(String mediaId, String category);
    }

}
