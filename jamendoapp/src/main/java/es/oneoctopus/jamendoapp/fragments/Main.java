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

package es.oneoctopus.jamendoapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import es.oneoctopus.jamendoapp.R;
import es.oneoctopus.jamendoapp.activities.TrackActivity;
import es.oneoctopus.jamendoapp.adapters.TracksAdapter;
import es.oneoctopus.jamendoapp.api.Response.TracksResponse;
import es.oneoctopus.jamendoapp.models.Track;
import es.oneoctopus.jamendoapp.utils.Constants;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Main extends BaseJamendoFragment {
    private final String TAG = "Main";
    private ListView featuredList;
    private Spinner featuredOptions;
    // List doesn't implement Serializable
    private ArrayList<Track> featuredTracks = new ArrayList<>();
    private TracksAdapter tracksAdapter;

    public Main() {
    }

    public static Main newInstance() {
        return new Main();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        featuredList = (ListView) root.findViewById(R.id.featuredlist);
        View featuredheader = View.inflate(getActivity(), R.layout.featured_header, null);
        featuredOptions = (Spinner) featuredheader.findViewById(R.id.featuredoptions);
        ArrayAdapter<String> featuredAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_title, getResources().getStringArray(R.array.featured_home));
        featuredOptions.setAdapter(featuredAdapter);
        featuredList.addHeaderView(featuredheader, null, false);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            System.out.println("SAVED");
            featuredTracks = (ArrayList<Track>) savedInstanceState.get("featuredTracks");
            setAdapterAndListener();
        } else {
            System.out.println("NOT SAVED");
            getApi().getFeaturedTracks(Constants.CLIENT_ID, "json", "popularity_week", true, 600, 15, "stats", new Callback<TracksResponse>() {
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
                Intent goToTrackActivity = new Intent(getActivity(), TrackActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("track", (Track) parent.getItemAtPosition(position));
                goToTrackActivity.putExtras(data);
                startActivity(goToTrackActivity);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("featuredTracks", featuredTracks);
        super.onSaveInstanceState(outState);
    }
}
