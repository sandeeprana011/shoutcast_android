package com.zilideus.jukebox.fragment;

import android.content.Context;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zilideus.jukebox.Exo;
import com.zilideus.jukebox.R;
import com.zilideus.jukebox.VisualizerView;
import com.zilideus.jukebox.flags.Flags;

public class Home extends Fragment {
    Context context;
    private Visualizer visualizer;
    private VisualizerView visualizerView;
    private TextView textDesc, textTitle;
    private ImageView imageLogoBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();

        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        visualizerView = (VisualizerView) view.findViewById(R.id.myvisualizerview);
        textTitle = (TextView) getActivity().findViewById(R.id.text_title);
        textDesc = (TextView) getActivity().findViewById(R.id.text_description);
        imageLogoBack = (ImageView) getActivity().findViewById(R.id.imageLogoBack);
        if (visualizerView != null) {
            new Exo();
            if (Exo.getPlayer() != null) {
                initAudio();
            }
        }

        if (Flags.SONG_IMAGE_URL != null && imageLogoBack != null) {
            Glide.with(this)
                    .load(Flags.SONG_IMAGE_URL)
                    .placeholder(R.drawable.music)
                    .into(imageLogoBack);

        }
        textDesc.setText(Flags.SONG_DESCRIPTION);
        textTitle.setText(Flags.SONG_TITLE);

    }


    private void initAudio() {
//	  setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setupVisualizerFxAndUI();
        // Make sure the visualizer is enabled only when you actually want to
        // receive data, and
        // when it makes sense to receive data.
//        if (visualizer != null) {
        try {
            visualizer.setEnabled(true);
        } catch (Exception ex) {
            Log.e("except", " f" + ex.getMessage());
        }

//        }
        // When the stream ends, we don't need to collect any more data. We
        // don't do this in
        // setupVisualizerFxAndUI because we likely want to have more,
        // non-Visualizer related code
        // in this callback.
//	  visualizer.setEnabled(false);

    }

    private void setupVisualizerFxAndUI() {

        // Create the Visualizer object and attach it to our media player.

//	  PlayerControl playerControl=new PlayerControl(new Exo().getPlayer());
//	  int sessionid=playerControl.getAudioSessionId();

        try {
            visualizer = new Visualizer(0);
//	 int a=12;

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
        } catch (Exception ex) {
            Log.e("error", "initializing visualizer" + getClass().getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (visualizer != null) {
            visualizer.setEnabled(false);
        }
    }
}