package com.reconciliationhouse.android.loverekindle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;

import com.reconciliationhouse.android.loverekindle.utils.BlurBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OldAudioPlayer extends AppCompatActivity {

    LinearLayoutCompat play, pause, previous, next;
    TextView audioname;
    ImageView audio_image;
    TextView toolbar_title, duration, current;
    Toolbar audio_player_toolbar;
    String myaudioname, url;
    LinearLayout linearLayoutroot;
    int currentprogress = 0;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private int forwardTime = 10000;
    private int backwardTime = 10000;
    private AppCompatSeekBar seekbar;
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if (mediaPlayer == null) {

            } else {
                startTime = mediaPlayer.getCurrentPosition();
                current.setText(String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
                );
                seekbar.setProgress((int) startTime);
                myHandler.postDelayed(this, 100);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_audio_player);

        initializeView();
        getValueFromIntent();
        setOnclickListener();
        iniPlayer();

    }

    private void initializeView() {
//        play = findViewById(R.id.audio_player_play);
//        pause =  findViewById(R.id.audio_player_pause);
//
//        previous = findViewById(R.id.audio_player_previous);
//        next = findViewById(R.id.audio_player_next);
//        audioname = findViewById(R.id.audio_player_audioname);
//        audio_image = findViewById(R.id.audio_player_image);
//        toolbar_title = findViewById(R.id.audio_player_toolbar_title);
//        audio_player_toolbar = findViewById(R.id.audioplayer_In_toolbar);
//        seekbar = findViewById(R.id.audio_player_seekbar);
//        current = findViewById(R.id.audio_player_current);
//        linearLayoutroot = findViewById(R.id.root);
//        duration = findViewById(R.id.audio_player_duration);

        seekbar.setClickable(false);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.grow_material_background);
        Bitmap blurredBit = BlurBuilder.blur(this, bitmap);
        BitmapDrawable background = new BitmapDrawable(getResources(), blurredBit);
        linearLayoutroot.setBackground(background);


        setSupportActionBar(audio_player_toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(audio_player_toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void getValueFromIntent() {
        myaudioname = getIntent().getStringExtra("audioname");
        url = getIntent().getStringExtra("uri");
        audioname.setText(myaudioname);
        toolbar_title.setText(myaudioname);
        String imageurl = getIntent().getStringExtra("image");


        Uri imageuri = Uri.parse(imageurl);
//        audio_image.setImageURI(imageuri);
        Picasso.get().load(url).into(audio_image);
      /*ImageRequest request = ImageRequest.fromUri(imageuri);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(audio_image.getController()).build();


        audio_image.setController(controller);*/
    }

    private void setOnclickListener() {
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
                next.setEnabled(true);
                previous.setEnabled(true);
                pause.setEnabled(true);
                play.setEnabled(false);


                seekbar.setProgress((int) startTime);
                myHandler.postDelayed(UpdateSongTime, 100);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
                next.setEnabled(true);
                previous.setEnabled(true);
                pause.setEnabled(false);
                play.setEnabled(true);

            }
        });
        audio_player_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int temp = (int) startTime;

                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);

                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;

                if ((temp - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);

                }
            }
        });


    }

    private void iniPlayer() {
        Uri audiouri = Uri.parse(url);
//        mediaPlayer = MediaPlayer.create(AudioPlayerFragment.this, audiouri);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, Uri.parse(url));
            finalTime = mediaPlayer.getDuration();
            startTime = mediaPlayer.getCurrentPosition();

            seekbar.setMax((int) finalTime);

            current.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    startTime)))
            );

            duration.setText(String.format("%d:%d ",
                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    finalTime)))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        //mediaPlayer.reset();
        //mediaPlayer.prepare();
        //mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;

    }

    @Override
    public void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);
        outstate.putInt("pid", android.os.Process.myPid());

    }
}
