
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

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stats implements Serializable {
    @JsonIgnore
    private static final long serialVersionUID = 2L;
    private Number avgnote;
    private Number dislikes;
    private Number favorited;
    private Number likes;
    private Number notes;
    private Number playlisted;
    private Number rate_downloads_total;
    private Number rate_listened_total;

    public Number getAvgnote() {
        return this.avgnote;
    }

    public void setAvgnote(Number avgnote) {
        this.avgnote = avgnote;
    }

    public Number getDislikes() {
        return this.dislikes;
    }

    public void setDislikes(Number dislikes) {
        this.dislikes = dislikes;
    }

    public Number getFavorited() {
        return this.favorited;
    }

    public void setFavorited(Number favorited) {
        this.favorited = favorited;
    }

    public Number getLikes() {
        return this.likes;
    }

    public void setLikes(Number likes) {
        this.likes = likes;
    }

    public Number getNotes() {
        return this.notes;
    }

    public void setNotes(Number notes) {
        this.notes = notes;
    }

    public Number getPlaylisted() {
        return this.playlisted;
    }

    public void setPlaylisted(Number playlisted) {
        this.playlisted = playlisted;
    }

    public Number getRate_downloads_total() {
        return this.rate_downloads_total;
    }

    public void setRate_downloads_total(Number rate_downloads_total) {
        this.rate_downloads_total = rate_downloads_total;
    }

    public Number getRate_listened_total() {
        return this.rate_listened_total;
    }

    public void setRate_listened_total(Number rate_listened_total) {
        this.rate_listened_total = rate_listened_total;
    }
}
