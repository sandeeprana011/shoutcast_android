package com.zilideus.jukebox.parser;

import com.zilideus.jukebox.flags.Url_format;
import com.zilideus.jukebox.network.DownloadContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sandeeprana on 19/10/15.
 */
public class ParserXMLtoJSON {

   public StationList getTopStationswithLIMIT(String data) throws IOException, JSONException {


	  JSONObject objectRoot = XML.toJSONObject(data);
	  JSONObject object = objectRoot.getJSONObject("stationlist");

	  StationList stationList = new StationList();
	  if (object.has("tunein") && !object.isNull("tunein")) {
		 TuneIn tuneIn = new TuneIn();
		 if (object.has("base") && !object.isNull("base")) {
			tuneIn.setBase(object.getString("base"));
		 }
		 if (object.has("base-m3u") && !object.isNull("base-m3u")) {
			tuneIn.setBase(object.getString("base-m3u"));
		 }
		 if (object.has("base-xspf") && !object.isNull("base-xspf")) {
			tuneIn.setBase(object.getString("base-xspf"));
		 }
		 stationList.setTuneIn(tuneIn);
	  }


	  if (object.has("station") && !object.isNull("station")) {
		 Object objectArrayOrObject = object.get("station");
		 if (objectArrayOrObject instanceof JSONObject) {
			Station station = new Station();
			JSONObject o = (JSONObject) objectArrayOrObject;
			if (o.has("name") && !o.isNull("name")) {
			   station.setName(o.getString("name"));
			}
			if (o.has("br") && !o.isNull("br")) {
			   station.setBrbitrate(o.getString("br"));
			}
			if (o.has("ct") && !o.isNull("ct")) {
			   station.setCtqueryString(o.getString("ct"));
			}
			if (o.has("genre") && !o.isNull("genre")) {
			   station.setGenre(o.getString("genre"));
			}
			if (o.has("id") && !o.isNull("id")) {
			   station.setId(o.getString("id"));
			}
			if (o.has("lc") && !o.isNull("lc")) {
			   station.setLc(o.getString("lc"));
			}
			if (o.has("mt") && !o.isNull("mt")) {
			   station.setMt(o.getString("mt"));
			}
			if (o.has("logo") && !o.isNull("logo")) {
			   station.setLogo(o.getString("logo"));
			}
			if (o.has("ml") && !o.isNull("ml")) {
			   station.setMl(o.getString("ml"));
			}
			if (o.has("cst") && !o.isNull("cst")) {
			   station.setCst(o.getString("cst"));
			}
			if (o.has("genre2") && !o.isNull("genre2")) {
			   station.setGenre2(o.getString("genre2"));
			}
			if (o.has("genre3") && !o.isNull("genre3")) {
			   station.setGenre3(o.getString("genre3"));
			}


			ArrayList<Station> arrayList = new ArrayList<>();
			arrayList.add(station);

			stationList.setArrayListStations(arrayList);
		 } else {
			JSONArray arr = (JSONArray) objectArrayOrObject;
			ArrayList<Station> arrayList = new ArrayList<>();
			for (int j = 0; j < arr.length(); j++) {
			   Station station = new Station();
			   JSONObject o = arr.getJSONObject(j);
			   if (o.has("name") && !o.isNull("name")) {
				  station.setName(o.getString("name"));
			   }
			   if (o.has("br") && !o.isNull("br")) {
				  station.setBrbitrate(o.getString("br"));
			   }
			   if (o.has("ct") && !o.isNull("ct")) {
				  station.setCtqueryString(o.getString("ct"));
			   }
			   if (o.has("genre") && !o.isNull("genre")) {
				  station.setGenre(o.getString("genre"));
			   }
			   if (o.has("id") && !o.isNull("id")) {
				  station.setId(o.getString("id"));
			   }
			   if (o.has("lc") && !o.isNull("lc")) {
				  station.setLc(o.getString("lc"));
			   }
			   if (o.has("mt") && !o.isNull("mt")) {
				  station.setMt(o.getString("mt"));
			   }
			   if (o.has("logo") && !o.isNull("logo")) {
				  station.setLogo(o.getString("logo"));
			   }
			   if (o.has("ml") && !o.isNull("ml")) {
				  station.setMl(o.getString("ml"));
			   }
			   if (o.has("cst") && !o.isNull("cst")) {
				  station.setCst(o.getString("cst"));
			   }
			   if (o.has("genre2") && !o.isNull("genre2")) {
				  station.setGenre2(o.getString("genre2"));
			   }
			   if (o.has("genre3") && !o.isNull("genre3")) {
				  station.setGenre3(o.getString("genre3"));
			   }

			   arrayList.add(station);

			}
			stationList.setArrayListStations(arrayList);
		 }

	  }

	  return stationList;
   }


