package com.zilideus.jukebox.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zilideus.jukebox.R;
import com.zilideus.jukebox.model.Station;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Favourite extends Fragment {


    public static final String TITLE = "title";
    RecyclerView recyclerViewFavouriteList;
    private AdapterStationsList adapterStationsList;

    public Favourite() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewFavouriteList = (RecyclerView) view.findViewById(R.id.list_stations);
        List<Station> stationsList = Station.listAll(Station.class);
        adapterStationsList = new AdapterStationsList(getContext(), stationsList);
        recyclerViewFavouriteList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFavouriteList.setAdapter(adapterStationsList);
        adapterStationsList.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapterStationsList != null) {
            adapterStationsList.addNewListAndNotifyDataSetChanged(Station.listAll(Station.class));
        }
    }
}
