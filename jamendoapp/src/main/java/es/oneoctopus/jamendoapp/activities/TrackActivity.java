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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import es.oneoctopus.jamendoapp.R;
import es.oneoctopus.jamendoapp.models.Track;

public class TrackActivity extends BaseJamendoActivity {
    private final String TAG = "TrackActivity";
    private Track track;
    private long id;
    private ImageView trackimage;
    private TextView tracktitle;
    private TextView trackartist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        trackimage = (ImageView) findViewById(R.id.trackimage);
        tracktitle = (TextView) findViewById(R.id.titletrack);
        trackartist = (TextView) findViewById(R.id.artistname);

        if (getIntent().getExtras() != null) {
            track = (Track) getIntent().getExtras().getSerializable("track");
            updateUIWithTrack();
        }
    }

    public void updateUIWithTrack() {
        getActionBar().setTitle(track.getName());
        Picasso.with(this).load(track.getAlbum_image()).into(trackimage);
        tracktitle.setText(track.getName());
        trackartist.setText(track.getArtist_name());
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
