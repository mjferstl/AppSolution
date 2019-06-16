package mfdevelopement.appsolution;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

public class InternetRadio extends AppCompatActivity {

    private String url_radio= "http://stream.RadioimInternet.de:8080/mobile";
    private ProgressBar playSeekBar;

    private ImageButton btnPlayPause;
    private ImageButton btnStop;

    private MediaPlayer mediaPlayer;

    private String appname;
    private String LogTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_radio);

        getElements();

        appname = getString(R.string.app_name);
        LogTag = appname + "/internetRadio";

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url_radio);
            Log.i(LogTag,"datasource set");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                //playSeekBar.setIndeterminate(false);
                playSeekBar.setSecondaryProgress(percent);
                Log.i("Buffering", "" + percent);
            }
        });

        mediaPlayer.prepareAsync();
        playSeekBar.setVisibility(View.VISIBLE);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Toast.makeText(InternetRadio.this, "ready", Toast.LENGTH_SHORT).show();
                mediaPlayer.start();
                btnPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
                playSeekBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void getElements() {

        btnPlayPause = findViewById(R.id.btn_internet_radio_play_pause);
        //btnPause = findViewById(R.id.btn_internet_radio_pause);
        btnStop = findViewById(R.id.btn_internet_radio_stop);

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudio();
            }
        });

        playSeekBar = findViewById(R.id.progressBar1);
        playSeekBar.setMax(100);
        playSeekBar.setVisibility(View.INVISIBLE);
        playSeekBar.setIndeterminate(true);

    }

    private void playAudio() {

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            playSeekBar.setVisibility(View.VISIBLE);
            btnPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
        }
        else {
            pauseAudio();
        }
    }

    private void pauseAudio() {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            btnPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }

    }

    private void stopAudio() {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            btnPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }

        playSeekBar.setIndeterminate(true);
        playSeekBar.setVisibility(View.INVISIBLE);
    }

}
