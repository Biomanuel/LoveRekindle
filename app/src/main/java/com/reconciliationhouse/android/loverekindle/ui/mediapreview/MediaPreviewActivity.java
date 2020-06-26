package com.reconciliationhouse.android.loverekindle.ui.mediapreview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.reconciliationhouse.android.loverekindle.R;
import com.reconciliationhouse.android.loverekindle.repository.LocalMediaRepository;
import com.reconciliationhouse.android.loverekindle.repository.MediaRepo;
import com.reconciliationhouse.android.loverekindle.repository.UserRepo;

public class MediaPreviewActivity extends AppCompatActivity {

    public static final String MEDIA_ID_KEY = "MediaId";
    public static final String MEDIA_CATEGORY_KEY = "Category";
    public static final String START_MEDIA_PLAYER = "Start_Media_Player";
    /**
     * Optionally used with {FullScreen Intent} to carry a MediaDescription to
     * the {Fullscreen Activity}, speeding up the screen rendering
     * while the {@link android.support.v4.media.session.MediaControllerCompat} is connecting.
     */
    public static final String EXTRA_CURRENT_MEDIA_DESCRIPTION =
            "com.example.android.uamp.CURRENT_MEDIA_DESCRIPTION";
    private static int REQUEST_CODE = 1;
    private MediaPreviewViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_preview);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_CODE);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if (getIntent().getExtras() != null) {
            String mediaId = getIntent().getStringExtra(MEDIA_ID_KEY);
            String category = getIntent().getStringExtra(MEDIA_CATEGORY_KEY);
            // TODO: Use this when you're ready to open Media Player or PDF Reader with pending intent from notification
            Boolean startMediaPlayer = getIntent().getBooleanExtra(START_MEDIA_PLAYER, false);
            mViewModel = new ViewModelProvider(this, new MediaPreviewViewModel.Factory(this.getApplication(), mediaId, category)).get(MediaPreviewViewModel.class);
            if (startMediaPlayer) mViewModel.startMediaPlayerImmediately(true);

            Bundle args = new Bundle();
            args.putString(MEDIA_ID_KEY, mediaId);
            args.putString(MEDIA_CATEGORY_KEY, category);
            args.putBoolean(START_MEDIA_PLAYER, startMediaPlayer);
            navController.setGraph(navController.getGraph(), args);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            UserRepo.getInstance();
        } catch (NullPointerException e) {
            UserRepo.initializeWithUser(this);
        }
    }

    private void updateLocal() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.local_data_prefs), Context.MODE_PRIVATE);
        String localMediaCountKey = "LocalMediaCount";
        int localMediaCount = prefs.getInt(localMediaCountKey, 0);
        int allMediaListSize = MediaRepo.getInstance().getAllMedia().getValue().size();
        if (localMediaCount != allMediaListSize) {
            prefs.edit().putInt(localMediaCountKey, allMediaListSize).apply();
            //LocalMediaRepository.open(getApplication());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
        }
    }
}