   public StationList getStationByKeySearch(String devID, String search, String limit, String bitrate, String mediaType) throws IOException, JSONException {
	  Url_format uri_format = new Url_format();
	  String data = DownloadContent.downloadContent(uri_format.getStationByKeywords(devID, search, limit,
			  bitrate,
			  mediaType));

	  JSONObject objectRoot = XML.toJSONObject(data);
	  JSONObject object = objectRoot.getJSONObject("stationlist");

	  StationList stationList = new StationList();
	  if (object.has("tunein") && !object.isNull("tunein")) {
		 TuneIn tuneIn = new TuneIn();
		 if (object.has("base") && !object.isNull("base")) {
			tuneIn.setBase(object.getString("base"));
		 }
		 if (object.has("base-m3u") && !object.isNull("base-m3u")) {
			tuneIn.setBase(object.getString("base-m3u"));
		 }
		 if (object.has("base-xspf") && !object.isNull("base-xspf")) {
			tuneIn.setBase(object.getString("base-xspf"));
		 }
		 stationList.setTuneIn(tuneIn);
	  }


	  if (object.has("station") && !object.isNull("station")) {
		 Object objectArrayOrObject = object.get("station");
		 if (objectArrayOrObject instanceof JSONObject) {
			Station station = new Station();
			JSONObject o = (JSONObject) objectArrayOrObject;
			if (o.has("name") && !o.isNull("name")) {
			   station.setName(o.getString("name"));
			}
			if (o.has("br") && !o.isNull("br")) {
			   station.setBrbitrate(o.getString("br"));
			}
			if (o.has("ct") && !o.isNull("ct")) {
			   station.setCtqueryString(o.getString("ct"));
			}
			if (o.has("genre") && !o.isNull("genre")) {
			   station.setGenre(o.getString("genre"));
			}
			if (o.has("id") && !o.isNull("id")) {
			   station.setId(o.getString("id"));
			}
			if (o.has("lc") && !o.isNull("lc")) {
			   station.setLc(o.getString("lc"));
			}
			if (o.has("mt") && !o.isNull("mt")) {
			   station.setMt(o.getString("mt"));
			}
			if (o.has("logo") && !o.isNull("logo")) {
			   station.setLogo(o.getString("logo"));
			}
			if (o.has("ml") && !o.isNull("ml")) {
			   station.setMl(o.getString("ml"));
			}
			if (o.has("cst") && !o.isNull("cst")) {
			   station.setCst(o.getString("cst"));
			}
			if (o.has("genre2") && !o.isNull("genre2")) {
			   station.setGenre2(o.getString("genre2"));
			}
			if (o.has("genre3") && !o.isNull("genre3")) {
			   station.setGenre3(o.getString("genre3"));
			}

			ArrayList<Station> arrayList = new ArrayList<>();
			arrayList.add(station);

			stationList.setArrayListStations(arrayList);
		 } else {
			JSONArray arr = (JSONArray) objectArrayOrObject;
			ArrayList<Station> arrayList = new ArrayList<>();
			for (int j = 0; j < arr.length(); j++) {
			   Station station = new Station();
			   JSONObject o = arr.getJSONObject(j);
			   if (o.has("name") && !o.isNull("name")) {
				  station.setName(o.getString("name"));
			   }
			   if (o.has("br") && !o.isNull("br")) {
				  station.setBrbitrate(o.getString("br"));
			   }
			   if (o.has("ct") && !o.isNull("ct")) {
				  station.setCtqueryString(o.getString("ct"));
			   }
			   if (o.has("genre") && !o.isNull("genre")) {
				  station.setGenre(o.getString("genre"));
			   }
			   if (o.has("id") && !o.isNull("id")) {
				  station.setId(o.getString("id"));
			   }
			   if (o.has("lc") && !o.isNull("lc")) {
				  station.setLc(o.getString("lc"));
			   }
			   if (o.has("mt") && !o.isNull("mt")) {
				  station.setMt(o.getString("mt"));
			   }
			   if (o.has("logo") && !o.isNull("logo")) {
				  station.setLogo(o.getString("logo"));
			   }
			   if (o.has("ml") && !o.isNull("ml")) {
				  station.setMl(o.getString("ml"));
			   }
			   if (o.has("cst") && !o.isNull("cst")) {
				  station.setCst(o.getString("cst"));
			   }
			   if (o.has("genre2") && !o.isNull("genre2")) {
				  station.setGenre2(o.getString("genre2"));
			   }
			   if (o.has("genre3") && !o.isNull("genre3")) {
				  station.setGenre3(o.getString("genre3"));
			   }

			   arrayList.add(station);

			}
			stationList.setArrayListStations(arrayList);
		 }

	  }

	  return stationList;
   }


