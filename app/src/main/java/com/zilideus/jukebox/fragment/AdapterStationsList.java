package com.zilideus.jukebox.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zilideus.jukebox.MainActivity;
import com.zilideus.jukebox.MyService;
import com.zilideus.jukebox.R;
import com.zilideus.jukebox.flags.JKeys;
import com.zilideus.jukebox.model.Station;
import com.zilideus.jukebox.model.StationAddedManually;
import com.zilideus.jukebox.utilities.DownloadSongDetailAndPlayOnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeeprana on 14/12/16.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */
class AdapterStationsList extends RecyclerView.Adapter<AdapterStationsList.ViewHolder> implements OnNewStationSaved {


    private final Context context;
    private final List<Station> stationList;
    private final TextDrawable.IBuilder builder;
    private DownloadSongDetailAndPlayOnClick downloadSongDetailAndPlayOnclick;
    private FavouriteClickCallbacks listenerFavouriteCallbacks;
    private OnNewStationSaved onStationSavedOrEditedListener;

    AdapterStationsList(Context context, List<Station> stationList) {

        builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(0)
                .endConfig()
                .round();

        this.context = context;
        this.stationList = stationList;
    }

    AdapterStationsList(Context context) {

        builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(0)
                .endConfig()
                .round();

        this.context = context;
        this.stationList = new ArrayList<>();
    }

    public void setListenerFavouriteCallbacks(FavouriteClickCallbacks listenerFavouriteCallbacks) {
        this.listenerFavouriteCallbacks = listenerFavouriteCallbacks;
    }

