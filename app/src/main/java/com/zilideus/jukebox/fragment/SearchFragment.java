package com.zilideus.jukebox.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zilideus.jukebox.R;
import com.zilideus.jukebox.flags.Flags;
import com.zilideus.jukebox.flags.Url_format;
import com.zilideus.jukebox.model.Station;
import com.zilideus.jukebox.parser.ParserXMLtoJSON;

import org.json.JSONException;

import java.io.IOException;

public class SearchFragment extends Fragment implements View.OnClickListener, FavouriteClickCallbacks {
    public static final String TITLE = "search_fragment";
    private EditText editTextSearchStrin;
    private Button buttonSearch;
    private RecyclerView recyclerViewSearch;
    private AdapterStationsList adapterStationsList;
    private ProgressBar progressBar;

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
        adapterStationsList = new AdapterStationsList(getContext());
        adapterStationsList.setListenerFavouriteCallbacks(this);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setAdapter(adapterStationsList);
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

        if (recyclerViewSearch != null) {

            String urlToSearch = new Url_format().getStationByKeywords(Flags.DEV_ID,
                    editTextSearchStrin.getText().toString(), "30", null, null);
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
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", "" + error.getMessage());
                        }
                    });
            Volley.newRequestQueue(getContext()).add(request);
        }
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
}