   public StationList getStationByGenre(String devID, String genre, String limit, String bitrate, String mediaType) throws IOException, JSONException {
	  Url_format uri_format = new Url_format();
	  String data = DownloadContent.downloadContent(uri_format.getStationsByGenre(devID, genre, limit,
			  bitrate,
			  mediaType));

	  JSONObject objectRoot = XML.toJSONObject(data);
	  JSONObject object = objectRoot.getJSONObject("stationlist");

	  StationList stationList = new StationList();
	  if (object.has("tunein") && !object.isNull("tunein")) {
		 TuneIn tuneIn = new TuneIn();
		 if (object.has("base") && !object.isNull("base")) {
			tuneIn.setBase(object.getString("base"));
		 }
		 if (object.has("base-m3u") && !object.isNull("base-m3u")) {
			tuneIn.setBase(object.getString("base-m3u"));
		 }
		 if (object.has("base-xspf") && !object.isNull("base-xspf")) {
			tuneIn.setBase(object.getString("base-xspf"));
		 }
		 stationList.setTuneIn(tuneIn);
	  }


	  if (object.has("station") && !object.isNull("station")) {
		 Object objectArrayOrObject = object.get("station");
		 if (objectArrayOrObject instanceof JSONObject) {
			Station station = new Station();
			JSONObject o = (JSONObject) objectArrayOrObject;
			if (o.has("name") && !o.isNull("name")) {
			   station.setName(o.getString("name"));
			}
			if (o.has("br") && !o.isNull("br")) {
			   station.setBrbitrate(o.getString("br"));
			}
			if (o.has("ct") && !o.isNull("ct")) {
			   station.setCtqueryString(o.getString("ct"));
			}
			if (o.has("genre") && !o.isNull("genre")) {
			   station.setGenre(o.getString("genre"));
			}
			if (o.has("id") && !o.isNull("id")) {
			   station.setId(o.getString("id"));
			}
			if (o.has("lc") && !o.isNull("lc")) {
			   station.setLc(o.getString("lc"));
			}
			if (o.has("mt") && !o.isNull("mt")) {
			   station.setMt(o.getString("mt"));
			}
			if (o.has("logo") && !o.isNull("logo")) {
			   station.setLogo(o.getString("logo"));
			}
			if (o.has("ml") && !o.isNull("ml")) {
			   station.setMl(o.getString("ml"));
			}
			if (o.has("cst") && !o.isNull("cst")) {
			   station.setCst(o.getString("cst"));
			}
			if (o.has("genre2") && !o.isNull("genre2")) {
			   station.setGenre2(o.getString("genre2"));
			}
			if (o.has("genre3") && !o.isNull("genre3")) {
			   station.setGenre3(o.getString("genre3"));
			}

			ArrayList<Station> arrayList = new ArrayList<>();
			arrayList.add(station);

			stationList.setArrayListStations(arrayList);
		 } else {
			JSONArray arr = (JSONArray) objectArrayOrObject;
			ArrayList<Station> arrayList = new ArrayList<>();
			for (int j = 0; j < arr.length(); j++) {
			   Station station = new Station();
			   JSONObject o = arr.getJSONObject(j);
			   if (o.has("name") && !o.isNull("name")) {
				  station.setName(o.getString("name"));
			   }
			   if (o.has("br") && !o.isNull("br")) {
				  station.setBrbitrate(o.getString("br"));
			   }
			   if (o.has("ct") && !o.isNull("ct")) {
				  station.setCtqueryString(o.getString("ct"));
			   }
			   if (o.has("genre") && !o.isNull("genre")) {
				  station.setGenre(o.getString("genre"));
			   }
			   if (o.has("id") && !o.isNull("id")) {
				  station.setId(o.getString("id"));
			   }
			   if (o.has("lc") && !o.isNull("lc")) {
				  station.setLc(o.getString("lc"));
			   }
			   if (o.has("mt") && !o.isNull("mt")) {
				  station.setMt(o.getString("mt"));
			   }
			   if (o.has("logo") && !o.isNull("logo")) {
				  station.setLogo(o.getString("logo"));
			   }
			   if (o.has("ml") && !o.isNull("ml")) {
				  station.setMl(o.getString("ml"));
			   }
			   if (o.has("cst") && !o.isNull("cst")) {
				  station.setCst(o.getString("cst"));
			   }
			   if (o.has("genre2") && !o.isNull("genre2")) {
				  station.setGenre2(o.getString("genre2"));
			   }
			   if (o.has("genre3") && !o.isNull("genre3")) {
				  station.setGenre3(o.getString("genre3"));
			   }

			   arrayList.add(station);

			}
			stationList.setArrayListStations(arrayList);
		 }

	  }

	  return stationList;
   }

