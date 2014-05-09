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

package es.oneoctopus.jamendoholoplayer.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.oneoctopus.jamendoholoplayer.R;
import es.oneoctopus.jamendoholoplayer.models.Track;

public class TracksAdapter extends BaseAdapter {
    private Activity activity;
    private List<Track> items;
    private Track track;

    public TracksAdapter(Activity activity, List<Track> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    public List<Track> getAllItems() {
        return items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater ly = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = ly.inflate(R.layout.list_track, null);
        }

        track = items.get(position);

        ImageView cover = (ImageView) v.findViewById(R.id.trackcover);
        TextView title = (TextView) v.findViewById(R.id.titletrack);
        TextView artist = (TextView) v.findViewById(R.id.artistname);

        Picasso.with(activity).load(track.getAlbum_image()).into(cover);
        title.setText(track.getName());
        artist.setText(track.getArtist_name());

        return v;
    }
}
