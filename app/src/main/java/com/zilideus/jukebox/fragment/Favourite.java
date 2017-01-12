package com.zilideus.jukebox.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zilideus.jukebox.R;
import com.zilideus.jukebox.model.Station;
import com.zilideus.jukebox.model.StationAddedManually;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Favourite extends Fragment implements FavouriteClickCallbacks, View.OnClickListener, OnNewStationSaved {


    public static final String TITLE = "title";
    private static final String TAG = "FavouriteFragment";
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
        FloatingActionButton addActionBar = (FloatingActionButton) view.findViewById(R.id.fb_add_manual_station);
        addActionBar.setOnClickListener(this);


//        StationAddedManually stationAddedManually = new StationAddedManually();
//        stationAddedManually.setName("Some");
//        stationsList.add(stationAddedManually);

        adapterStationsList = new AdapterStationsList(getContext());
        adapterStationsList.setListenerFavouriteCallbacks(this);
        adapterStationsList.setOnStationSavedOrEditedListener(this);
        recyclerViewFavouriteList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFavouriteList.setAdapter(adapterStationsList);
        adapterStationsList.notifyDataSetChanged();
    }

    private List<Station> getCompleteList() {

        List<Station> stationsList = Station.listAll(Station.class);
        List<StationAddedManually> stationAddedManuallies = StationAddedManually.listAll(StationAddedManually.class);
        List<Station> completeList = new ArrayList<>();
        completeList.addAll(stationAddedManuallies);
        completeList.addAll(stationsList);
        return completeList;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.onUpdateUI();
    }

    public void onUpdateUI() {

        if (this.adapterStationsList != null) {

            this.adapterStationsList.addNewListAndNotifyDataSetChanged(getCompleteList());
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

    @Override
    public void favrouriteDeleted(StationAddedManually manually, int adapterPosition) {
        adapterStationsList.removeItemAndNotify(adapterPosition);
        this.onUpdateUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb_add_manual_station:
                openDialogForCustomFavourite(v);
                break;

        }

    }

    public void openDialogForCustomFavourite(View view) {
        // TODO: 08/01/17 Open Edit Dialog
        DialogSurvey dialogSurvey = DialogSurvey.getSingletonInstance("Add Station");
        FragmentManager manager = getActivity().getSupportFragmentManager();
        dialogSurvey.show(manager, DialogSurvey.TITLE);
        dialogSurvey.setOnSaveListener(this);

    }


    @Override
    public void onStationSaved(StationAddedManually stationAddedManually) {
        this.onUpdateUI();
    }
}
