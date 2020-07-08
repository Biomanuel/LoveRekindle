package com.reconciliationhouse.android.loverekindle.ui.mediareader;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.data.LocalMedia;
import com.reconciliationhouse.android.loverekindle.databinding.FragmentAudioPlayerBinding;
import com.reconciliationhouse.android.loverekindle.ui.mediapreview.MediaPreviewViewModel;
import com.reconciliationhouse.android.loverekindle.utils.AudioPlayerBroadcastReceiver;
import com.reconciliationhouse.android.loverekindle.utils.MusicService;
import com.reconciliationhouse.android.loverekindle.utils.MusicUtils;
import com.reconciliationhouse.android.loverekindle.utils.Tools;

import java.io.IOException;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AudioPlayerFragment extends Fragment implements View.OnClickListener, AudioManager.OnAudioFocusChangeListener, MusicService.Callbacks {

    public static final String MEDIA_ID_KEY = "mediaId";
    private static final String TAG = AudioPlayerFragment.class.getSimpleName();
    private static final String NOTIFICATION_CHANNEL_ID = "AUDIO_Notification_Channel";
    private NotificationManager mNotifyManager;

    // Media Player
    private MediaPlayer mMediaPlayer;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();

    //private SongsManager songManager;
    private MusicUtils utils;
    private FragmentAudioPlayerBinding mBinding;
    private MediaPreviewViewModel mViewModel;
    private boolean initialStage = true;
    private boolean isPlaying;
    private LocalMedia mLocalMedia;
    private int startPos = 0;
    private AudioPlayerBroadcastReceiver mReceiver;
    private IntentFilter mFilter = new IntentFilter();
    private AudioManager mAudioManager;
    private boolean repeat = false;
    private MediaSessionCompat mMediaSession;
    private MusicService mService;
    private Intent serviceIntent;

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {

        public void run() {
            if (true) {
                long totalDuration = mMediaPlayer.getDuration();
                long currentDuration = mMediaPlayer.getCurrentPosition();

                // Displaying Total Duration time
                mBinding.tvSongTotalDuration.setText(utils.milliSecondsToTimer(totalDuration));
                // Displaying time completed playing
                mBinding.tvSongCurrentDuration.setText(utils.milliSecondsToTimer(currentDuration));

                // Updating progress bar
                if (totalDuration > 200) {
                    int progress = (int) (utils.getProgressSeekBar(currentDuration, totalDuration));
                    mBinding.seekSongProgressbar.setProgress(progress);
                } else mBinding.seekSongProgressbar.setProgress(2);
                // Running this thread after 10 milliseconds
                if (mMediaPlayer.isPlaying()) {
                    mHandler.postDelayed(this, 100);
                }
            }
        }
    };

    public AudioPlayerFragment() {
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Toast.makeText(requireContext(), "onServiceConnected called", Toast.LENGTH_SHORT).show();
            // We've binded to LocalService, cast the IBinder and get LocalService instance
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            mService = binder.getServiceInstance(); //Get instance of your service!
            mService.registerClient(AudioPlayerFragment.this); //Activity register in the service as client for callabcks!
            mBinding.btPlay.setEnabled(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Toast.makeText(requireContext(), "onServiceDisconnected called", Toast.LENGTH_SHORT).show();
            Toast.makeText(requireContext(), "Service disconnected", Toast.LENGTH_SHORT).show();
            mBinding.btPlay.setEnabled(false);
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) repeat = savedInstanceState.getBoolean("Repeat");

        getAndSetupWithViewModel();

        mAudioManager = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);

        mReceiver = new AudioPlayerBroadcastReceiver(mMediaPlayer, mBinding.btPlay);

        mFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

        requireActivity().registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("Repeat", repeat);
    }

    private void getAndSetupWithViewModel() {
        assert getParentFragment() != null;
        try {
            mViewModel = new ViewModelProvider(getParentFragment()).get(MediaPreviewViewModel.class);
        } catch (RuntimeException e) {
            mViewModel = new ViewModelProvider(requireActivity()).get(MediaPreviewViewModel.class);
            Log.e(TAG, "onActivityCreated: Unable to create viewmodel form ParentFragment");
        } catch (Exception err) {
            mViewModel = new ViewModelProvider(requireActivity()).get(MediaPreviewViewModel.class);
            Log.e(TAG, "onActivityCreated: Unable to create viewmodel form ParentFragment");
        }

        mViewModel.getLocalMedia().observe(getViewLifecycleOwner(), new Observer<LocalMedia>() {
            @Override
            public void onChanged(@NonNull LocalMedia media) {
                mLocalMedia = media;
                startPos = media.getCurrent_position();
                if (initialStage && !mMediaPlayer.isPlaying()) onResume();
            }
        });
        mBinding.setViewModel(mViewModel);
    }

