package com.zilideus.jukebox.parser;

import com.zilideus.jukebox.flags.JKeys;
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
        JSONObject object = objectRoot.getJSONObject(JKeys.STATION_LIST);

        StationList stationList = new StationList();
        if (object.has(JKeys.TUNE_IN) && !object.isNull(JKeys.TUNE_IN)) {
            TuneIn tuneIn = new TuneIn();
            if (object.has(JKeys.BASE) && !object.isNull(JKeys.BASE)) {
                tuneIn.setBase(object.getString(JKeys.BASE));
            }
            if (object.has(JKeys.BASE_M3U) && !object.isNull(JKeys.BASE_M3U)) {
                tuneIn.setBase(object.getString(JKeys.BASE_M3U));
            }
            if (object.has(JKeys.BASE_XSPF) && !object.isNull(JKeys.BASE_XSPF)) {
                tuneIn.setBase(object.getString(JKeys.BASE_XSPF));
            }
            stationList.setTuneIn(tuneIn);
        }


        if (object.has(JKeys.STATION) && !object.isNull(JKeys.STATION)) {
            Object objectArrayOrObject = object.get(JKeys.STATION);
            if (objectArrayOrObject instanceof JSONObject) {
                Station station = new Station();
                JSONObject o = (JSONObject) objectArrayOrObject;
                if (o.has(JKeys.NAME) && !o.isNull(JKeys.NAME)) {
                    station.setName(o.getString(JKeys.NAME));
                }
                if (o.has(JKeys.BR) && !o.isNull(JKeys.BR)) {
                    station.setBrbitrate(o.getString(JKeys.BR));
                }
                if (o.has(JKeys.CT) && !o.isNull(JKeys.CT)) {
                    station.setCtqueryString(o.getString(JKeys.CT));
                }
                if (o.has(JKeys.GENRE) && !o.isNull(JKeys.GENRE)) {
                    station.setGenre(o.getString(JKeys.GENRE));
                }
                if (o.has(JKeys.ID) && !o.isNull(JKeys.ID)) {
                    station.setStationId(o.getString(JKeys.ID));
                }
                if (o.has(JKeys.LC) && !o.isNull(JKeys.LC)) {
                    station.setLc(o.getString(JKeys.LC));
                }
                if (o.has(JKeys.MT) && !o.isNull(JKeys.MT)) {
                    station.setMt(o.getString(JKeys.MT));
                }
                if (o.has(JKeys.LOGO) && !o.isNull(JKeys.LOGO)) {
                    station.setLogo(o.getString(JKeys.LOGO));
                }
                if (o.has(JKeys.ML) && !o.isNull(JKeys.ML)) {
                    station.setMl(o.getString(JKeys.ML));
                }
                if (o.has(JKeys.CST) && !o.isNull(JKeys.CST)) {
                    station.setCst(o.getString(JKeys.CST));
                }
                if (o.has(JKeys.GENRE2) && !o.isNull(JKeys.GENRE2)) {
                    station.setGenre2(o.getString(JKeys.GENRE2));
                }
                if (o.has(JKeys.GENRE3) && !o.isNull(JKeys.GENRE3)) {
                    station.setGenre3(o.getString(JKeys.GENRE3));
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
                    if (o.has(JKeys.NAME) && !o.isNull(JKeys.NAME)) {
                        station.setName(o.getString(JKeys.NAME));
                    }
                    if (o.has(JKeys.BR) && !o.isNull(JKeys.BR)) {
                        station.setBrbitrate(o.getString(JKeys.BR));
                    }
                    if (o.has(JKeys.CT) && !o.isNull(JKeys.CT)) {
                        station.setCtqueryString(o.getString(JKeys.CT));
                    }
                    if (o.has(JKeys.GENRE) && !o.isNull(JKeys.GENRE)) {
                        station.setGenre(o.getString(JKeys.GENRE));
                    }
                    if (o.has(JKeys.ID) && !o.isNull(JKeys.ID)) {
                        station.setStationId(o.getString(JKeys.ID));
                    }
                    if (o.has(JKeys.LC) && !o.isNull(JKeys.LC)) {
                        station.setLc(o.getString(JKeys.LC));
                    }
                    if (o.has(JKeys.MT) && !o.isNull(JKeys.MT)) {
                        station.setMt(o.getString(JKeys.MT));
                    }
                    if (o.has(JKeys.LOGO) && !o.isNull(JKeys.LOGO)) {
                        station.setLogo(o.getString(JKeys.LOGO));
                    }
                    if (o.has(JKeys.ML) && !o.isNull(JKeys.ML)) {
                        station.setMl(o.getString(JKeys.ML));
                    }
                    if (o.has(JKeys.CST) && !o.isNull(JKeys.CST)) {
                        station.setCst(o.getString(JKeys.CST));
                    }
                    if (o.has(JKeys.GENRE2) && !o.isNull(JKeys.GENRE2)) {
                        station.setGenre2(o.getString(JKeys.GENRE2));
                    }
                    if (o.has(JKeys.GENRE3) && !o.isNull(JKeys.GENRE3)) {
                        station.setGenre3(o.getString(JKeys.GENRE3));
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
        JSONObject object = objectRoot.getJSONObject(JKeys.STATION_LIST);

        StationList stationList = new StationList();
        if (object.has(JKeys.TUNE_IN) && !object.isNull(JKeys.TUNE_IN)) {
            TuneIn tuneIn = new TuneIn();
            if (object.has(JKeys.BASE) && !object.isNull(JKeys.BASE)) {
                tuneIn.setBase(object.getString(JKeys.BASE));
            }
            if (object.has(JKeys.BASE_M3U) && !object.isNull(JKeys.BASE_M3U)) {
                tuneIn.setBase(object.getString(JKeys.BASE_M3U));
            }
            if (object.has(JKeys.BASE_XSPF) && !object.isNull(JKeys.BASE_XSPF)) {
                tuneIn.setBase(object.getString(JKeys.BASE_XSPF));
            }
            stationList.setTuneIn(tuneIn);
        }


        if (object.has(JKeys.STATION) && !object.isNull(JKeys.STATION)) {
            Object objectArrayOrObject = object.get(JKeys.STATION);
            if (objectArrayOrObject instanceof JSONObject) {
                Station station = new Station();
                JSONObject o = (JSONObject) objectArrayOrObject;
                if (o.has(JKeys.NAME) && !o.isNull(JKeys.NAME)) {
                    station.setName(o.getString(JKeys.NAME));
                }
                if (o.has(JKeys.BR) && !o.isNull(JKeys.BR)) {
                    station.setBrbitrate(o.getString(JKeys.BR));
                }
                if (o.has(JKeys.CT) && !o.isNull(JKeys.CT)) {
                    station.setCtqueryString(o.getString(JKeys.CT));
                }
                if (o.has(JKeys.GENRE) && !o.isNull(JKeys.GENRE)) {
                    station.setGenre(o.getString(JKeys.GENRE));
                }
                if (o.has(JKeys.ID) && !o.isNull(JKeys.ID)) {
                    station.setStationId(o.getString(JKeys.ID));
                }
                if (o.has(JKeys.LC) && !o.isNull(JKeys.LC)) {
                    station.setLc(o.getString(JKeys.LC));
                }
                if (o.has(JKeys.MT) && !o.isNull(JKeys.MT)) {
                    station.setMt(o.getString(JKeys.MT));
                }
                if (o.has(JKeys.LOGO) && !o.isNull(JKeys.LOGO)) {
                    station.setLogo(o.getString(JKeys.LOGO));
                }
                if (o.has(JKeys.ML) && !o.isNull(JKeys.ML)) {
                    station.setMl(o.getString(JKeys.ML));
                }
                if (o.has(JKeys.CST) && !o.isNull(JKeys.CST)) {
                    station.setCst(o.getString(JKeys.CST));
                }
                if (o.has(JKeys.GENRE2) && !o.isNull(JKeys.GENRE2)) {
                    station.setGenre2(o.getString(JKeys.GENRE2));
                }
                if (o.has(JKeys.GENRE3) && !o.isNull(JKeys.GENRE3)) {
                    station.setGenre3(o.getString(JKeys.GENRE3));
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
                    if (o.has(JKeys.NAME) && !o.isNull(JKeys.NAME)) {
                        station.setName(o.getString(JKeys.NAME));
                    }
                    if (o.has(JKeys.BR) && !o.isNull(JKeys.BR)) {
                        station.setBrbitrate(o.getString(JKeys.BR));
                    }
                    if (o.has(JKeys.CT) && !o.isNull(JKeys.CT)) {
                        station.setCtqueryString(o.getString(JKeys.CT));
                    }
                    if (o.has(JKeys.GENRE) && !o.isNull(JKeys.GENRE)) {
                        station.setGenre(o.getString(JKeys.GENRE));
                    }
                    if (o.has(JKeys.ID) && !o.isNull(JKeys.ID)) {
                        station.setStationId(o.getString(JKeys.ID));
                    }
                    if (o.has(JKeys.LC) && !o.isNull(JKeys.LC)) {
                        station.setLc(o.getString(JKeys.LC));
                    }
                    if (o.has(JKeys.MT) && !o.isNull(JKeys.MT)) {
                        station.setMt(o.getString(JKeys.MT));
                    }
                    if (o.has(JKeys.LOGO) && !o.isNull(JKeys.LOGO)) {
                        station.setLogo(o.getString(JKeys.LOGO));
                    }
                    if (o.has(JKeys.ML) && !o.isNull(JKeys.ML)) {
                        station.setMl(o.getString(JKeys.ML));
                    }
                    if (o.has(JKeys.CST) && !o.isNull(JKeys.CST)) {
                        station.setCst(o.getString(JKeys.CST));
                    }
                    if (o.has(JKeys.GENRE2) && !o.isNull(JKeys.GENRE2)) {
                        station.setGenre2(o.getString(JKeys.GENRE2));
                    }
                    if (o.has(JKeys.GENRE3) && !o.isNull(JKeys.GENRE3)) {
                        station.setGenre3(o.getString(JKeys.GENRE3));
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
        if (object.has(JKeys.TUNE_IN) && !object.isNull(JKeys.TUNE_IN)) {
            TuneIn tuneIn = new TuneIn();
            if (object.has(JKeys.BASE) && !object.isNull(JKeys.BASE)) {
                tuneIn.setBase(object.getString(JKeys.BASE));
            }
            if (object.has(JKeys.BASE_M3U) && !object.isNull(JKeys.BASE_M3U)) {
                tuneIn.setBase(object.getString(JKeys.BASE_M3U));
            }
            if (object.has(JKeys.BASE_XSPF) && !object.isNull(JKeys.BASE_XSPF)) {
                tuneIn.setBase(object.getString(JKeys.BASE_XSPF));
            }
            stationList.setTuneIn(tuneIn);
        }


        if (object.has(JKeys.STATION) && !object.isNull(JKeys.STATION)) {
            Object objectArrayOrObject = object.get(JKeys.STATION);
            if (objectArrayOrObject instanceof JSONObject) {
                Station station = new Station();
                JSONObject o = (JSONObject) objectArrayOrObject;
                if (o.has(JKeys.NAME) && !o.isNull(JKeys.NAME)) {
                    station.setName(o.getString(JKeys.NAME));
                }
                if (o.has(JKeys.BR) && !o.isNull(JKeys.BR)) {
                    station.setBrbitrate(o.getString(JKeys.BR));
                }
                if (o.has(JKeys.CT) && !o.isNull(JKeys.CT)) {
                    station.setCtqueryString(o.getString(JKeys.CT));
                }
                if (o.has(JKeys.GENRE) && !o.isNull(JKeys.GENRE)) {
                    station.setGenre(o.getString(JKeys.GENRE));
                }
                if (o.has(JKeys.ID) && !o.isNull(JKeys.ID)) {
                    station.setStationId(o.getString(JKeys.ID));
                }
                if (o.has(JKeys.LC) && !o.isNull(JKeys.LC)) {
                    station.setLc(o.getString(JKeys.LC));
                }
                if (o.has(JKeys.MT) && !o.isNull(JKeys.MT)) {
                    station.setMt(o.getString(JKeys.MT));
                }
                if (o.has(JKeys.LOGO) && !o.isNull(JKeys.LOGO)) {
                    station.setLogo(o.getString(JKeys.LOGO));
                }
                if (o.has(JKeys.ML) && !o.isNull(JKeys.ML)) {
                    station.setMl(o.getString(JKeys.ML));
                }
                if (o.has(JKeys.CST) && !o.isNull(JKeys.CST)) {
                    station.setCst(o.getString(JKeys.CST));
                }
                if (o.has(JKeys.GENRE2) && !o.isNull(JKeys.GENRE2)) {
                    station.setGenre2(o.getString(JKeys.GENRE2));
                }
                if (o.has(JKeys.GENRE3) && !o.isNull(JKeys.GENRE3)) {
                    station.setGenre3(o.getString(JKeys.GENRE3));
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
                    if (o.has(JKeys.NAME) && !o.isNull(JKeys.NAME)) {
                        station.setName(o.getString(JKeys.NAME));
                    }
                    if (o.has(JKeys.BR) && !o.isNull(JKeys.BR)) {
                        station.setBrbitrate(o.getString(JKeys.BR));
                    }
                    if (o.has(JKeys.CT) && !o.isNull(JKeys.CT)) {
                        station.setCtqueryString(o.getString(JKeys.CT));
                    }
                    if (o.has(JKeys.GENRE) && !o.isNull(JKeys.GENRE)) {
                        station.setGenre(o.getString(JKeys.GENRE));
                    }
                    if (o.has(JKeys.ID) && !o.isNull(JKeys.ID)) {
                        station.setStationId(o.getString(JKeys.ID));
                    }
                    if (o.has(JKeys.LC) && !o.isNull(JKeys.LC)) {
                        station.setLc(o.getString(JKeys.LC));
                    }
                    if (o.has(JKeys.MT) && !o.isNull(JKeys.MT)) {
                        station.setMt(o.getString(JKeys.MT));
                    }
                    if (o.has(JKeys.LOGO) && !o.isNull(JKeys.LOGO)) {
                        station.setLogo(o.getString(JKeys.LOGO));
                    }
                    if (o.has(JKeys.ML) && !o.isNull(JKeys.ML)) {
                        station.setMl(o.getString(JKeys.ML));
                    }
                    if (o.has(JKeys.CST) && !o.isNull(JKeys.CST)) {
                        station.setCst(o.getString(JKeys.CST));
                    }
                    if (o.has(JKeys.GENRE2) && !o.isNull(JKeys.GENRE2)) {
                        station.setGenre2(o.getString(JKeys.GENRE2));
                    }
                    if (o.has(JKeys.GENRE3) && !o.isNull(JKeys.GENRE3)) {
                        station.setGenre3(o.getString(JKeys.GENRE3));
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
        if (object.has(JKeys.TUNE_IN) && !object.isNull(JKeys.TUNE_IN)) {
            TuneIn tuneIn = new TuneIn();
            if (object.has(JKeys.BASE) && !object.isNull(JKeys.BASE)) {
                tuneIn.setBase(object.getString(JKeys.BASE));
            }
            if (object.has(JKeys.BASE_M3U) && !object.isNull(JKeys.BASE_M3U)) {
                tuneIn.setBase(object.getString(JKeys.BASE_M3U));
            }
            if (object.has(JKeys.BASE_XSPF) && !object.isNull(JKeys.BASE_XSPF)) {
                tuneIn.setBase(object.getString(JKeys.BASE_XSPF));
            }
            stationList.setTuneIn(tuneIn);
        }


        if (object.has(JKeys.STATION) && !object.isNull(JKeys.STATION)) {
            JSONArray arr = object.getJSONArray(JKeys.STATION);
            ArrayList<Station> arrayList = new ArrayList<>();
            for (int j = 0; j < arr.length(); j++) {
                Station station = new Station();
                JSONObject o = arr.getJSONObject(j);
                if (o.has(JKeys.NAME) && !o.isNull(JKeys.NAME)) {
                    station.setName(o.getString(JKeys.NAME));
                }
                if (o.has(JKeys.BR) && !o.isNull(JKeys.BR)) {
                    station.setBrbitrate(o.getString(JKeys.BR));
                }
                if (o.has(JKeys.CT) && !o.isNull(JKeys.CT)) {
                    station.setCtqueryString(o.getString(JKeys.CT));
                }
                if (o.has(JKeys.GENRE) && !o.isNull(JKeys.GENRE)) {
                    station.setGenre(o.getString(JKeys.GENRE));
                }
                if (o.has(JKeys.ID) && !o.isNull(JKeys.ID)) {
                    station.setStationId(o.getString(JKeys.ID));
                }
                if (o.has(JKeys.LC) && !o.isNull(JKeys.LC)) {
                    station.setLc(o.getString(JKeys.LC));
                }
                if (o.has(JKeys.MT) && !o.isNull(JKeys.MT)) {
                    station.setMt(o.getString(JKeys.MT));
                }
                if (o.has(JKeys.LOGO) && !o.isNull(JKeys.LOGO)) {
                    station.setLogo(o.getString(JKeys.LOGO));
                }
                if (o.has(JKeys.ML) && !o.isNull(JKeys.ML)) {
                    station.setMl(o.getString(JKeys.ML));
                }
                if (o.has(JKeys.CST) && !o.isNull(JKeys.CST)) {
                    station.setCst(o.getString(JKeys.CST));
                }
                if (o.has(JKeys.GENRE2) && !o.isNull(JKeys.GENRE2)) {
                    station.setGenre2(o.getString(JKeys.GENRE2));
                }
                if (o.has(JKeys.GENRE3) && !o.isNull(JKeys.GENRE3)) {
                    station.setGenre3(o.getString(JKeys.GENRE3));
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
        if (object.has(JKeys.TUNE_IN) && !object.isNull(JKeys.TUNE_IN)) {
            TuneIn tuneIn = new TuneIn();
            if (object.has(JKeys.BASE) && !object.isNull(JKeys.BASE)) {
                tuneIn.setBase(object.getString(JKeys.BASE));
            }
            if (object.has(JKeys.BASE_M3U) && !object.isNull(JKeys.BASE_M3U)) {
                tuneIn.setBase(object.getString(JKeys.BASE_M3U));
            }
            if (object.has(JKeys.BASE_XSPF) && !object.isNull(JKeys.BASE_XSPF)) {
                tuneIn.setBase(object.getString(JKeys.BASE_XSPF));
            }
            stationList.setTuneIn(tuneIn);
        }


        if (object.has(JKeys.STATION) && !object.isNull(JKeys.STATION)) {
            JSONArray arr = object.getJSONArray(JKeys.STATION);
            ArrayList<Station> arrayList = new ArrayList<>();
            for (int j = 0; j < arr.length(); j++) {
                Station station = new Station();
                JSONObject o = arr.getJSONObject(j);
                if (o.has(JKeys.NAME) && !o.isNull(JKeys.NAME)) {
                    station.setName(o.getString(JKeys.NAME));
                }
                if (o.has(JKeys.BR) && !o.isNull(JKeys.BR)) {
                    station.setBrbitrate(o.getString(JKeys.BR));
                }
                if (o.has(JKeys.CT) && !o.isNull(JKeys.CT)) {
                    station.setCtqueryString(o.getString(JKeys.CT));
                }
                if (o.has(JKeys.GENRE) && !o.isNull(JKeys.GENRE)) {
                    station.setGenre(o.getString(JKeys.GENRE));
                }
                if (o.has(JKeys.ID) && !o.isNull(JKeys.ID)) {
                    station.setStationId(o.getString(JKeys.ID));
                }
                if (o.has(JKeys.LC) && !o.isNull(JKeys.LC)) {
                    station.setLc(o.getString(JKeys.LC));
                }
                if (o.has(JKeys.MT) && !o.isNull(JKeys.MT)) {
                    station.setMt(o.getString(JKeys.MT));
                }
                if (o.has(JKeys.LOGO) && !o.isNull(JKeys.LOGO)) {
                    station.setLogo(o.getString(JKeys.LOGO));
                }
                if (o.has(JKeys.ML) && !o.isNull(JKeys.ML)) {
                    station.setMl(o.getString(JKeys.ML));
                }
                if (o.has(JKeys.CST) && !o.isNull(JKeys.CST)) {
                    station.setCst(o.getString(JKeys.CST));
                }
                if (o.has(JKeys.GENRE2) && !o.isNull(JKeys.GENRE2)) {
                    station.setGenre2(o.getString(JKeys.GENRE2));
                }
                if (o.has(JKeys.GENRE3) && !o.isNull(JKeys.GENRE3)) {
                    station.setGenre3(o.getString(JKeys.GENRE3));
                }

                arrayList.add(station);

            }
            stationList.setArrayListStations(arrayList);
        }


        return stationList;
    }


}