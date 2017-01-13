package com.zilideus.jukebox;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.Util;
import com.zilideus.jukebox.flags.Flags;
import com.zilideus.jukebox.model.Station;
import com.zilideus.jukebox.model.StationAddedManually;

public class MyService extends Service {

    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int BUFFER_SEGMENT_COUNT = 256;
    private static final int BROADCAST_INT = 589;
    private static final String TAG = "MyService";
    private static int NOTIFICATION_ID = 48;
    private String stationTitle, urlStation, urlStationImage, bitrate, genre;
    private IBinder binder = new ServiceBinder();
    private NotificationManager mNotificationManager;
    private BroadcastReceiver broadcastReceiver;
    private boolean isPlaying;
    private OnChangePlayerState onChangePlayerStateListener;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();


        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notification("Jukebox", "Shoutcast");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Exo.getPlayer().stop();
                Exo.getPlayer().release();
                mNotificationManager.cancelAll();
                System.exit(0);
            }
        };
//
        this.registerReceiver(broadcastReceiver, new IntentFilter(Flags.CLOSE_PLAYER));


        Exo.getPlayer().addListener(new ExoPlayer.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {


                if (onChangePlayerStateListener != null) {
                    onChangePlayerStateListener.onStateChanged(playWhenReady, playbackState);
                }
                Flags.STATE = playbackState;
                if (playbackState == ExoPlayer.STATE_READY) {
                    setIsPlaying(true);
                } else {
                    setIsPlaying(false);
                }

//			notification();'
            }

            @Override
            public void onPlayWhenReadyCommitted() {
                Log.e(TAG, "ONPlay Ready Commited");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                onChangePlayerStateListener.onPlayerError(error);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setOnChangePlayerStateListener(OnChangePlayerState onChangePlayerStateListener) {
        this.onChangePlayerStateListener = onChangePlayerStateListener;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//	  prepare("icy://37.130.230.93:9092");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    public void prepare(Station station) {

        if (station != null && (station.hasValidUri() || ((StationAddedManually) station).isValidStation())) {


            Uri uri = null;
            switch (station.getType()) {
                case Station.TYPE_MANUALLY_ADDED:
                    uri = Uri.parse(((StationAddedManually) station).getUrlstation());
                    break;
                default:
                    uri = station.getUriArrayList().get(0);
                    break;


            }

            Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
            DataSource dataSource = new DefaultUriDataSource(this, null, Util.getUserAgent(this,
                    "Jukebox"), true);

            ExtractorSampleSource sampleSource = new ExtractorSampleSource(uri, dataSource,
                    allocator, BUFFER_SEGMENT_COUNT * BUFFER_SEGMENT_SIZE);

            MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource, MediaCodecSelector.DEFAULT);
            Exo.getPlayer().stop();
            Exo.getPlayer().prepare(audioRenderer);
            Exo.getPlayer().setPlayWhenReady(true);

            notification(station.getName(), station.getCtqueryString());
        } else {
            Toast.makeText(this, "Invalid Station", Toast.LENGTH_LONG).show();
        }
    }


    public void notification(String title, String contenttext) {

        CharSequence text = getText(R.string.app_name);
        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        PendingIntent pi = PendingIntent.getBroadcast(this,
                NOTIFICATION_ID,
                new Intent(Flags.CLOSE_PLAYER),
                PendingIntent.FLAG_UPDATE_CURRENT);


//        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);


        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_launcher)
                // Add media control buttons that invoke intents in your media service
//                .addAction(R.drawable.ic_prev, "Previous", prevPendingIntent) // #0
                .addAction(R.drawable.ic_exit, "Exit", pi)  // #1
                .addAction(R.drawable.ic_play_dark, "Play", contentIntent)     // #2
                // Apply the media style template
//                .setStyle(new NotificationCompat.MediaStyle()
//                        .setShowActionsInCompactView(1 /* #1: pause button */)
//                        .setMediaSession(mMediaSession.getSessionToken()))
                .setContentTitle(title)
                .setContentText(contenttext)
                .setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap())
                .build();


        notification.flags |= Notification.FLAG_NO_CLEAR;


        // Set the info for the views that show in the notification panel.
//        Notification notification = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)  // the status icon
//                .setTicker(contenttext)  // the status text
//                .setOngoing(true)
//                .setContent(remoteViews)
//                .setWhen(System.currentTimeMillis())  // the time stamp
//                .setContentTitle(title)  // the label of the entry
//                .setContentText(contenttext)  // the contents of the entry
//                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
//                .build();

//        remoteViews.setTextViewText(R.id.station_title, title);
//        remoteViews.setTextViewText(R.id.station_description, contenttext);
//        remoteViews.setImageViewResource(R.id.station_icon, R.mipmap.ic_launcher);


//        remoteViews.setOnClickPendingIntent(R.id.station_close, pi);


// mId allows you to update the notification later on.
//        mNotificationManager.notify(NOTIFICATION_ID, notification);
        startForeground(NOTIFICATION_ID, notification);


    }


    public void stop() {
        Exo.getPlayer().stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception ex) {
            Log.e("Exception", " broadcast receiver " + ex.getMessage());
        }
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }
//	  mNotificationManager.cancelAll();
    }

    public void exit() {
        this.stopSelf();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public class ServiceBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

}
