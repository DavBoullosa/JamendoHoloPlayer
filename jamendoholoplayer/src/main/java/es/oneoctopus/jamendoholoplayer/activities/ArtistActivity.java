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

package es.oneoctopus.jamendoholoplayer.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvanbenschoten.motion.ParallaxImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.oneoctopus.jamendoholoplayer.R;
import es.oneoctopus.jamendoholoplayer.api.Responses.AlbumResponse;
import es.oneoctopus.jamendoholoplayer.models.Album;
import es.oneoctopus.jamendoholoplayer.models.Artist;
import es.oneoctopus.jamendoholoplayer.models.Track;
import es.oneoctopus.jamendoholoplayer.utils.Constants;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArtistActivity extends BaseJamendoActivity {
    private final String TAG = "ArtistActivity";
    private Artist artist;
    private List<Album> artistAlbums = new ArrayList<>();
    private List<Track> artistTracks = new ArrayList<>();
    private ParallaxImageView groupimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        groupimage = (ParallaxImageView) findViewById(R.id.groupimage);

        artist = (Artist) getIntent().getSerializableExtra("artist");
        if (artist != null) updateArtist();
    }

    @Override
    protected void onResume() {
        super.onResume();

        groupimage.registerSensorManager();
    }

    public void getLastAlbums() {
        getApi().getLastAlbumsByArtistId(Constants.CLIENT_ID, Constants.API_FORMAT, "releasedate", artist.getId(), Constants.IMAGES_SIZE, new Callback<AlbumResponse>() {
            @Override
            public void success(AlbumResponse albumResponse, Response response) {
                if (albumResponse.getResults().size() > 0) {
                    ObjectMapper mapper = new ObjectMapper();
                    for (int i = 0; i < albumResponse.getResults().size(); i++)
                        artistAlbums.add(mapper.convertValue(albumResponse.getResults().get(i), Album.class));
                    setAlbumsList();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ArtistActivity.this, getString(R.string.error), Toast.LENGTH_LONG).show();
                Log.e(TAG, error.getMessage());
            }
        });
    }

    private void setAlbumsList() {
    }

    public void updateArtist() {
        getActionBar().setTitle(artist.getName());
        Picasso.with(this).load(artist.getImage()).into(groupimage);
    }

    public void updateArtistAlbums() {

    }

    @Override
    protected void onPause() {
        super.onPause();

        groupimage.unregisterSensorManager();
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
}