   public StationList getStationBasedOnNowPlaying(String devID, String ct_queryString, String limit, String bitrate, String mediaType) throws IOException, JSONException {
	  Url_format uri_format = new Url_format();
	  String data = DownloadContent.downloadContent(uri_format.getStationsJSONBasedOnNowPlaying(devID, ct_queryString, limit,
			  bitrate,
			  mediaType));

	  JSONObject objectRoot = new JSONObject(data).getJSONObject("data");
	  JSONObject object = objectRoot.getJSONObject("stationlist");

	  StationList stationList = new StationList();
	  if (object.has("tunein") && !object.isNull("tunein")) {
		 TuneIn tuneIn = new TuneIn();
		 if (object.has("base") && !object.isNull("base")) {
			tuneIn.setBase(object.getString("base"));
		 }
		 if (object.has("base-m3u") && !object.isNull("base-m3u")) {
			tuneIn.setBase(object.getString("base-m3u"));
		 }
		 if (object.has("base-xspf") && !object.isNull("base-xspf")) {
			tuneIn.setBase(object.getString("base-xspf"));
		 }
		 stationList.setTuneIn(tuneIn);
	  }


	  if (object.has("station") && !object.isNull("station")) {
		 JSONArray arr = object.getJSONArray("station");
		 ArrayList<Station> arrayList = new ArrayList<>();
		 for (int j = 0; j < arr.length(); j++) {
			Station station = new Station();
			JSONObject o = arr.getJSONObject(j);
			if (o.has("name") && !o.isNull("name")) {
			   station.setName(o.getString("name"));
			}
			if (o.has("br") && !o.isNull("br")) {
			   station.setBrbitrate(o.getString("br"));
			}
			if (o.has("ct") && !o.isNull("ct")) {
			   station.setCtqueryString(o.getString("ct"));
			}
			if (o.has("genre") && !o.isNull("genre")) {
			   station.setGenre(o.getString("genre"));
			}
			if (o.has("id") && !o.isNull("id")) {
			   station.setId(o.getString("id"));
			}
			if (o.has("lc") && !o.isNull("lc")) {
			   station.setLc(o.getString("lc"));
			}
			if (o.has("mt") && !o.isNull("mt")) {
			   station.setMt(o.getString("mt"));
			}
			if (o.has("logo") && !o.isNull("logo")) {
			   station.setLogo(o.getString("logo"));
			}
			if (o.has("ml") && !o.isNull("ml")) {
			   station.setMl(o.getString("ml"));
			}
			if (o.has("cst") && !o.isNull("cst")) {
			   station.setCst(o.getString("cst"));
			}
			if (o.has("genre2") && !o.isNull("genre2")) {
			   station.setGenre2(o.getString("genre2"));
			}
			if (o.has("genre3") && !o.isNull("genre3")) {
			   station.setGenre3(o.getString("genre3"));
			}

			arrayList.add(station);

		 }
		 stationList.setArrayListStations(arrayList);
	  }


	  return stationList;
   }

