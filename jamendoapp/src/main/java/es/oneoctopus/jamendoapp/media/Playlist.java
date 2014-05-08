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

package es.oneoctopus.jamendoapp.media;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import es.oneoctopus.jamendoapp.models.Track;
import es.oneoctopus.jamendoapp.utils.PlayMode;

import static es.oneoctopus.jamendoapp.utils.PlayMode.NORMAL;

public class Playlist implements Serializable {
    private static final long serialVersionUID = 3L;
    private static final String TAG = "Playlist";
    private PlayMode playlistMode = NORMAL;
    private List<Track> currentPlaylist;
    private List<Track> backedPlaylist;
    private int currentTrack = 0;

    public Playlist(List<Track> currentPlaylist) {
        this.currentPlaylist = currentPlaylist;
        this.backedPlaylist = currentPlaylist;
    }

    public Playlist(List<Track> currentPlaylist, int currentTrack) {
        this.currentPlaylist = currentPlaylist;
        this.currentTrack = currentTrack;
    }

    public PlayMode getPlaylistMode() {
        return playlistMode;
    }

    public void setPlaylistMode(PlayMode playlistMode) {
        this.playlistMode = playlistMode;
    }

    public List<Track> getCurrentPlaylist() {
        return currentPlaylist;
    }

    public void setCurrentPlaylist(List<Track> currentPlaylist) {
        this.currentPlaylist = currentPlaylist;
    }

    public void addTrack(Track track) {
        currentPlaylist.add(track);
    }

    public boolean isEmpty() {
        return currentPlaylist.isEmpty();
    }

    public void selectPreviousTrack() {
        if (!isEmpty()) {
            currentTrack--;
            if (currentTrack < 0)
                currentTrack = currentPlaylist.size() - 1;
        }
    }

    public void selectNextTrack() {
        if (!isEmpty() && currentTrack != currentPlaylist.size() - 1) {
            currentTrack++;
            currentTrack %= currentPlaylist.size();
        } else {
            currentTrack = -1;
        }
    }

    public void selectTrack(int position) {
        currentTrack = position;
    }

    public Track getCurrentTrack() {
        return currentPlaylist.get(currentTrack);
    }

    public boolean isLastTrack() {
        return currentTrack == currentPlaylist.size() - 1;
    }

    public boolean isOver() {
        if (currentTrack == -1) return true;
        else return false;
    }

    public void restartPlaylist() {
        currentTrack = 0;
    }

    public void changePlaylistOrder() {

        switch (playlistMode) {
            case NORMAL:
                currentPlaylist = backedPlaylist;
                break;
            case SHUFFLE:
                Collections.shuffle(currentPlaylist);
                break;
            case REPEAT:
                break;
            case SHUFFLEANDREPEAT:
                Collections.shuffle(currentPlaylist);
                break;
        }
    }
}
