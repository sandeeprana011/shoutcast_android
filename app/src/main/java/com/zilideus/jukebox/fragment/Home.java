package com.zilideus.jukebox.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.audiofx.Visualizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zilideus.jukebox.Exo;
import com.zilideus.jukebox.R;
import com.zilideus.jukebox.VisualizerView;
import com.zilideus.jukebox.flags.Flags;
import com.zilideus.jukebox.network.DownloadContent;

import java.io.IOException;
import java.net.URL;

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
//	  context=getActivity();
        // Inflate the layout for this fragment
//new AsyncTask<Void, Void, StationList>(){
//   public StationList stationList;
//
//   @Override
//   protected StationList doInBackground(Void... params) {
//	  try {
//		 String string= DownloadContent.downloadContent(new Url_format().getTopStationsXML(Flags
//				 .DEV_ID,"20",null,null));
//		 ParserXMLtoJSON parserXMLtoJSON=new ParserXMLtoJSON();
//		 stationList=parserXMLtoJSON.getTopStationswithLIMIT(string);
//
//
//	  } catch (IOException e) {
//		 e.printStackTrace();
//	  } catch (JSONException e) {
//		 e.printStackTrace();
//	  }
//	  return stationList;
//   }
//
//   @Override
//   protected void onPostExecute(StationList stationList) {
//	  super.onPostExecute(stationList);
//	  ListAdapterStations adapterStations=new ListAdapterStations(context,R.id.list_stations,
//			  stationList.getArrayListStations());
//   }
//}.execute();
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
            if (DownloadContent.isAvailable(context)) {
                new AsyncTask<Void, Void, Bitmap>() {
                    public Bitmap bitmap;

                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        try {
                            URL url = new URL(Flags.SONG_IMAGE_URL);
                            bitmap = BitmapFactory.decodeStream(url.openStream());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return bitmap;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute(bitmap);
                        imageLogoBack.setImageBitmap(bitmap);
                    }
                }.execute();
            } else {
                Toast.makeText(context, "Network not available", Toast.LENGTH_LONG).show();
            }
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
        try
        {
            visualizer.setEnabled(true);
        }catch (Exception ex){
            Log.e("except"," f"+ex.getMessage());
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