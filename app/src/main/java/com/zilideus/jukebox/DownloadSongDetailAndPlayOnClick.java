package com.zilideus.jukebox;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zilideus.jukebox.flags.Flags;
import com.zilideus.jukebox.network.DownloadContent;
import com.zilideus.jukebox.parser.Station;

import java.util.ArrayList;

public class DownloadSongDetailAndPlayOnClick extends AsyncTask<Station, Void, ArrayList<Uri>> {
    ImageButton imageButtonPlay;
    String file;
    private Station station;
    private MainActivity activity;


    public DownloadSongDetailAndPlayOnClick(Station station, MainActivity activity) {
        this.station = station;
        this.activity = activity;
    }


    @Override
    protected void onPreExecute() {
        TextView textTitle, textDesc;

        super.onPreExecute();
        textTitle = (TextView) activity.findViewById(R.id.text_title);
        textDesc = (TextView) activity.findViewById(R.id.text_description);
        imageButtonPlay = (ImageButton) activity.findViewById(R.id
                .but_media_play);

        Flags.SONG_TITLE = station.getName();
        Flags.SONG_DESCRIPTION = station.getCtqueryString();

        Flags.SONG_IMAGE_URL = station.getLogo();

        if (textDesc != null && textTitle != null) {
            textDesc.setText(station.getCtqueryString());
            textTitle.setText(station.getName());
        }
    }

    @Override
    protected ArrayList<Uri> doInBackground(Station... params) {
        ArrayList<String> m3u = DownloadContent.lineArray("http://yp.shoutcast" +
                ".com/" + "/sbin/tunein-station.m3u" + "?id=" + params[0].getStationId());
        ArrayList<Uri> uriArrayList = new ArrayList<Uri>();

        for (int i = 0; i < m3u.size(); i++) {
            if (m3u.get(i).startsWith("http")) {
                file = m3u.get(i);
                file = file.replace("http", "icy");
                uriArrayList.add(Uri.parse(file));
                break;
            }
        }

        return uriArrayList;
    }

    @Override
    protected void onPostExecute(final ArrayList<Uri> uriArrayList) {
        super.onPostExecute(uriArrayList);
        if (imageButtonPlay != null) {
            imageButtonPlay.setEnabled(true);
        }
        Intent intent = new Intent(activity, MyService.class);
        activity.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MyService.ServiceBinder servicBinder = (MyService.ServiceBinder) iBinder;
                MyService myServiceEngine = servicBinder.getService();
//  				myServiceEngine.prepare(url,"icy://37.130.230.93:9092");
                try {
                    station.setUriArrayList(uriArrayList); //Must be not null
                    myServiceEngine.prepare(station);
                } catch (Exception ex) {
                    Log.e("Exception service", "while preparing for" + getClass().getName());
                }

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.e("disconnected", "service");
            }
        }, Context.BIND_AUTO_CREATE);

    }
}