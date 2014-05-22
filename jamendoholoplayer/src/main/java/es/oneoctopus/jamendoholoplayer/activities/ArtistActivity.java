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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nirhart.parallaxscroll.views.ParallaxListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.oneoctopus.jamendoholoplayer.R;
import es.oneoctopus.jamendoholoplayer.adapters.TracksAdapter;
import es.oneoctopus.jamendoholoplayer.api.Responses.AlbumResponse;
import es.oneoctopus.jamendoholoplayer.api.Responses.TracksResponse;
import es.oneoctopus.jamendoholoplayer.media.Playlist;
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

    private ParallaxListView popularSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        popularSongs = (ParallaxListView) findViewById(R.id.parallaxscroll);

        artist = (Artist) getIntent().getSerializableExtra("artist");
        if (artist != null) updateArtist();
    }

    public void updateArtist() {
        getActionBar().setTitle(artist.getName());

        // Parallaxed header
        ImageView artistImage = new ImageView(this);
        artistImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.artist_cover_height));
        artistImage.setLayoutParams(params);

        if (!artist.getImage().isEmpty())
            Picasso.with(this).load(artist.getImage()).into(artistImage);

            //TODO: change this drawable
        else artistImage.setImageResource(R.drawable.playicon);

        popularSongs.addParallaxedHeaderView(artistImage, null, false);

        // Title

        View popularSongsTitle = getLayoutInflater().inflate(R.layout.header_listview_popularsongs, null);
        popularSongs.addHeaderView(popularSongsTitle, null, false);

        getPopularSongs();
    }

    public void getPopularSongs() {
        getApi().getPopularSongsByArtistId(Constants.CLIENT_ID, Constants.API_FORMAT, artist.getId(), "listens_month", Constants.IMAGES_SIZE, 5, "stats", new Callback<TracksResponse>() {

            @Override
            public void success(TracksResponse tracksResponse, Response response) {
                if (tracksResponse.getResults().size() > 0) {
                    ObjectMapper mapper = new ObjectMapper();
                    for (int i = 0; i < tracksResponse.getResults().size(); i++)
                        artistTracks.add(mapper.convertValue(tracksResponse.getResults().get(i), Track.class));
                    setArtistTracks();
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
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

    public void updateArtistAlbums() {

    }

    public void setArtistTracks() {
        TracksAdapter tracksAdapter = new TracksAdapter(this, artistTracks);
        popularSongs.setAdapter(tracksAdapter);

        popularSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent goToPlayActivity = new Intent(ArtistActivity.this, PlayerActivity.class);
                Bundle data = new Bundle();
                List<Track> tracksList = new ArrayList<>();
                // Avoiding listview headers. Position 0 is the first header, pos 1 the second one
                for (int aux = 2; aux < parent.getCount(); aux++)
                    tracksList.add((Track) parent.getItemAtPosition(aux));
                // Same way, the real position of the song is position-2
                Playlist playlist = new Playlist(tracksList, position - 2);
                data.putSerializable("playlist", playlist);
                goToPlayActivity.putExtras(data);
                startActivity(goToPlayActivity);
            }
        });
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
