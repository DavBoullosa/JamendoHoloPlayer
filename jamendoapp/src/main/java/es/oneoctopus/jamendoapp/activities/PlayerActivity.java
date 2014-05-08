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

package es.oneoctopus.jamendoapp.activities;

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

import es.oneoctopus.jamendoapp.R;
import es.oneoctopus.jamendoapp.media.Playlist;
import es.oneoctopus.jamendoapp.services.PlayService;

public class PlayerActivity extends BaseJamendoActivity {
    private final String TAG = "PlayerActivity";
    Handler handler = new Handler();
    Runnable trackbarRunnable;

    private ParallaxImageView trackImage;
    private TextView trackTitle;
    private TextView trackArtist;
    private ImageView playPause;
    private ImageView previous;
    private ImageView next;
    private SeekBar trackBar;

    private Playlist playlist;
    private PlayService playService;

    private boolean updateTrackbar = false;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {

                case PlayService.PLAYLIST_SET:
                    updatePlayerInterface();
                    setPlayControl();
                    setControls();
                    break;

                case PlayService.TRACK_CHANGE:
                    updatePlayerInterface();
                    break;

                case PlayService.TRACK_START:
                    updateTrackbar = true;
                    updateTrackbar();
                    setPauseControl();
                    break;

                case PlayService.TRACK_END:
                    updateTrackbar = false;
                    setPlayControl();
                    resetTrackbar();
                    break;

                case PlayService.PLAYLIST_END:
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
            PlayService.PlayBinder binder = (PlayService.PlayBinder) service;
            playService = binder.getService();
            playService.setPlaylist(playlist);
            boundToPlayService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            boundToPlayService = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        trackImage = (ParallaxImageView) findViewById(R.id.trackimage);
        trackTitle = (TextView) findViewById(R.id.titletrack);
        trackArtist = (TextView) findViewById(R.id.artistname);
        playPause = (ImageView) findViewById(R.id.playpausebutton);
        next = (ImageView) findViewById(R.id.next);
        previous = (ImageView) findViewById(R.id.previous);
        trackBar = (SeekBar) findViewById(R.id.trackbar);

        if (getIntent().getExtras() != null)
            playlist = (Playlist) getIntent().getExtras().getSerializable("playlist");
    }

    @Override
    protected void onResume() {
        super.onResume();

        trackImage.registerSensorManager();
        setReceivers();

        if (playIntent == null) {
            playIntent = new Intent(this, PlayService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }

        if (boundToPlayService && playService != null) updateTrackbar();
        if (boundToPlayService) setControls();
    }

    public void setReceivers() {
        registerReceiver(broadcastReceiver, new IntentFilter(PlayService.TRACK_START));
        registerReceiver(broadcastReceiver, new IntentFilter(PlayService.TRACK_END));
        registerReceiver(broadcastReceiver, new IntentFilter(PlayService.TRACK_CHANGE));
        registerReceiver(broadcastReceiver, new IntentFilter(PlayService.PLAYLIST_END));
        registerReceiver(broadcastReceiver, new IntentFilter(PlayService.PLAYLIST_SET));
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unbindService(musicConnection);
            if (playlist.isEmpty() || playlist.isOver()) {
                stopService(playIntent);
                playService = null;
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "onPause", e);
        }

        unregisterReceiver(broadcastReceiver);
        updateTrackbar = false;
        trackImage.unregisterSensorManager();
    }

    private void setControls() {

        if (playService.isPlaying()) updateTrackbar = true;

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (playService.getCurrentTrack() == null) {
                    start();
                } else {
                    playService.setPlaylist(playlist);
                    start();
                }
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

    private void playNext() {
        playService.playNext();
    }

    private void playPrev() {
        playService.playPrev();
    }

    public void updatePlayerInterface() {
        getActionBar().setTitle(playService.getCurrentTrack().getName());
        Picasso.with(PlayerActivity.this).load(playService.getCurrentTrack().getAlbum_image()).into(trackImage);
        trackTitle.setText(playService.getCurrentTrack().getName());
        trackArtist.setText(playService.getCurrentTrack().getArtist_name());
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
        getMenuInflater().inflate(R.menu.artist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void start() {
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

    public int getCurrentPosition() {
        if (playService != null && boundToPlayService && playService.isPlaying())
            return playService.getPosition();
        else return 0;
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    public int getAudioSessionId() {
        return 0;
    }

    public void setPauseControl() {
        playPause.setImageResource(R.drawable.pauseicon);
    }

    public void setPlayControl() {
        playPause.setImageResource(R.drawable.playicon);
    }

}
