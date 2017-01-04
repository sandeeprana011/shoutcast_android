package com.zilideus.jukebox.fragment;

import android.content.Context;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer.ExoPlayer;
import com.zilideus.jukebox.Exo;
import com.zilideus.jukebox.R;
import com.zilideus.jukebox.VisualizerView;
import com.zilideus.jukebox.model.CurrentStation;
import com.zilideus.jukebox.model.Station;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class Home extends Fragment implements View.OnClickListener {
    public static final String TITLE = "home_fragment";
    private static final String TAG = "HomeFragment";
    Context context;
    private Visualizer visualizer;
    private VisualizerView visualizerView;
    private TextView textDesc, textTitle;
    private ImageView imageLogoBack;
    private TextView textListenersOnline;
    private ImageView imageViewIsFavourite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();

        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OverScrollDecoratorHelper.setUpStaticOverScroll(view, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        visualizerView = (VisualizerView) view.findViewById(R.id.myvisualizerview);
        textTitle = (TextView) view.findViewById(R.id.text_title);
        textDesc = (TextView) view.findViewById(R.id.text_description);
        textListenersOnline = (TextView) view.findViewById(R.id.text_home_listeners_listening);

        imageViewIsFavourite = (ImageView) view.findViewById(R.id.imageview_is_favourite);
        imageViewIsFavourite.setOnClickListener(this);

//        Station.isFavourite()

        imageLogoBack = (ImageView) view.findViewById(R.id.image_logo_back);

        if (visualizerView != null) {
            new Exo();
            if (Exo.getPlayer() != null) {
                initAudio();
            }
        }

//        onUpdateUI();
        this.onViewCreated();

    }


    public void initAudio() {
//	  setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setupVisualizerFxAndUI();
        // Make sure the visualizer is enabled only when you actually want to
        // receive data, and
        // when it makes sense to receive data.
//        if (visualizer != null) {

//        }
        // When the stream ends, we don't need to collect any more data. We
        // don't do this in
        // setupVisualizerFxAndUI because we likely want to have more,
        // non-Visualizer related code
        // in this callback.
//        visualizer.setEnabled(false);

    }

    public void setEnabled(boolean enabled) {
        visualizer.setEnabled(enabled);
    }

    private void setupVisualizerFxAndUI() {

        // Create the Visualizer object and attach it to our media player.

//	  PlayerControl playerControl=new PlayerControl(new Exo().getPlayer());
//	  int sessionid=playerControl.getAudioSessionId();

        try {
            visualizer = new Visualizer(0);
            visualizer.setEnabled(false);
            visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            visualizer.setDataCaptureListener(
                    new Visualizer.OnDataCaptureListener() {
                        public void onWaveFormDataCapture(Visualizer visualizer,
                                                          byte[] bytes, int samplingRate) {
                            visualizerView.updateVisualizer(bytes);
                        }

                        public void onFftDataCapture(Visualizer visualizer,
                                                     byte[] bytes, int samplingRate) {
                        }
                    }, Visualizer.getMaxCaptureRate() / 2, true, false);
            visualizer.setEnabled(true);
        } catch (Exception ex) {
            Log.e("error", "initializing visualizer" + getClass().getName() + " \n Exception : " + ex.getMessage());
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (visualizer != null) {
            visualizer.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        CurrentStation cs = CurrentStation.build();
        if (cs != null) {
            Station station = cs.getStation();
            switch (v.getId()) {
                case R.id.imageview_is_favourite:
                    if (station.isFavourite()) {
                        station.delete();
                        imageViewIsFavourite.setImageResource(R.drawable.favourite_grey);
                    } else {
                        if (station.getStationId() != null && !station.getStationId().equals("")) {
                            station.save();
                        }
                        imageViewIsFavourite.setImageResource(R.drawable.favourite);
                    }
            }
        }

    }

    public void onUpdateUI() {
        CurrentStation currentStation = CurrentStation.build();
        if (currentStation != null) {
            Station cs = currentStation.getStation();
            if (cs.getLogo() != null && imageLogoBack != null) {
                Glide.with(this)
                        .load(cs.getLogo())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.music)
                        .into(imageLogoBack);

            } else {
                Glide.with(this)
                        .load(R.drawable.music)
                        .into(imageLogoBack);
            }
            if (cs.getName() != null) {
                textTitle.setText(cs.getName());
                textDesc.setText(cs.getCtqueryString());
                textListenersOnline.setText(cs.getLc() + " peoples are listening to this channel.");
            }
            if (cs.getStationId() != null) {
                imageViewIsFavourite.setVisibility(View.VISIBLE);
                if (cs.getStationId() != null && !cs.getStationId().equals("")) {
                    if (cs.isFavourite()) {
                        imageViewIsFavourite.setImageResource(R.drawable.favourite);
                    } else {
                        imageViewIsFavourite.setImageResource(R.drawable.favourite_grey);
                    }
                } else {
                    imageViewIsFavourite.setImageResource(R.drawable.favourite_grey);
                }
            }
        }

    }

    public void onViewCreated() {
        Station cs = Station.getStationRandom();
        if (cs != null) {
//            Station cs = currentStation.getStation();
            if (cs.getLogo() != null && imageLogoBack != null) {
                Glide.with(this)
                        .load(cs.getLogo())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.music)
                        .into(imageLogoBack);

            }
            if (cs.getName() != null) {
                textTitle.setText(cs.getName());
                textDesc.setText(cs.getCtqueryString());
                textListenersOnline.setText(cs.getLc() + " peoples are listening to this channel.");
            }
            if (cs.getStationId() != null) {
                imageViewIsFavourite.setVisibility(View.VISIBLE);
                if (cs.getStationId() != null && !cs.getStationId().equals("")) {
                    if (cs.isFavourite()) {
                        imageViewIsFavourite.setImageResource(R.drawable.favourite);
                    } else {
                        imageViewIsFavourite.setImageResource(R.drawable.favourite_grey);
                    }
                } else {
                    imageViewIsFavourite.setImageResource(R.drawable.favourite_grey);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.onUpdateUI();
        this.onStateChanged();
    }


    public void onStateChanged() {

        RotateAnimation rotateAnimation;

        rotateAnimation = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(10000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);


        ImageView imageButtonPlayStop = (ImageView) getActivity().findViewById(R.id.but_media_play);

        if (imageButtonPlayStop == null) return;
        switch (Exo.getPlayer().getPlaybackState()) {
            case ExoPlayer.STATE_READY:
                Log.e(TAG, "State Ready");
                imageButtonPlayStop.startAnimation(rotateAnimation);
                rotateAnimation.cancel();
                rotateAnimation.reset();
                imageButtonPlayStop.setEnabled(true);
                imageButtonPlayStop.setImageResource(R.drawable.ic_stop);
                break;
            case ExoPlayer.STATE_BUFFERING:
                imageButtonPlayStop.setEnabled(false);
                imageButtonPlayStop.setImageResource(R.drawable.ic_buffering);
                imageButtonPlayStop.setAnimation(rotateAnimation);
                Snackbar.make((View) imageButtonPlayStop.getParent(), "Buffering...", Snackbar.LENGTH_SHORT).show();
//                rotateAnimation.start();
                imageButtonPlayStop.startAnimation(rotateAnimation);

                Log.e(TAG, "State Buffering");
                break;
            case ExoPlayer.STATE_ENDED:
                rotateAnimation.cancel();
                imageButtonPlayStop.setEnabled(true);
                imageButtonPlayStop.setImageResource(R.drawable.ic_play);
                Log.e(TAG, "State Ended");
                break;
            case ExoPlayer.STATE_IDLE:
                rotateAnimation.cancel();
                imageButtonPlayStop.setEnabled(true);
                imageButtonPlayStop.setImageResource(R.drawable.ic_play);
                Log.e(TAG, "State Idle");
                break;
            case ExoPlayer.STATE_PREPARING:
                Snackbar.make((View) imageButtonPlayStop.getParent(), "Preparing...", Snackbar.LENGTH_SHORT).show();
                break;
            default:
                Log.e(TAG, "Default Unknown state");
                break;
        }
//        Log.e(TAG, "PLayer state changed");
    }

}