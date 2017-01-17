package com.zilideus.jukebox.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zilideus.jukebox.R;
import com.zilideus.jukebox.flags.Flags;
import com.zilideus.jukebox.flags.Url_format;
import com.zilideus.jukebox.model.Station;
import com.zilideus.jukebox.network.DownloadContent;
import com.zilideus.jukebox.parser.ParserXMLtoJSON;
import com.zilideus.jukebox.parser.StationList;
import com.zilideus.jukebox.parser.TuneIn;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollStateListener;
import me.everything.android.ui.overscroll.ListenerStubs;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopListFragment extends Fragment {


    private static final String TAG = "TopListFragment";
    AdapterStationsList adapterStationsList;
    private SharedPreferences preferences;
    private IOverScrollDecor decor;
    private ScrollView scrollView;
    private RecyclerView recyclerView;
    private int offsetOfStation = 0;
    private ProgressBar progressBar;
    private int maxLimit = 20;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        preferences = getActivity().getSharedPreferences(Flags.SETTINGS, Context
                .MODE_PRIVATE);

        return inflater.inflate(R.layout.station_list, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scrollView = (ScrollView) view;
        recyclerView = (RecyclerView) view.findViewById(R.id.list_stations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = (ProgressBar) view.findViewById(R.id.progressbarlist);
        adapterStationsList = new AdapterStationsList(getActivity());
        recyclerView.setAdapter(adapterStationsList);


    }


    @Override
    public void onResume() {
        super.onResume();

        searchAndUpdateList(null);
//        if (DownloadContent.isAvailable(getActivity())) {
//
//            DownloadAndShowList downloadAndShowList = new DownloadAndShowList(getView());
//            downloadAndShowList.execute(url);
//
//        } else {
//            Toast.makeText(getActivity(), "Network connection not available", Toast.LENGTH_LONG).show();
//        }
    }

    private void searchAndUpdateList(View view) {
        resetOffsetStation();

        String url = new Url_format().getTopStationsXML(
                Flags.DEV_ID,
                String.valueOf(getOffsetOfStation()),
                String.valueOf(getMaxLimit()),
                null, null);

        decor = OverScrollDecoratorHelper.setUpOverScroll((ScrollView) scrollView);
        decor.setOverScrollStateListener(new IOverScrollStateListener() {
            @Override
            public void onOverScrollStateChange(IOverScrollDecor decor, int oldState, int newState) {
                if (oldState == 3 && newState == 0) {
                    Log.e(TAG, String.format("%d,%d", oldState, newState));
                    addMoreStations();
                }
            }
        });

        showProgressBar();

        adapterStationsList.clearList();


//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        if (recyclerView != null) {
//            String urlToSearch = new Url_format().getStationByKeywords(Flags.DEV_ID,
//                    editTextSearchStrin.getText().toString(), String.valueOf(getOffsetOfStation()), String.valueOf(getMaxLimit()), null, null);
            showProgressBar();

            StringRequest request = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ParserXMLtoJSON parser = new ParserXMLtoJSON();
                            hideProgressBar();
                            try {
                                ArrayList<Station> list = parser.getTopStationswithLIMIT(response).getArrayListStations();
                                adapterStationsList.addNewListAndNotifyDataSetChanged(list);
                                increaseCounterOfSearch();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", "" + error.getMessage());

                            hideProgressBar();
                        }
                    });
            Volley.newRequestQueue(getContext()).add(request);
        }
    }

    private void resetOffsetStation() {
        setOffsetOfStation(0);
    }

    private void addMoreStations() {
        String url = new Url_format().getTopStationsXML(
                Flags.DEV_ID,
                String.valueOf(getOffsetOfStation()),
                String.valueOf(getMaxLimit()),
                null, null);


        showProgressBar();
        if (recyclerView != null) {


            StringRequest request = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ParserXMLtoJSON parser = new ParserXMLtoJSON();
                            hideProgressBar();
                            try {
                                ArrayList<Station> list = parser.getTopStationswithLIMIT(response).getArrayListStations();
                                adapterStationsList.addMoreStations(list);
                                increaseCounterOfSearch();
                                if (list == null || !(list.size() > 0)) {
                                    decor.setOverScrollStateListener(new ListenerStubs.OverScrollStateListenerStub());
                                }


                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                            hideProgressBar();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", "" + error.getMessage());

                            hideProgressBar();
                        }
                    });
            Volley.newRequestQueue(getContext()).add(request);
        }
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.requestFocus();
    }

    private void increaseCounterOfSearch() {
        setOffsetOfStation(getOffsetOfStation() + getMaxLimit());
    }

    public int getOffsetOfStation() {
        return offsetOfStation;
    }

    public void setOffsetOfStation(int offsetOfStation) {
        this.offsetOfStation = offsetOfStation;
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }


    /**
     * Class downloads the list and shows on the ListView
     */
    @Deprecated
    private class DownloadAndShowList extends AsyncTask<String, Void, ArrayList<Station>> {
        //        ImageButton listButton;
        StationList stationList;
        RecyclerView recyclerView;
        ProgressBar progressBar;
        ParserXMLtoJSON parser;
        Url_format url_format;
        TuneIn tuneIn;
        private View view;

        DownloadAndShowList(@NonNull View view) {
            this.view = view;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = (ProgressBar) view.findViewById(R.id.progressbarlist);
            progressBar.setVisibility(View.VISIBLE);
            parser = new ParserXMLtoJSON();
            url_format = new Url_format();
            recyclerView = (RecyclerView) view.findViewById(R.id.list_stations);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    Log.e(TAG, String.valueOf(newState));
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Log.e(TAG, String.valueOf(dx) + " dy " + String.valueOf(dy));
                }
            });


//            listButton = (ImageButton) view.getRootView().findViewById(R.id.but_media_list);
//            if (listButton != null) {
//                listButton.setEnabled(false);
//            }


        }

        @Override
        protected ArrayList<Station> doInBackground(String... params) {
            try {
                String data = DownloadContent.downloadContent(params[0]);
                stationList = parser.getTopStationswithLIMIT(data);
                tuneIn = stationList.getTuneIn();
                if (stationList != null && stationList.getArrayListStations() != null) {
//                    Collections.reverse(stationList.getArrayListStations());
                    return stationList.getArrayListStations();
                } else return null;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<Station> stations) {

            if (recyclerView != null && stations != null && stations.size() > 0) {
                super.onPostExecute(stations);

//                recyclerView.setAdapter(new ListAdapterStations(getActivity().getApplicationContext(), R.layout.itemlistrow,
//                        stations));
                AdapterStationsList adapterStationsList = new AdapterStationsList(getActivity(), stations);
                recyclerView.setAdapter(adapterStationsList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                if (listButton != null) {
//                    listButton.setEnabled(true);
//                }
                progressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(getContext(), "No station found", Toast.LENGTH_LONG).show();
                preferences.edit().putString(Flags.JSON_URL, new Url_format().getTopStationsXML(Flags
                        .DEV_ID, "0", "30", null, null)).apply();
                if (progressBar != null && progressBar.isShown()) {
                    progressBar.setVisibility(View.GONE);
                }

            }

        }


    }

}
