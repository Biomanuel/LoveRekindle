package com.reconciliationhouse.android.loverekindle.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.HashMap;

import static com.google.common.primitives.Longs.tryParse;

public class MusicService extends Service {

    // Extra on MediaSession that contains the Cast device name currently connected to
    public static final String EXTRA_CONNECTED_CAST = "com.example.android.uamp.CAST_NAME";
    // The action of the incoming Intent indicating that it contains a command
    // to be executed (see {@link #onStartCommand})
    public static final String ACTION_CMD = "com.example.android.uamp.ACTION_CMD";
    // The key in the extras of the incoming Intent indicating the command that
    // should be executed (see {@link #onStartCommand})
    public static final String CMD_NAME = "CMD_NAME";
    // A value of a CMD_NAME key in the extras of the incoming Intent that
    // indicates that the music playback should be paused (see {@link #onStartCommand})
    public static final String CMD_PAUSE = "CMD_PAUSE";
    // A value of a CMD_NAME key that indicates that the music playback should switch
    // to local playback from cast playback.
    public static final String CMD_STOP_CASTING = "CMD_STOP_CASTING";
    private static final String TAG = LogHelper.makeLogTag(MusicService.class);
    // Delay stopSelf by using a handler.
    private static final int STOP_DELAY = 30000;
    private static MusicService INSTANCE;
    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    private static MediaSessionCompat mMediaSession;
    MediaSessionCompat mSession;
    private MediaSessionCompat.Token mToken;
    private MediaPlayer mMediaPlayer;
    private boolean running = false;

    public MusicService() {

    }

    public static MusicService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MusicService();
        }
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        // Instantiate new MediaSession object.
        configureMediaSession();
    }

    @Override
    public IBinder onBind(Intent intent) {
//        // Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mMediaPlayer == null) createMediaPlayer();
        running = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mMediaSession != null) mMediaSession.release();
        running = false;
        mMediaPlayer = null;
        INSTANCE = null;
    }

    private void configureMediaSession() {
        mMediaSession = new MediaSessionCompat(this, "MyMediaSession");

        // Overridden methods in the MediaSession.Callback class.
        mMediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonIntent) {
                Log.d(TAG, "onMediaButtonEvent called: " + mediaButtonIntent);
                KeyEvent ke = mediaButtonIntent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if (ke != null && ke.getAction() == KeyEvent.ACTION_DOWN) {
                    int keyCode = ke.getKeyCode();
                    Log.d(TAG, "onMediaButtonEvent Received command: " + ke);
                }
                return super.onMediaButtonEvent(mediaButtonIntent);
            }

            @Override
            public void onSkipToNext() {
                Log.d(TAG, "onSkipToNext called (media button pressed)");
                Toast.makeText(getApplicationContext(), "onSkipToNext called", Toast.LENGTH_SHORT).show();
                skipToNextPlaylistItem(); // Handle this button press.
                super.onSkipToNext();
            }

            @Override
            public void onSkipToPrevious() {
                Log.d(TAG, "onSkipToPrevious called (media button pressed)");
                Toast.makeText(getApplicationContext(), "onSkipToPrevious called", Toast.LENGTH_SHORT).show();
                skipToPreviousPlaylistItem(); // Handle this button press.
                super.onSkipToPrevious();
            }

            @Override
            public void onPause() {
                Log.d(TAG, "onPause called (media button pressed)");
                Toast.makeText(getApplicationContext(), "onPause called", Toast.LENGTH_SHORT).show();
                mpPause(); // Pause the player.
                super.onPause();
            }

            @Override
            public void onPlay() {
                Log.d(TAG, "onPlay called (media button pressed)");
                mpStart(); // Start player/playback.
                super.onPlay();
            }

            @Override
            public void onStop() {
                Log.d(TAG, "onStop called (media button pressed)");
                mpReset(); // Stop and/or reset the player.
                super.onStop();
            }
        });

        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setActive(true);
    }

    public void mpReset() {

    }

    public void mpStart() {

    }

    public void mpPause() {

    }

    public void skipToPreviousPlaylistItem() {

    }

    public void skipToNextPlaylistItem() {

    }

    void sendMetaData(@NonNull final HashMap<String, String> hm) {
        // Return if Bluetooth A2DP is not in use.
        if (!((AudioManager) getSystemService(Context.AUDIO_SERVICE)).isBluetoothA2dpOn()) return;

        MediaMetadataCompat metadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, hm.get("Title"))
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, hm.get("Album"))
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, hm.get("Artist"))
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, hm.get("Author"))
                .putString(MediaMetadataCompat.METADATA_KEY_COMPOSER, hm.get("Composer"))
                .putString(MediaMetadataCompat.METADATA_KEY_WRITER, hm.get("Writer"))
                .putString(MediaMetadataCompat.METADATA_KEY_DATE, hm.get("Date"))
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, hm.get("Genre"))
                .putLong(MediaMetadataCompat.METADATA_KEY_YEAR, tryParse(hm.get("Year")))
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, tryParse(hm.get("Raw Duration")))
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, tryParse(hm.get("Track Number")))
                .build();

        mMediaSession.setMetadata(metadata);
    }

    private void setPlaybackState(@NonNull final int stateValue) {
        PlaybackStateCompat state = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                        | PlaybackStateCompat.ACTION_STOP | PlaybackStateCompat.ACTION_PLAY_PAUSE)
                .setState(stateValue, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0)
                .build();

        mMediaSession.setPlaybackState(state);
    }

    public MediaPlayer createMediaPlayer() {
        if (mMediaPlayer == null) mMediaPlayer = new MediaPlayer();
        return mMediaPlayer;
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public MediaSessionCompat.Token getSessionToken() {
        return this.mToken;
    }

    public void setSessionToken(MediaSessionCompat.Token token) {
        mToken = token;
    }

    public boolean isRunning() {
        return running;
    }
}
