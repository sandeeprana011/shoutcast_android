package com.zilideus.jukebox.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zilideus.jukebox.R;
import com.zilideus.jukebox.model.Station;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Favourite extends Fragment implements FavouriteClickCallbacks {


    public static final String TITLE = "title";
    RecyclerView recyclerViewFavouriteList;
    private AdapterStationsList adapterStationsList;
    private LinearLayout linearLayoutOnNoItem;

    public Favourite() {
        // Required empty public constructor
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        linearLayoutOnNoItem = (LinearLayout) view.findViewById(R.id.lin_onno_station);
        List<Station> stationsList = Station.listAll(Station.class);
        adapterStationsList = new AdapterStationsList(getContext(), stationsList);
        adapterStationsList.setListenerFavouriteCallbacks(this);
        recyclerViewFavouriteList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFavouriteList.setAdapter(adapterStationsList);
        adapterStationsList.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.onUpdateUI();
    }

    public void onUpdateUI() {

        if (this.adapterStationsList != null) {
            List<Station> stationArrayList = Station.listAll(Station.class);
            this.adapterStationsList.addNewListAndNotifyDataSetChanged(stationArrayList);
//            adapterStationsList.notifyDataSetChanged();
        }
        if (this.adapterStationsList != null && linearLayoutOnNoItem != null) {
            if (this.adapterStationsList.isEmpty()) {
                linearLayoutOnNoItem.setVisibility(View.VISIBLE);
            } else {
                linearLayoutOnNoItem.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void favouriteAdded(Station station, int position) {
        Log.e("Added ", "To favourite");
        this.onUpdateUI();
    }

    @Override
    public void favouriteRemoved(Station station, int position) {
        adapterStationsList.removeItemAndNotify(position);
        this.onUpdateUI();
    }

}
