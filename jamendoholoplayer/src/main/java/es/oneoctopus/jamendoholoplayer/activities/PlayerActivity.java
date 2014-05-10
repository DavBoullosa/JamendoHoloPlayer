/*
 * Copyright (c) 2014 David Alejandro Fernández Sancho
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.oneoctopus.jamendoholoplayer.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nvanbenschoten.motion.ParallaxImageView;
import com.squareup.picasso.Picasso;

import es.oneoctopus.jamendoholoplayer.R;
import es.oneoctopus.jamendoholoplayer.media.Playlist;
import es.oneoctopus.jamendoholoplayer.services.PlayService;

public class PlayerActivity extends BaseJamendoActivity {
    private final String TAG = "PlayerActivity";
    Handler handler = new Handler();
    Runnable trackbarRunnable;

    private ParallaxImageView trackImage;
    private TextView trackTitle;
    private TextView trackArtist;
    private TextView timeLeft;
    private ImageView playPause;
    private ImageView previous;
    private ImageView next;
    private SeekBar trackBar;

    private Playlist playlist;
    private PlayService playService;
    private boolean fromNotification = false;
    private boolean restart = false;

    private boolean updateTrackbar = false;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {

                case PlayService.PLAYLIST_LOADED:
                    updatePlayerInterface();
                    setPlayControl();
                    restart = false;
                    setControls();
                    break;

                case PlayService.PLAYLIST_STARTED:
                    restart = true;
                    break;

                case PlayService.TRACK_START:
                    makeControlsAvailable(true);
                    updateTrackbar = true;
                    updateTrackbar();
                    setPauseControl();
                    break;

                case PlayService.TRACK_END:
                    updateTrackbar = false;
                    setPlayControl();
                    resetTrackbar();
                    break;

                case PlayService.TRACK_CHANGE:
                    updatePlayerInterface();
                    makeControlsAvailable(false);
                    break;

                case PlayService.PLAYLIST_END:
                    restart = false;
                    updatePlayerInterface();
                    setPlayControl();
                    resetTrackbar();
            }
        }
    };
    private Intent playIntent;
    private boolean boundToPlayService = false;
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "Binding service");
            PlayService.PlayBinder binder = (PlayService.PlayBinder) service;
            playService = binder.getService();
            if (!fromNotification) playService.loadPlaylist(playlist);
            boundToPlayService = true;
            if (fromNotification) {
                setControls();
                updatePlayerInterface();
                updateTrackbar = true;
                updateTrackbar();
                setPauseControl();
                restart = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "Unbinding service");
            boundToPlayService = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        setContentView(R.layout.activity_player);

        trackImage = (ParallaxImageView) findViewById(R.id.trackimage);
        trackTitle = (TextView) findViewById(R.id.titletrack);
        trackArtist = (TextView) findViewById(R.id.artistname);
        playPause = (ImageView) findViewById(R.id.playpausebutton);
        next = (ImageView) findViewById(R.id.next);
        previous = (ImageView) findViewById(R.id.previous);
        trackBar = (SeekBar) findViewById(R.id.trackbar);
        timeLeft = (TextView) findViewById(R.id.timeleft);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("playlist"))
                playlist = (Playlist) getIntent().getExtras().getSerializable("playlist");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "onNewIntent");
        fromNotification = true;
        setControls();
        updatePlayerInterface();
        updateTrackbar = true;
        updateTrackbar();
        setPauseControl();
        restart = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        trackImage.registerSensorManager();
        setReceivers();

        fromNotification = getIntent().getBooleanExtra("fromNotification", false);

        if (playService == null) {
            playIntent = new Intent(this, PlayService.class);
            getApplicationContext().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    public void setReceivers() {
        registerReceiver(broadcastReceiver, new IntentFilter(PlayService.PLAYLIST_LOADED));
        registerReceiver(broadcastReceiver, new IntentFilter(PlayService.PLAYLIST_STARTED));
        registerReceiver(broadcastReceiver, new IntentFilter(PlayService.TRACK_START));
        registerReceiver(broadcastReceiver, new IntentFilter(PlayService.TRACK_END));
        registerReceiver(broadcastReceiver, new IntentFilter(PlayService.TRACK_CHANGE));
        registerReceiver(broadcastReceiver, new IntentFilter(PlayService.PLAYLIST_END));
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            getApplicationContext().unbindService(musicConnection);
        } catch (IllegalArgumentException e) {
            //Log.e(TAG, "onPause", e);
        }

        unregisterReceiver(broadcastReceiver);
        updateTrackbar = false;
        restart = false;
        trackImage.unregisterSensorManager();
    }

    private void setControls() {

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fromNotification) {
                    System.out.println("FROM NOTIFICATION");
                    if (playService.isPlaying()) {
                        System.out.println("PAUSE");
                        pause();
                    } else {
                        System.out.println("RESTART");
                        restart();
                    }
                } else {
                    if (playService.isPlaying()) {
                        if (restart) {
                            System.out.println("PAUSE");
                            pause();
                        } else {
                            System.out.println("START");
                            start();
                        }
                    } else {
                        if (restart) {
                            System.out.println("RESTART");
                            restart();
                        } else {
                            System.out.println("START");
                            start();
                        }

                    }
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playService.setLoadedAsCurrent();
                playService.playPrev();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playService.setLoadedAsCurrent();
                playService.playNext();
            }
        });

        trackBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && playService.isPlaying()) playService.seek(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!playService.isPlaying()) playService.seek(seekBar.getProgress());
            }
        });
    }

    private void makeControlsAvailable(boolean available) {
        playPause.setClickable(available);
        next.setClickable(available);
        previous.setClickable(available);
    }

    public void updatePlayerInterface() {
        if (fromNotification) {
            getActionBar().setTitle(playService.getCurrentTrack().getName());
            Picasso.with(PlayerActivity.this).load(playService.getCurrentTrack().getAlbum_image()).into(trackImage);
            trackTitle.setText(playService.getCurrentTrack().getName());
            trackArtist.setText(playService.getCurrentTrack().getArtist_name());
        } else {
            getActionBar().setTitle(playService.getLoadedTrack().getName());
            Picasso.with(PlayerActivity.this).load(playService.getLoadedTrack().getAlbum_image()).into(trackImage);
            trackTitle.setText(playService.getLoadedTrack().getName());
            trackArtist.setText(playService.getLoadedTrack().getArtist_name());
        }
    }

    /**
     * Updates the trackbar only under three circumstances: if the player was stopped and we play a track, if the track we selected is the same
     * that is playing at the moment, or if we play a different track than the one playing.
     */
    public void updateTrackbar() {

        if (playService.isPlaying()) {
            trackBar.setMax(getDuration());
            trackbarRunnable = new Runnable() {
                @Override
                public void run() {
                    if (playService.isPlaying() && updateTrackbar) {
                        trackBar.setProgress(playService.getPosition());
                        //TODO: show correct time left
                        timeLeft.setText("-" + String.valueOf((playService.getDuration() - playService.getPosition()) / 100));
                        handler.postDelayed(this, 1000);
                    }
                }
            };
            handler.postDelayed(trackbarRunnable, 1000);
        }
    }

    public void resetTrackbar() {
        trackBar.setProgress(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //TODO: handle download item

        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void start() {
        playService.setLoadedAsCurrent();
        playService.playTrack();
    }

    public void restart() {
        playService.go();
        setPauseControl();
    }

    public void pause() {
        playService.pausePlayer();
        setPlayControl();
    }

    public int getDuration() {
        if (playService != null && boundToPlayService && playService.isPlaying())
            return playService.getDuration();
        else return 0;
    }

    public void setPauseControl() {
        playPause.setImageResource(R.drawable.pauseicon);
    }

    public void setPlayControl() {
        playPause.setImageResource(R.drawable.playicon);
    }

}
