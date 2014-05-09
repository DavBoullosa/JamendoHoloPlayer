
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

package es.oneoctopus.jamendoholoplayer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;

@JsonRootName(value = "result")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Track implements Serializable {
    @JsonIgnore
    private static final long serialVersionUID = 1L;
    private Long album_id;
    private String album_image;
    private String album_name;
    private Long artist_id;
    private String artist_idstr;
    private String artist_name;
    private String audio;
    private String audiodownload;
    private Number duration;
    private Long id;
    private String license_ccurl;
    private String name;
    private Number position;
    private String releasedate;
    private String shareurl;
    private String shorturl;
    private Stats stats;

    public Long getAlbum_id() {
        return this.album_id;
    }

    public void setAlbum_id(Long album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_image() {
        return this.album_image;
    }

    public void setAlbum_image(String album_image) {
        this.album_image = album_image;
    }

    public String getAlbum_name() {
        return this.album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public Long getArtist_id() {
        return this.artist_id;
    }

    public void setArtist_id(Long artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist_idstr() {
        return this.artist_idstr;
    }

    public void setArtist_idstr(String artist_idstr) {
        this.artist_idstr = artist_idstr;
    }

    public String getArtist_name() {
        return this.artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getAudio() {
        return this.audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudiodownload() {
        return this.audiodownload;
    }

    public void setAudiodownload(String audiodownload) {
        this.audiodownload = audiodownload;
    }

    public Number getDuration() {
        return this.duration;
    }

    public void setDuration(Number duration) {
        this.duration = duration;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicense_ccurl() {
        return this.license_ccurl;
    }

    public void setLicense_ccurl(String license_ccurl) {
        this.license_ccurl = license_ccurl;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Number getPosition() {
        return this.position;
    }

    public void setPosition(Number position) {
        this.position = position;
    }

    public String getReleasedate() {
        return this.releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getShareurl() {
        return this.shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    public String getShorturl() {
        return this.shorturl;
    }

    public void setShorturl(String shorturl) {
        this.shorturl = shorturl;
    }

    public Stats getStats() {
        return this.stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    @Override
    public String toString() {
        return "Track{" +
                "album_id=" + album_id +
                ", album_image='" + album_image + '\'' +
                ", album_name='" + album_name + '\'' +
                ", artist_id=" + artist_id +
                ", artist_idstr='" + artist_idstr + '\'' +
                ", artist_name='" + artist_name + '\'' +
                ", audio='" + audio + '\'' +
                ", audiodownload='" + audiodownload + '\'' +
                ", duration=" + duration +
                ", id=" + id +
                ", license_ccurl='" + license_ccurl + '\'' +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", releasedate='" + releasedate + '\'' +
                ", shareurl='" + shareurl + '\'' +
                ", shorturl='" + shorturl + '\'' +
                ", stats=" + stats +
                '}';
    }
}
