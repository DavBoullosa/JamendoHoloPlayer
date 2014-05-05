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

package es.oneoctopus.jamendoapp.interfaces;

import es.oneoctopus.jamendoapp.api.Response.ArtistResponse;
import es.oneoctopus.jamendoapp.api.Response.TracksResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface RestMethods {

    @GET("/artists/")
    void getArtistById(
            @Query("client_id") String clientid,
            @Query("format") String format,
            @Query("id") long id,
            Callback<ArtistResponse> callback);

    @GET("/tracks/")
    void getFeaturedTracks(
            @Query("client_id") String clientid,
            @Query("format") String format,
            @Query("order") String order,
            @Query("featured") boolean featured,
            @Query("imagesize") int imagesize,
            @Query("limit") int limit,
            @Query("include") String include,
            Callback<TracksResponse> callback);
}
