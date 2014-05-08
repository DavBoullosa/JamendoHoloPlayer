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

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import es.oneoctopus.jamendoapp.R;
import es.oneoctopus.jamendoapp.adapters.ArtistsAdapter;
import es.oneoctopus.jamendoapp.api.Responses.ArtistResponse;
import es.oneoctopus.jamendoapp.models.Artist;
import es.oneoctopus.jamendoapp.utils.Constants;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FeaturedArtists extends BaseJamendoFragment {
    private final String TAG = "Main";
    private ListView featuredList;
    private ArrayList<Artist> featuredArtists = new ArrayList<>();
    private ArtistsAdapter artistsAdapter;

    public FeaturedArtists() {
    }

    public static FeaturedArtists newInstance() {
        return new FeaturedArtists();
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
            if (savedInstanceState.containsKey("featuredArtists"))
                featuredArtists = (ArrayList<Artist>) savedInstanceState.get("featuredArtists");
            setAdapterAndListener();
        } else {
            getApi().getPopularArtists(Constants.CLIENT_ID, "json", "popularity_week", Constants.LIST_LIMIT, new Callback<ArtistResponse>() {
                @Override
                public void success(ArtistResponse artistResponse, Response response) {
                    if (artistResponse.getResults().size() > 0) {
                        ObjectMapper mapper = new ObjectMapper();
                        for (int i = 0; i < artistResponse.getResults().size(); i++)
                            featuredArtists.add(mapper.convertValue(artistResponse.getResults().get(i), Artist.class));
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
        artistsAdapter = new ArtistsAdapter(getActivity(), featuredArtists);
        featuredList.setAdapter(artistsAdapter);
//        featuredList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent goToTrackActivity = new Intent(getActivity(), PlayerActivity.class);
//
//                Bundle data = new Bundle();
//                List<Track> tracksList = new ArrayList<>();
//                tracksList.add((Track) parent.getItemAtPosition(position));
//                Playlist playlist = new Playlist(tracksList);
//                data.putSerializable("playlist", playlist);
//
//                goToTrackActivity.putExtras(data);
//                startActivity(goToTrackActivity);
//            }
//        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("featuredArtists", featuredArtists);
    }
}
