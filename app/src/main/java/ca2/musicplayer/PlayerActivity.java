package ca2.musicplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private TextView songNameText;
    private ImageButton playPauseButton;
    private boolean isPlaying = false;
    private Runnable updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        String songPath = getIntent().getStringExtra("songPath");
        String songName = getIntent().getStringExtra("songName");

        if (songPath == null || songName == null) {
            Toast.makeText(this, "Error: Song details not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        songNameText.setText(songName);
        setupMediaPlayer(songPath);
        setupSeekBar();
        setupPlayPauseButton();
    }

    private void initializeViews() {
        songNameText = findViewById(R.id.song_name);
        seekBar = findViewById(R.id.seek_bar);
        playPauseButton = findViewById(R.id.play_pause_button);
    }

    private void setupMediaPlayer(String songPath) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(songPath);
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (Exception e) {
            Toast.makeText(this, "Error playing song: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            finish();
        }
    }

    private void setupSeekBar() {
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    try {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        handler.postDelayed(this, 100);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupPlayPauseButton() {
        playPauseButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    playPauseButton.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    mediaPlayer.start();
                    handler.post(updateSeekBar);
                    playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
                }
                isPlaying = !isPlaying;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            playPauseButton.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            handler.removeCallbacks(updateSeekBar);
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}