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

package es.oneoctopus.jamendoapp.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;

import es.oneoctopus.jamendoapp.media.Playlist;
import es.oneoctopus.jamendoapp.models.Track;

public class PlayService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    public final String TAG = "PlayService";
    private final IBinder playBind = new PlayBinder();
    private MediaPlayer mediaPlayer;
    private Playlist currentPlaylist;
    private Track currentTrack;

    @Override
    public IBinder onBind(Intent intent) {
        return playBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //mediaPlayer.stop();
        //mediaPlayer.release();
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public void setPlaylist(Playlist playlist) {
        this.currentPlaylist = playlist;
    }

    public void playTrack() {
        if (isPlaying()) mediaPlayer.stop();
        mediaPlayer.reset();
        updateCurrentTrack();

        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(currentTrack.getAudio()));
        } catch (IOException e) {
            Log.e(TAG, "Setting DataSource", e);
        }

        mediaPlayer.prepareAsync();
    }

    public void updateCurrentTrack() {
        currentTrack = currentPlaylist.getCurrentTrack();
    }

    public Track currentTrackPlaying() {
        return currentTrack;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public int getPosn() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getDur() {
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void pausePlayer() {
        mediaPlayer.pause();
    }

    public void seek(int posn) {
        mediaPlayer.seekTo(posn);
    }

    public void go() {
        mediaPlayer.start();
    }

    public void playPrev() {
        currentPlaylist.selectPreviousTrack();
        updateCurrentTrack();
        playTrack();
    }

    public void playNext() {
        currentPlaylist.selectNextTrack();
        updateCurrentTrack();
        playTrack();
    }

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

}
