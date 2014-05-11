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

package es.oneoctopus.jamendoholoplayer.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import es.oneoctopus.jamendoholoplayer.R;
import es.oneoctopus.jamendoholoplayer.models.Track;

public class TrackDownloader {
    private Track track;
    private Context cx;
    private long downloadId;

    public TrackDownloader(Context context, Track track) {
        this.cx = context;
        this.track = track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public void startDownload() {
        android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(Uri.parse(track.getAudiodownload()));
        request.setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(String.format(cx.getString(R.string.downloading), track.getName()));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //TODO: Improve location and name
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, track.getName() + cx.getString(R.string.mp3format));

        DownloadManager downloadManager = (DownloadManager) cx.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadId = downloadManager.enqueue(request);
    }
}
