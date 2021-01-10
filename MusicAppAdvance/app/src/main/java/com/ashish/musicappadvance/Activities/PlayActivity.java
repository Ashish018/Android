package com.ashish.musicappadvance.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ashish.musicappadvance.ModelClasses.MusicFiles;
import com.ashish.musicappadvance.ModelClasses.NotificationClass;
import com.ashish.musicappadvance.ModelClasses.Playable;
import com.ashish.musicappadvance.R;
import com.ashish.musicappadvance.Services.OnClearFromRecentService;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.ashish.musicappadvance.Activities.MainActivity.musicFiles;
import static com.ashish.musicappadvance.Adapters.MusicAdapter.musicFilesArrayList;

public class PlayActivity extends AppCompatActivity implements Playable {

    TextView played,total,song,artist;
    ImageView previous,next,songImage;
    FloatingActionButton playPause;
    SeekBar seekBar;
    int position;
    static ArrayList<MusicFiles> songsFiles;
    Uri uri;
    static MediaPlayer mediaPlayer;
    Handler handler;
    AudioManager audioManager;

    NotificationManager notificationManager;

    AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Permanent loss of audio focus
                        // Pause playback immediately
                        if(mediaPlayer!=null) mediaPlayer.release();
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                        // Pause playback
                        mediaPlayer.pause();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Lower the volume, keep playing
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        mediaPlayer.start();
                        // Your app has been granted audio focus again
                        // Raise volume to normal, restart playback if necessary
                    }
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        init();
        getSupportActionBar().hide();
        getIntentMethod();
        setNameArtistTime();
        audioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        PlayActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,500);
                played.setText(getTime(mediaPlayer.getCurrentPosition()/1000));
            }
        });
        createNotificationMethod();
        registerReceiver(broadcastReceiver,new IntentFilter("TRACKS_TRACKS"));
        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
    }

    private void createNotificationMethod() {
        createChannel();
        if(mediaPlayer.isPlaying())
        NotificationClass.createNotification(this,songsFiles.get(position),R.drawable.ic_baseline_pause_24,position,songsFiles.size()-1);
        else  NotificationClass.createNotification(this,songsFiles.get(position),R.drawable.ic_baseline_play_arrow_24,position,songsFiles.size()-1);
    }

    private void createChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel(NotificationClass.CHANNEL_ID,"AK", NotificationManager.IMPORTANCE_LOW);
            notificationManager=getSystemService(NotificationManager.class);
            if(notificationManager!=null)
            {
                notificationManager.createNotificationChannel(notificationChannel);
            }

        }

    }

    private void songCompleted(MediaPlayer mediaPlayer) {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextButton();
            }
        });
    }

    private void setNameArtistTime() {
        song.setText(songsFiles.get(position).getTitle());
        artist.setText(songsFiles.get(position).getArtist());
        total.setText(getTime(mediaPlayer.getDuration()/1000));
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void init() {
        played=findViewById(R.id.played);
        total=findViewById(R.id.total);
        song=findViewById(R.id.albumSong);
        artist=findViewById(R.id.artist);
        playPause=findViewById(R.id.play);
        previous=findViewById(R.id.previous);
        next=findViewById(R.id.next);
        seekBar=findViewById(R.id.seekBar);
        handler=new Handler();
        songImage=findViewById(R.id.albumImage);
        next.setOnClickListener(v->nextButton());
        previous.setOnClickListener(v->prevButton());
        playPause.setOnClickListener(v->playPauseButton());
    }

    private void playPauseButton() {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
            playPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
        else
        {
            mediaPlayer.start();
            playPause.setImageResource(R.drawable.ic_baseline_pause_24);
        }
        createNotificationMethod();
    }


    private void nextButton() {
        songsFiles=musicFiles;
        position=(position+1)%songsFiles.size();
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        uri=Uri.parse(songsFiles.get(position).getPath()); //Uri uri = Uri.parse("String file location");
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        setNameArtistTime();
        songCompleted(mediaPlayer);
        createNotificationMethod();
        playPause.setImageResource(R.drawable.ic_baseline_pause_24);
    }

    private void prevButton() {
        songsFiles=musicFiles;
        position=(position-1)<0?(songsFiles.size()-1):(position-1);
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        uri=Uri.parse(songsFiles.get(position).getPath());
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        setNameArtistTime();
        songCompleted(mediaPlayer);
        createNotificationMethod();
        playPause.setImageResource(R.drawable.ic_baseline_pause_24);
    }

    private void getIntentMethod() {
        position=getIntent().getIntExtra("position",-1);
        songsFiles=musicFilesArrayList; // Getting files of AdapterClass
        if(songsFiles!=null)
        {
            uri=Uri.parse(songsFiles.get(position).getPath());
        }
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        songCompleted(mediaPlayer);
        seekBar.setMax(mediaPlayer.getDuration()); // Dividing by 1000 because getDuration() gives in ms and we want it in seconds
        int result = audioManager.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Start playback
            // Create and setup the {@link MediaPlayer} for the audio resource associated
            // with the current word
            mediaPlayer=MediaPlayer.create(this,uri);

            // Start the audio file
            mediaPlayer.start();

            // Setup a listener on the media player, so that we can stop and release the
            // media player once the sound has finished playing.
        }

    }

    private String getTime(int i) {
        String s=i/60+":"+i%60;
        return s;
    }

    private void metaData(Uri uri)
    {
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal=Integer.parseInt(songsFiles.get(position).getDuration());
        total.setText(getTime(durationTotal));
        byte art[]=retriever.getEmbeddedPicture();
        if(art!=null) Glide.with(this).asBitmap().load(art).into(songImage);
        else Glide.with(this).load(R.drawable.food_logo).into(songImage);
    }


    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getExtras().getString("actionName");
            switch (action)
            {
                case NotificationClass.ACTION_PREVIOUS:
                    prevButton();
                    break;
                case NotificationClass.ACTION_NEXT:
                    nextButton();
                    break;
                case NotificationClass.ACTION_PLAY:
                    playPauseButton();
                    break;


            }
        }
    };
    @Override
    public void onTrackPrevious() {

    }

    @Override
    public void onTrackPlay() {

    }

    @Override
    public void onTrackPause() {

    }

    @Override
    public void onTrackNext() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificationManager.cancelAll();
        unregisterReceiver(broadcastReceiver);
    }
}