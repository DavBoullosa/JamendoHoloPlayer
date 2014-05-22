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
import java.net.URI;
import java.net.URISyntaxException;

@JsonRootName(value = "result")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist implements Serializable {

    private long id;
    private String image;
    private String joindate;
    private String name;
    private String shareurl;
    private String shorturl;
    private String website;

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

        /*
        Apparently Picasso doesn't handle right URL containing whitespaces, which is kinda weird,
        so we replace them. This can't be avoided if other library is used.
         */

        try {
            URI imageUri = new URI(image.replace(" ", "%20"));
            image = imageUri.toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.image = image;
    }

    public String getJoindate() {
        return this.joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
