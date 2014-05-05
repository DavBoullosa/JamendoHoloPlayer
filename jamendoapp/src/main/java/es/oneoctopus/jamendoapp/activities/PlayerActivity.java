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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import es.oneoctopus.jamendoapp.R;
import es.oneoctopus.jamendoapp.media.Playlist;
import es.oneoctopus.jamendoapp.models.Track;
import es.oneoctopus.jamendoapp.services.PlayService;

public class PlayerActivity extends BaseJamendoActivity {
    private final String TAG = "PlayerActivity";
    private ImageView trackImage;
    private TextView trackTitle;
    private TextView trackArtist;
    private ImageView playButton;

    private Track currentTrack;
    private Playlist playlist;
    private PlayService playService;
    private Intent playIntent;
    private boolean musicBound = false;
    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayService.PlayBinder binder = (PlayService.PlayBinder) service;
            //get service
            playService = binder.getService();
            //pass list
            playService.setPlaylist(playlist);
            musicBound = true;
            setControls();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        trackImage = (ImageView) findViewById(R.id.trackimage);
        trackTitle = (TextView) findViewById(R.id.titletrack);
        trackArtist = (TextView) findViewById(R.id.artistname);

        playButton = (ImageView) findViewById(R.id.playbutton);

        if (getIntent().getExtras() != null) {
            playlist = (Playlist) getIntent().getExtras().getSerializable("playlist");
            updateTrackStuff();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, PlayService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    private void setControls() {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                If we tap this with the same track we're listening that's a pause. If not, that's a track change.
                 */
                if (currentTrack == playService.currentTrackPlaying()) {
                    if (!isPlaying()) playService.playTrack();
                    else pause();
                } else {
                    playService.playTrack();
                }
            }
        });
    }

    private void playNext() {
        playService.playNext();
    }

    private void playPrev() {
        playService.playPrev();
    }

    public void updateTrackStuff() {
        if (!playlist.isLastTrack()) {
            playlist.selectNextTrack();
            currentTrack = playlist.getCurrentTrack();
            updateUI();
        }
    }

    public void startPlayingSongAutomatically() {
        playService.setPlaylist(playlist);
        playService.playTrack();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (playlist.isEmpty() || playlist.isOver()) {
            stopService(playIntent);
            playService = null;
        }
        unbindService(musicConnection);
    }

    public void updateUI() {
        getActionBar().setTitle(currentTrack.getName());
        Picasso.with(this).load(currentTrack.getAlbum_image()).into(trackImage);
        trackTitle.setText(currentTrack.getName());
        trackArtist.setText(currentTrack.getArtist_name());
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
        playService.go();
    }

    public void pause() {
        playService.pausePlayer();
    }

    public int getDuration() {
        if (playService != null && musicBound && playService.isPlaying())
            return playService.getDur();
        else return 0;
    }

    public int getCurrentPosition() {
        if (playService != null && musicBound && playService.isPlaying())
            return playService.getPosn();
        else return 0;
    }

    public void seekTo(int pos) {

    }

    public boolean isPlaying() {
        if (playService != null && musicBound)
            return playService.isPlaying();
        else
            return false;
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
}
