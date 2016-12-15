package com.zilideus.jukebox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zilideus.jukebox.model.Station;

import java.util.List;

@Deprecated
public class ListAdapterStations extends ArrayAdapter<Station> {


    @Deprecated
    public ListAdapterStations(Context context, int resource, List<Station> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.itemlistrow, null);
        }

        Station station = getItem(position);

        if (station != null) {
            TextView title = (TextView) v.findViewById(R.id.text_list_title);
            TextView text = (TextView) v.findViewById(R.id.text_list_text);
            TextView bt = (TextView) v.findViewById(R.id.text_list_bt);
            TextView genre = (TextView) v.findViewById(R.id.text_list_genre);

            if (title != null) {
                title.setText(station.getName());
            }

            if (text != null) {
                text.setText(station.getCtqueryString());
            }

            if (bt != null) {
                bt.setText(station.getBrbitrate() + " Hz");
            }

            if (genre != null) {
                genre.setText(station.getGenre());
            }
        }

        return v;
    }

}