//    private void initToolbar() {
//        mBinding.toolbar.setNavigationIcon(R.drawable.ic_home_black_24dp);
//        setSupportActionBar(mBinding.toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        Tools.setSystemBarColor(this, R.color.grey_1000);
//    }

    @Override
    public void onResume() {
        //startPlayingImmediately();

        if (mViewModel.getLocalMedia().getValue() != null && initialStage) {
            Toast.makeText(requireContext(), "Media Ready!", Toast.LENGTH_SHORT).show();
            updateTimerAndSeekbar(mLocalMedia.getCurrent_position());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.btPlay.setEnabled(true);
                    mBinding.btPlay.callOnClick();
                }
            }, 300);
        }
        if (mMediaPlayer.isPlaying())
            mBinding.btPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        else mBinding.btPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow));
        super.onResume();
    }

    private void startPlayingImmediately() {
        if (!isPlaying) {
            if (initialStage) {
                final ProgressDialog progressDialog = new ProgressDialog(requireActivity());
                progressDialog.setMessage("Please Wait!");
                progressDialog.show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //I delayed the playing because it wasn't getting prepared, when i try to start
                        //immediately but if i should wait a little and click play manually it will start.
//                        new Player(progressDialog).execute(mediaUrl);
                        new Player(progressDialog).execute(mViewModel.getLocalMedia().getValue().getMedia_uri());
                        if (mMediaPlayer != null)
                            updateTimerAndSeekbar(AudioPlayerFragment.this.mMediaPlayer.getCurrentPosition());
                    }
                }, 3000);
            }
        }
    }

    private void initComponent() {

        // set Progress bar values
        mBinding.seekSongProgressbar.setProgress(0);
        mBinding.seekSongProgressbar.setMax(MusicUtils.MAX_PROGRESS);

        // Media Player
        if (mService.getMediaPlayer() == null) {
            mMediaPlayer = mService.createMediaPlayer();
        } else mMediaPlayer = mService.getMediaPlayer();

        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (Exception e) {
            Snackbar.make(mBinding.parentView, "Cannot load audio file", Snackbar.LENGTH_SHORT).show();
            Log.e(TAG, "initComponent.mMediaPlayer.setDataSource: ", e);
        }

        utils = new MusicUtils();
        // Listeners
        mBinding.seekSongProgressbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // remove message Handler from updating progress bar
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mMediaPlayer.getDuration();
                int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                mMediaPlayer.seekTo(currentPosition);

                // update timer progress again
                mHandler.post(mUpdateTimeTask);
            }
        });