   public StationList getStationRandom(String devID, String genre, String limit, String
		   bitrate, String mediaType) throws IOException, JSONException {
	  Url_format uri_format = new Url_format();
	  String data = DownloadContent.downloadContent(uri_format.getStationsJSONRandom(devID, genre,
			  limit,
			  bitrate,
			  mediaType));

	  JSONObject objectRoot = new JSONObject(data).getJSONObject("data");
	  JSONObject object = objectRoot.getJSONObject("stationlist");

	  StationList stationList = new StationList();
	  if (object.has("tunein") && !object.isNull("tunein")) {
		 TuneIn tuneIn = new TuneIn();
		 if (object.has("base") && !object.isNull("base")) {
			tuneIn.setBase(object.getString("base"));
		 }
		 if (object.has("base-m3u") && !object.isNull("base-m3u")) {
			tuneIn.setBase(object.getString("base-m3u"));
		 }
		 if (object.has("base-xspf") && !object.isNull("base-xspf")) {
			tuneIn.setBase(object.getString("base-xspf"));
		 }
		 stationList.setTuneIn(tuneIn);
	  }


	  if (object.has("station") && !object.isNull("station")) {
		 JSONArray arr = object.getJSONArray("station");
		 ArrayList<Station> arrayList = new ArrayList<>();
		 for (int j = 0; j < arr.length(); j++) {
			Station station = new Station();
			JSONObject o = arr.getJSONObject(j);
			if (o.has("name") && !o.isNull("name")) {
			   station.setName(o.getString("name"));
			}
			if (o.has("br") && !o.isNull("br")) {
			   station.setBrbitrate(o.getString("br"));
			}
			if (o.has("ct") && !o.isNull("ct")) {
			   station.setCtqueryString(o.getString("ct"));
			}
			if (o.has("genre") && !o.isNull("genre")) {
			   station.setGenre(o.getString("genre"));
			}
			if (o.has("id") && !o.isNull("id")) {
			   station.setId(o.getString("id"));
			}
			if (o.has("lc") && !o.isNull("lc")) {
			   station.setLc(o.getString("lc"));
			}
			if (o.has("mt") && !o.isNull("mt")) {
			   station.setMt(o.getString("mt"));
			}
			if (o.has("logo") && !o.isNull("logo")) {
			   station.setLogo(o.getString("logo"));
			}
			if (o.has("ml") && !o.isNull("ml")) {
			   station.setMl(o.getString("ml"));
			}
			if (o.has("cst") && !o.isNull("cst")) {
			   station.setCst(o.getString("cst"));
			}
			if (o.has("genre2") && !o.isNull("genre2")) {
			   station.setGenre2(o.getString("genre2"));
			}
			if (o.has("genre3") && !o.isNull("genre3")) {
			   station.setGenre3(o.getString("genre3"));
			}

			arrayList.add(station);

		 }
		 stationList.setArrayListStations(arrayList);
	  }


	  return stationList;
   }


}