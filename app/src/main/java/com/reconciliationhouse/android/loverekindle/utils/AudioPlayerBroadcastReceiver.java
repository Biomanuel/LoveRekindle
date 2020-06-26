package com.reconciliationhouse.android.loverekindle.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.ImageButton;

public class AudioPlayerBroadcastReceiver extends BroadcastReceiver {

    private final MediaPlayer mMediaPlayer;
    private final ImageButton mPlayButton;

    public AudioPlayerBroadcastReceiver(MediaPlayer mediaPlayer, ImageButton playButton) {
        super();
        mMediaPlayer = mediaPlayer;
        mPlayButton = playButton;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                    if (mMediaPlayer.isPlaying()) mPlayButton.callOnClick();
                    break;
            }
        }
    }
}
