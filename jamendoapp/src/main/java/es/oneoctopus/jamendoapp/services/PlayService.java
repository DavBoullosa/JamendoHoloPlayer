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
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import es.oneoctopus.jamendoapp.media.Playlist;
import es.oneoctopus.jamendoapp.models.Track;
import es.oneoctopus.jamendoapp.notifications.NowPlayingNotification;

public class PlayService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {
    public static final String TRACK_CHANGE = "es.oneoctopus.jamendoapp.PlayService.action.TRACK_CHANGE";
    public static final String TRACK_START = "es.oneoctopus.jamendoapp.PlayService.action.TRACK_START";
    public static final String TRACK_END = "es.oneoctopus.jamendoapp.PlayService.action.TRACK_END";
    public static final String TRACK_BUFFERING = "es.oneoctopus.jamendoapp.PlayService.action.TRACK_BUFFERING";
    public static final String PLAYLIST_LOADED = "es.oneoctopus.jamendoapp.PlayService.action.PLAYLIST_LOADED";
    public static final String PLAYLIST_STARTED = "es.oneoctopus.jamendoapp.PlayService.action.PLAYLIST_STARTED";
    public static final String PLAYLIST_END = "es.oneoctopus.jamendoapp.PlayService.action.PLAYLIST_END";

    public final String TAG = "PlayService";
    private final IBinder playBind = new PlayBinder();
    private Intent commActivity;
    private MediaPlayer mediaPlayer;
    private Playlist currentPlaylist;
    private Playlist loadedPlaylist;
    private boolean canBePaused;

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

    public void loadPlaylist(Playlist playlist) {
        this.loadedPlaylist = playlist;
        notifyToPlayer(PLAYLIST_LOADED);
    }

    public Track getLoadedTrack() {
        return loadedPlaylist.getCurrentTrack();
    }

    public void setLoadedAsCurrent() {
        currentPlaylist = loadedPlaylist;
    }

    public void playTrack() {
        if (isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(currentPlaylist.getCurrentTrack().getAudio()));
        } catch (IOException e) {
            Log.e(TAG, "Setting DataSource", e);
        }

        mediaPlayer.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        showNotification(true);
        setCanBePaused(true);
        if (currentPlaylist.isPlaylistFirstPlayed()) {
            notifyToPlayer(PLAYLIST_STARTED);
            currentPlaylist.setIsPlaylistFirstPlayed(false);
        }
        notifyToPlayer(TRACK_START);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mediaPlayer.reset();
        setCanBePaused(false);
        showNotification(false);
        currentPlaylist.selectNextTrack();

        if (currentPlaylist.isOver()) {
            currentPlaylist.restartPlaylist();
            notifyToPlayer(PLAYLIST_END);
        } else {
            notifyToPlayer(TRACK_END);
            notifyToPlayer(TRACK_CHANGE);
            playTrack();
        }
    }

    public Track getCurrentTrack() {
        return currentPlaylist.getCurrentTrack();
    }

    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public int getPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void pausePlayer() {
        mediaPlayer.pause();
        showNotification(false);
    }

    public void seek(int posn) {
        mediaPlayer.seekTo(posn);
    }

    public void go() {
        mediaPlayer.start();
        showNotification(true);
    }

    public void playPrev() {
        if (!currentPlaylist.isFirstTrack())
            currentPlaylist.selectPreviousTrack();
        else
            currentPlaylist.selectLastTrack();

        notifyToPlayer(TRACK_CHANGE);
        playTrack();
    }

    public void playNext() {
        if (!currentPlaylist.isLastTrack())
            currentPlaylist.selectNextTrack();
        else
            currentPlaylist.restartPlaylist();

        notifyToPlayer(TRACK_CHANGE);
        playTrack();
    }

    public void notifyToPlayer(String notification) {
        commActivity = new Intent(notification);
        sendBroadcast(commActivity);
    }

    public void showNotification(boolean show) {
        if (show) {
            Picasso.with(getApplicationContext()).load(currentPlaylist.getCurrentTrack().getAlbum_image()).into(new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    NowPlayingNotification.notify(getApplicationContext(), currentPlaylist.getCurrentTrack().getName(), currentPlaylist.getCurrentTrack().getArtist_name(), bitmap);
                }

                @Override
                public void onBitmapFailed(final Drawable errorDrawable) {
                    Log.d("TAG", "FAILED");
                }

                @Override
                public void onPrepareLoad(final Drawable placeHolderDrawable) {
                    Log.d("TAG", "Prepare Load");
                }
            });
        } else
            NowPlayingNotification.cancel(getApplicationContext());
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    public boolean canBePaused() {
        return canBePaused;
    }

    private void setCanBePaused(boolean canBePaused) {
        this.canBePaused = canBePaused;
    }

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }
}