//        updateTimerAndSeekbar();
    }

    /**
     * Play button click event plays a song and changes button to pause image
     * pauses a song and changes button to play image
     */
    private void pausePlayAction() {
        int result = mAudioManager.requestAudioFocus(AudioPlayerFragment.this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // check for already playing
            if (!isPlaying) {
                if (initialStage) {
//                        new Player().execute(mediaUrl);
                    // if (mService.isRunning()) mService.stopSelf();
                    requireActivity().startService(new Intent(requireContext(), MusicService.class));
                    mMediaPlayer.seekTo(startPos);
                    new Player().execute(mViewModel.getLocalMedia().getValue().getMedia_uri());
                    mBinding.btPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                    mMediaSession.setActive(true);
                } else {
                    mMediaPlayer.start();
                    // Changing button image to pause button
                    mBinding.btPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                    // Updating progress bar
                    mHandler.post(mUpdateTimeTask);
                }
                isPlaying = true;
//                    mHandler.post(mUpdateTimeTask);
            } else {
                mBinding.btPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow));
                mMediaPlayer.pause();
                mViewModel.saveCurrentPosition(mMediaPlayer.getCurrentPosition());
                isPlaying = false;
            }
        } else {
            Toast.makeText(requireContext(), "Sorry! Media can't play now.", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAudioPlayerBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(this);
        mBinding.setControlsClickListener(this);

        serviceIntent = new Intent(requireContext(), MusicService.class);
        mService = MusicService.getInstance();
        mMediaSession = new MediaSessionCompat(requireContext(), TAG);

        Bundle mediaMetaBundle = new Bundle();
//        MediaMetadataCompat mediaMetadataCompat = new MediaMetadataCompat(mediaMetaBundle);

//        initToolbar();
        initComponent();
        Tools.setSystemBarColor(requireActivity(), R.color.grey_90);
        return mBinding.getRoot();
    }


    private boolean toggleButtonColor(ImageButton bt) {
        String selected = (String) bt.getTag(bt.getId());
        if (selected != null) { // selected
            bt.setColorFilter(getResources().getColor(R.color.grey_90), PorterDuff.Mode.SRC_ATOP);
            bt.setTag(bt.getId(), null);
            return false;
        } else {
            bt.setTag(bt.getId(), "selected");
            bt.setColorFilter(getResources().getColor(R.color.red_500), PorterDuff.Mode.SRC_ATOP);
            return true;
        }
    }

    private void updateTimerAndSeekbar(long currentDuration) {
        long totalDuration = mMediaPlayer.getDuration();

        // Displaying Total Duration time
        mBinding.tvSongTotalDuration.setText(utils.milliSecondsToTimer(totalDuration));
        // Displaying time completed playing
        mBinding.tvSongCurrentDuration.setText(utils.milliSecondsToTimer(currentDuration));

        // Updating progress bar
        if (totalDuration > 200) {
            int progress = (int) (utils.getProgressSeekBar(currentDuration, totalDuration));
            mBinding.seekSongProgressbar.setProgress(progress);
        } else mBinding.seekSongProgressbar.setProgress(2);
        // Running this thread after 10 milliseconds
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //TODO: inflate the right optionsMenu
    }

    // stop player when destroy

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            finish();
        } else {
            Snackbar.make(mBinding.parentView, item.getTitle(), Snackbar.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        super.onDestroy();
        //if (mMediaPlayer != null) mMediaPlayer.release();
    }

    @Override
    public void onDetach() {
        mMediaSession.setActive(false);
        super.onDetach();
    }

    @Override
    public void onClick(final View v) {

        //TODO: Change this buttons and their function to suit the player
        int id = v.getId();
        switch (id) {
            case R.id.bt_repeat: {
                toggleButtonColor((ImageButton) v);
                repeat = !repeat;
                String repState = repeat ? "on!" : "Off!";
                Snackbar.make(mBinding.parentView, "Repeat is now " + repState, Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.bt_timer: {
                toggleButtonColor((ImageButton) v);
                Snackbar.make(mBinding.parentView, "Timer", Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.bt_skip: {
                skipAudio((ImageButton) v);
                break;
            }
            case R.id.bt_backward: {
                rewindAudio((ImageButton) v);
                break;
            }
            case R.id.bt_play:
                pausePlayAction();
                break;
            case R.id.bt_close:
                NavHostFragment.findNavController(this).navigateUp();
                break;
            case R.id.bt_share:
                Snackbar.make(mBinding.parentView, "You can't share this media yet", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    private void skipAudio(final ImageButton v) {
        toggleButtonColor(v);
        int currentPos = mMediaPlayer.getCurrentPosition();
        mMediaPlayer.seekTo(currentPos + 5000);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setColorFilter(getResources().getColor(R.color.grey_50), PorterDuff.Mode.SRC_ATOP);
            }
        }, 1000);
    }

    private void rewindAudio(final ImageButton v) {
        toggleButtonColor(v);
        int currentPos = mMediaPlayer.getCurrentPosition();
        int pos = Math.max(currentPos - 5000, 0);
        mMediaPlayer.seekTo(pos);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setColorFilter(getResources().getColor(R.color.grey_50), PorterDuff.Mode.SRC_ATOP);
            }
        }, 1000);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Sets what should happen when audio focus is lost to another app or granted by the android system
     */
    @Override
    public void onAudioFocusChange(int focusChange) {
        assert mMediaPlayer != null;
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                mMediaPlayer.pause();
                mBinding.btPlay.setBackgroundResource(R.drawable.ic_play_arrow);
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                mMediaPlayer.setVolume(0.1f, 0.1f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                if (isPlaying) {
                    mMediaPlayer.pause();
                    mBinding.btPlay.setBackgroundResource(R.drawable.ic_play_arrow);
                    //isPlaying = false;
                }
                break;
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                mMediaPlayer.start();
                isPlaying = true;
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                if (isPlaying && !mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                    mMediaPlayer.setVolume(1, 1);
                    isPlaying = true;
                }
                break;
        }
    }

    public void createNotificationChannel() {
        mNotifyManager = (NotificationManager)
                requireContext().getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void updateClient(long data) {

    }

    /**
     * preparing mediaplayer will take sometime to buffer the content so prepare it inside the background thread and starting it on UI thread.
     *
     * @author piyush
     * <p>
     * Basically: This Player AsyncTask Class is used to fetch the audio from the internet.
     */
    class Player extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        public Player() {
            this(new ProgressDialog(requireActivity()));
        }

        public Player(ProgressDialog progressDialog) {
            this.progress = progressDialog;

            mMediaPlayer.setOnCompletionListener(
                    new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (!repeat) {
                                isPlaying = false;
                                mMediaPlayer.seekTo(0);
                                mBinding.btPlay.setBackgroundResource(R.drawable.ic_play_arrow);
                                mMediaPlayer.pause();
                                updateTimerAndSeekbar(0);
                                mViewModel.saveCurrentPosition(0);
                                initialStage = true;
                                mService.stopSelf();
                                //mMediaPlayer.stop();
                                //mMediaPlayer.reset();
                            } else {
                                mMediaPlayer.seekTo(0);
                                updateTimerAndSeekbar(AudioPlayerFragment.this.mMediaPlayer.getCurrentPosition());
                                mMediaPlayer.start();
                            }
                        }
                    }
            );

            mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {

                }
            });

        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {

                mMediaPlayer.setDataSource(params[0]);

                mMediaPlayer.prepare();
                prepared = true;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                Log.d("IllegarArgument", e.getMessage());
//                Toast.makeText(AudioPlayerFragment.this, e.getMessage(), Toast.LENGTH_LONG).show();
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (!result) {
                Toast.makeText(requireContext(), "Failed to prepare player. Press Play", Toast.LENGTH_LONG).show();
                this.progress.dismiss();
            }
            Log.w("Prepared", "//" + result);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            this.progress.setMessage("Buffering...");
            if (!this.progress.isShowing()) this.progress.show();
            this.progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    progress.dismiss();
                }
            });

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.e(TAG, "onPrepared: MediaPlayer is Prepared");
                    if (progress.isShowing()) {
                        progress.cancel();
                    }

                    // remove message Handler from updating progress bar
                    mHandler.removeCallbacks(mUpdateTimeTask);

                    mMediaPlayer.start();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        mMediaPlayer.seekTo(startPos, MediaPlayer.SEEK_PREVIOUS_SYNC);
                    } else mMediaPlayer.seekTo(startPos);

                    // update timer progress again
                    mHandler.post(mUpdateTimeTask);

                    mBinding.btPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                    mHandler.post(mUpdateTimeTask);
                    initialStage = false;
                }
            });
        }
    }

    public class NotifiacationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
