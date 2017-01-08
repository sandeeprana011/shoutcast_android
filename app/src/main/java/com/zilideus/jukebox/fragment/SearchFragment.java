package com.zilideus.jukebox.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zilideus.jukebox.AdapterSearchStationList;
import com.zilideus.jukebox.R;
import com.zilideus.jukebox.flags.Flags;
import com.zilideus.jukebox.flags.Url_format;
import com.zilideus.jukebox.model.Station;
import com.zilideus.jukebox.parser.ParserXMLtoJSON;

import org.json.JSONException;

import java.io.IOException;

import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollStateListener;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class SearchFragment extends Fragment implements View.OnClickListener, FavouriteClickCallbacks {
    public static final String TITLE = "search_fragment";
    private static final String TAG = "SearchFragment";
    int offsetOfStation = 0, maxLimit = 20;
    private EditText editTextSearchStrin;
    private Button buttonSearch;
    private RecyclerView recyclerViewSearch;
    private AdapterSearchStationList adapterStationsList;
    private ProgressBar progressBar;
    private boolean isLoadingStations;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextSearchStrin = (EditText) view.findViewById(R.id.search_text_station);
        recyclerViewSearch = (RecyclerView) view.findViewById(R.id.rv_search_fragment);
        ScrollView scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
//        scrollView.setSmoothScrollingEnabled(true);
//        scrollView.fling(10);

        adapterStationsList = new AdapterSearchStationList(getContext());
        adapterStationsList.setListenerFavouriteCallbacks(this);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setAdapter(adapterStationsList);
        recyclerViewSearch.setNestedScrollingEnabled(false);
        recyclerViewSearch.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) { // on scroll stop
                            Log.e(TAG, String.valueOf(newState) + "new state");
                        }
                    }
                });
        recyclerViewSearch.setItemAnimator(new FadeInDownAnimator());
//        recyclerViewSearch.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                Log.e(TAG, String.valueOf(newState) + "new state");
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                Log.e(TAG, String.valueOf(dx) + "new state");
//            }
//        });

        IOverScrollDecor decor = OverScrollDecoratorHelper.setUpOverScroll((ScrollView) view);
        decor.setOverScrollStateListener(new IOverScrollStateListener() {
            @Override
            public void onOverScrollStateChange(IOverScrollDecor decor, int oldState, int newState) {
                if (oldState == 3 && newState == 0) {
                    Log.e(TAG, String.format("%d,%d", oldState, newState));
                    addMoreStations();
                }
            }
        });
//        FlingBehavior behavior = new FlingBehavior();
//        recyclerViewSearch.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
//        ((ScrollView) view).setOverScrollMode(View.OVER_SCROLL_ALWAYS);


        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        buttonSearch = (Button) view.findViewById(R.id.search_buttom);

        buttonSearch.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_buttom:
                searchAndUpdateList(v);
                break;
        }
    }

    private void searchAndUpdateList(View view) {

        adapterStationsList.clearList();

        setLoadingStationsStatus(true);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        resetOffsetStation();

        if (recyclerViewSearch != null) {
            String urlToSearch = new Url_format().getStationByKeywords(Flags.DEV_ID,
                    editTextSearchStrin.getText().toString(), String.valueOf(getOffsetOfStation()), String.valueOf(getMaxLimit()), null, null);
            progressBar.setVisibility(View.VISIBLE);

            StringRequest request = new StringRequest(Request.Method.GET,
                    urlToSearch,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ParserXMLtoJSON parser = new ParserXMLtoJSON();
                            progressBar.setVisibility(View.GONE);
                            try {
                                adapterStationsList
                                        .addNewListAndNotifyDataSetChanged(
                                                parser.getTopStationswithLIMIT(response).getArrayListStations()
                                        );
                                increaseCounterOfSearch();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                            setLoadingStationsStatus(false);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", "" + error.getMessage());
                            setLoadingStationsStatus(false);
                        }
                    });
            Volley.newRequestQueue(getContext()).add(request);
        }
    }

    private void resetOffsetStation() {
        setOffsetOfStation(0);
    }

    private void addMoreStations() {

//        adapterStationsList.clearList();

        setLoadingStationsStatus(true);
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        if (recyclerViewSearch != null) {

            String urlToSearch = new Url_format().getStationByKeywords(Flags.DEV_ID,
                    editTextSearchStrin.getText().toString(), String.valueOf(offsetOfStation), String.valueOf(20), null, null);
            progressBar.setVisibility(View.VISIBLE);

            StringRequest request = new StringRequest(Request.Method.GET,
                    urlToSearch,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ParserXMLtoJSON parser = new ParserXMLtoJSON();
                            progressBar.setVisibility(View.GONE);
                            try {
                                adapterStationsList
                                        .addMoreStations(
                                                parser.getTopStationswithLIMIT(response).getArrayListStations()
                                        );
                                increaseCounterOfSearch();

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                            setLoadingStationsStatus(false);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", "" + error.getMessage());
                            setLoadingStationsStatus(false);
                        }
                    });
            Volley.newRequestQueue(getContext()).add(request);
        }
    }

    private void increaseCounterOfSearch() {
        setOffsetOfStation(getOffsetOfStation() + getMaxLimit());
    }

    @Override
    public void favouriteAdded(Station station, int position) {
//        if (station != null) {
//            station.save();
//        }
    }

    @Override
    public void favouriteRemoved(Station station, int position) {
//        if (station != null) {
//            station.delete();
//        }
    }

    public boolean isLoadingStations() {
        return isLoadingStations;
    }

    public void setLoadingStationsStatus(boolean loadingStations) {
        isLoadingStations = loadingStations;
    }
}