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

package es.oneoctopus.jamendoapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import es.oneoctopus.jamendoapp.R;
import es.oneoctopus.jamendoapp.api.Responses.ArtistResponse;
import es.oneoctopus.jamendoapp.models.Artist;
import es.oneoctopus.jamendoapp.utils.Constants;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArtistActivity extends BaseJamendoActivity {
    private final String TAG = "ArtistActivity";
    private Artist artist;
    private long id;
    private ImageView groupimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        groupimage = (ImageView) findViewById(R.id.groupimage);

        id = getIntent().getLongExtra("id", -1);
        if (id != -1) getArtist();
    }

    public void getArtist() {
        getApi().getArtistById(Constants.CLIENT_ID, "json", id, new Callback<ArtistResponse>() {

            @Override
            public void success(ArtistResponse artistResponse, Response response) {
                if (artistResponse.getResults().size() > 0) {
                    ObjectMapper mapper = new ObjectMapper();
                    artist = mapper.convertValue(artistResponse.getResults().get(0), Artist.class);
                    updateUI();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }

    public void updateUI() {
        getActionBar().setTitle(artist.getName());
        Picasso.with(this).load(artist.getImage()).into(groupimage);
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
