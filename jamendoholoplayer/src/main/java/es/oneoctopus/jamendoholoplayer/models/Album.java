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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;

@JsonRootName(value = "result")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Album implements Serializable {
    private long artist_id;
    private String artist_name;
    private long id;
    private String image;
    private String name;
    private String releasedate;
    private String shareurl;
    private String shorturl;

    public long getArtist_id() {
        return this.artist_id;
    }

    public void setArtist_id(long artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist_name() {
        return this.artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

}