    @Override
    public AdapterStationsList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Station.TYPE_NONE:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.itemlistrow, parent, false));
            case Station.TYPE_ADDED_AS_FAVOURITE_BY_CLICK:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.itemlistrow, parent, false));
            case Station.TYPE_MANUALLY_ADDED:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.itemlistrow, parent, false));
            default:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.itemlistrow, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return this.stationList.get(position).getType();
    }

    @Override
    public void onBindViewHolder(AdapterStationsList.ViewHolder holder, int position) {
        Station station = this.stationList.get(position);

        holder.title.setText(station.getName());
        holder.text.setText(station.getCtqueryString());
        if (station.getBrbitrate() != null) {
            holder.bt.setText(station.getBrbitrate() + " Hz");
        } else {
            holder.bt.setVisibility(View.GONE);
        }
        holder.genre.setText(station.getGenre());
        if (station.getLc() != null) {
            holder.peoplesListening.setText("Listeners : " + station.getLc());
        } else {
            holder.peoplesListening.setVisibility(View.GONE);
        }


        if (station.getLogo() != null) {
            Glide.with(context)
                    .load(station.getLogo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageLogo);
        } else {

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

            if (generator != null && station != null && builder != null && station.getGenre() != null) {
                int color = generator.getColor(station.getGenre());
                TextDrawable drawable = null;
                if (station.getName() != null && station.getName().length() > 0) {
                    drawable = builder.build(String.valueOf(station.getName().charAt(0)).toUpperCase(), color);
                } else {
                    drawable = builder.build("#", color);
                }

                holder.imageLogo.setImageDrawable(drawable);
            }
        }


        switch (station.getType()) {
            case Station.TYPE_MANUALLY_ADDED:
                holder.imageGearSettings.setVisibility(View.VISIBLE);
                holder.imageFavourite.setImageResource(R.drawable.ic_delete);
                break;
            default:
                holder.imageGearSettings.setVisibility(View.GONE);
                if (station.isFavourite()) {
                    holder.imageFavourite.setImageResource(R.drawable.favourite);
                } else {
                    holder.imageFavourite.setImageResource(R.drawable.favourite_grey);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.stationList.size();
    }

    public void appendAndNotifyDataSetChanged(List<Station> stationList) {
        this.stationList.addAll(stationList);
        this.notifyDataSetChanged();
    }

    public void addNewListAndNotifyDataSetChanged(List<Station> stationList) {
        if (this.stationList != null && stationList != null) {
            this.stationList.clear();
            this.stationList.addAll(stationList);
            this.notifyDataSetChanged();
        } else {
            Log.e("Null", "Stations List");
        }
    }

    public void addMoreStations(List<Station> stationList) {
        if (stationList != null) {
            this.stationList.addAll(stationList);
            this.notifyDataSetChanged();
        }
    }

    public void removeItemAndNotify(int adapterPosition) {
        this.stationList.remove(adapterPosition);
        this.notifyDataSetChanged();
    }

    public void clearList() {
        this.stationList.clear();
    }

    public boolean isEmpty() {
        return this.stationList.isEmpty();
    }

    private void bindWithServiceAndExecute(final Station station) {
        Intent intent = new Intent(context, MyService.class);
        context.bindService(intent, new ServiceConnection() {
            public static final String TAG = "AdapterStationsList BS";

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MyService.ServiceBinder servicBinder = (MyService.ServiceBinder) iBinder;
                MyService myServiceEngine = servicBinder.getService();
//  				myServiceEngine.prepare(url,"icy://37.130.230.93:9092");
                try {
                    myServiceEngine.prepare(station);
                } catch (Exception ex) {
                    Log.e(TAG, "while preparing for " + getClass().getName());
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.e(TAG, "service");
            }
        }, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onStationSaved(StationAddedManually stationAddedManually) {
        this.notifyDataSetChanged();
    }

    public void setOnStationSavedOrEditedListener(OnNewStationSaved onStationSavedOrEditedListener) {
        this.onStationSavedOrEditedListener = onStationSavedOrEditedListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageLogo, imageFavourite, imageGearSettings;
        private final TextView title;
        private final TextView text;
        private final TextView bt;
        private final TextView genre;
        private final TextView peoplesListening;

        ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.text_list_title);
            text = (TextView) v.findViewById(R.id.text_list_text);
            peoplesListening = (TextView) v.findViewById(R.id.text_list_listeners);
            bt = (TextView) v.findViewById(R.id.text_list_bt);
            genre = (TextView) v.findViewById(R.id.text_list_genre);
            imageLogo = (ImageView) v.findViewById(R.id.logo);
            imageFavourite = (ImageView) v.findViewById(R.id.favourite);
            imageGearSettings = (ImageView) v.findViewById(R.id.edit_manual_favourite);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Station station = stationList.get(ViewHolder.this.getAdapterPosition());
                    switch (station.getType()) {
                        case Station.TYPE_MANUALLY_ADDED:
                            bindWithServiceAndExecute(station);
                            break;
                        default:
                            if (downloadSongDetailAndPlayOnclick != null) {
                                downloadSongDetailAndPlayOnclick.cancel(true);
                            }
                            downloadSongDetailAndPlayOnclick = new DownloadSongDetailAndPlayOnClick(station, (MainActivity) context);
                            downloadSongDetailAndPlayOnclick.execute();
                            break;

                    }

                }
            });

            imageFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Station station = stationList.get(ViewHolder.this.getAdapterPosition());
                    Log.e("Favorite", "clicked" + station.getStationId());
                    switch (station.getType()) {
                        case Station.TYPE_MANUALLY_ADDED:
                            StationAddedManually manually = (StationAddedManually) station;
                            manually.delete(manually);
                            listenerFavouriteCallbacks.favrouriteDeleted(manually, getAdapterPosition());
                            break;
                        default:
                            if (station.isFavourite()) {
                                station.delete();
                                ((ImageView) view).setImageResource(R.drawable.favourite_grey);
                                if (AdapterStationsList.this.listenerFavouriteCallbacks != null) {
                                    listenerFavouriteCallbacks.favouriteRemoved(station, getAdapterPosition());
                                }
                            } else {
                                Station.save(station);
                                ((ImageView) view).setImageResource(R.drawable.favourite);
                                if (listenerFavouriteCallbacks != null)
                                    listenerFavouriteCallbacks.favouriteAdded(station, getAdapterPosition());
                            }
                            break;
                    }

                }
            });

            imageGearSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Station station = stationList.get(ViewHolder.this.getAdapterPosition());
                    Log.e("GearClicked", "clicked" + station.getStationId());
                    switch (station.getType()) {
                        case Station.TYPE_MANUALLY_ADDED:
                            // TODO: 08/01/17 Open Edit Dialog
                            DialogSurvey dialogSurvey = DialogSurvey.getSingletonInstance("Add Station");
                            Bundle bundle = new Bundle();
                            bundle.putString(JKeys.NAME, station.getName());
                            bundle.putString(JKeys.DESCRIPTION, station.getCtqueryString());
                            bundle.putString(JKeys.URI, ((StationAddedManually) station).getUrlstation());
                            bundle.putString(JKeys.LOGO, station.getLogo());
                            bundle.putString(JKeys.ID, station.getStationId());
                            dialogSurvey.setArguments(bundle);
                            FragmentManager manager = ((MainActivity) context).getSupportFragmentManager();
                            dialogSurvey.show(manager, DialogSurvey.TITLE);
                            dialogSurvey.setOnSaveListener(onStationSavedOrEditedListener);
                            break;
                        default:
                            break;
                    }
                }
            });


        }


    }
}
