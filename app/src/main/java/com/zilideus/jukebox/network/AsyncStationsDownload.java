package com.zilideus.jukebox.network;

import android.os.AsyncTask;

import com.zilideus.jukebox.flags.Flags;
import com.zilideus.jukebox.flags.Url_format;

import java.io.IOException;

/**
 * limit
 * bitrate
 * mediatype
 * Created by sandeeprana on 04/11/2015 AD.
 */
public class AsyncStationsDownload extends AsyncTask<String, Integer, String> {


   private String data;

   @Override
   protected String doInBackground(String... params) {
	  Url_format uri_format = new Url_format();
	  try {
		 data = DownloadContent.downloadContent(uri_format.getTopStationsXML(Flags.DEV_ID,
				 params[0],
				 params[1],
				 params[2]));

	  } catch (IOException e) {
		 e.printStackTrace();
	  }

	  return data;
   }

}
