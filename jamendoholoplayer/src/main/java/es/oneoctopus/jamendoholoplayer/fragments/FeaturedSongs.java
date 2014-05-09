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

package es.oneoctopus.jamendoholoplayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import es.oneoctopus.jamendoholoplayer.R;
import es.oneoctopus.jamendoholoplayer.activities.PlayerActivity;
import es.oneoctopus.jamendoholoplayer.adapters.TracksAdapter;
import es.oneoctopus.jamendoholoplayer.api.Responses.TracksResponse;
import es.oneoctopus.jamendoholoplayer.media.Playlist;
import es.oneoctopus.jamendoholoplayer.models.Track;
import es.oneoctopus.jamendoholoplayer.utils.Constants;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FeaturedSongs extends BaseJamendoFragment {
    private final String TAG = "Main";
    private ListView featuredList;
    private ArrayList<Track> featuredTracks = new ArrayList<>();
    private TracksAdapter tracksAdapter;

    public FeaturedSongs() {
    }

    public static FeaturedSongs newInstance() {
        return new FeaturedSongs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        featuredList = (ListView) root.findViewById(R.id.featuredlist);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("featuredTracks"))
                featuredTracks = (ArrayList<Track>) savedInstanceState.get("featuredTracks");
            setAdapterAndListener();
        } else {
            getApi().getFeaturedTracks(Constants.CLIENT_ID, "json", "popularity_week", true, Constants.IMAGES_SIZE, Constants.LIST_LIMIT, "stats", new Callback<TracksResponse>() {
                @Override
                public void success(TracksResponse tracksResponse, Response response) {
                    if (tracksResponse.getResults().size() > 0) {
                        ObjectMapper mapper = new ObjectMapper();
                        for (int i = 0; i < tracksResponse.getResults().size(); i++)
                            featuredTracks.add(mapper.convertValue(tracksResponse.getResults().get(i), Track.class));
                        setAdapterAndListener();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    Log.e(TAG, error.getMessage());
                }
            });
        }
    }

    public void setAdapterAndListener() {
        tracksAdapter = new TracksAdapter(getActivity(), featuredTracks);
        featuredList.setAdapter(tracksAdapter);
        featuredList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent goToPlayActivity = new Intent(getActivity(), PlayerActivity.class);

                Bundle data = new Bundle();

                List<Track> tracksList = new ArrayList<>();
                tracksList.addAll(((TracksAdapter) parent.getAdapter()).getAllItems());
                Playlist playlist = new Playlist(tracksList, position);
                data.putSerializable("playlist", playlist);

                goToPlayActivity.putExtras(data);
                startActivity(goToPlayActivity);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("featuredTracks", featuredTracks);
    }
}
