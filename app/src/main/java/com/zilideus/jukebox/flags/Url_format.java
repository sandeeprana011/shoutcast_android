package com.zilideus.jukebox.flags;

/**
 * Created by sandeeprana on 19/10/15.
 */
public class Url_format {
    /**
     * Returns string url which will return a xml formatted string
     *
     * @param devID     developer id
     * @param limit
     * @param bitrate
     * @param mediaType
     * @param offset
     * @return
     */
    public String getTopStationsXML(String devID, String offset, String limit, String bitrate, String mediaType) {

        String url = "http://api.shoutcast.com/legacy/Top500?k=" + devID;
        if (limit != null)
            url = url + "&limit=" + offset + "," + limit;
        if (bitrate != null)
            url = url + "&br=" + bitrate;
        if (mediaType != null)
            url = url + "&mt=" + mediaType;
        return url;
    }

    /**
     * search - Specify the query to search.
     * k - API Dev ID.
     * limit - Limits the no of results to be returned.
     * Ex: http://api.shoutcast.com/legacy/stationsearch?k=[Your Dev ID]&search=ambient+beats&limit=10
     * limit with pagination - Limits the no of results with pagination included.
     * Ex: http://api.shoutcast.com/legacy/stationsearch?k=[Your Dev ID]&search=ambient+beats&limit=X,Y
     * Y is the number of results to return and X is the offset.
     * br - Filter the stations based on bitrate specified.
     * Ex: http://api.shoutcast.com/legacy/stationsearch?k=[Your Dev ID]&search=ambient+beats&br=64
     * mt - Filter the stations based on media type specified.
     * Ex: http://api.shoutcast.com/legacy/stationsearch?k=[Your Dev ID]&search=ambient+beats&mt=audio/mpeg
     * MP3 = audio/mpeg and AAC+ = audio/aacp
     *
     * @param devID
     * @param bitrate
     * @param mediaType
     * @param search
     * @param limit
     * @return
     */

    public String getStationByKeywords(String devID, String search, String offset, String limit, String bitrate, String
            mediaType) {
        String url = "http://api.shoutcast.com/legacy/stationsearch?k=" + devID + "&search=" + search;
        if (limit != null)
            url = url + "&limit=" + offset + "," + limit;
        if (bitrate != null)
            url = url + "&br=" + bitrate;
        if (mediaType != null)
            url = url + "&mt=" + mediaType;
        return url;

    }

    /**
     * Get Stations by Genre
     * <p>
     * Description: Get stations which match the genre specified as query.
     * <p>
     * URL: http://api.shoutcast.com/legacy/genresearch?k=[Your Dev ID]&genre=classic
     * <p>
     * Required Parameters:
     * <p>
     * k - API Dev ID.
     * <p>
     * Optional Parameters:
     * <p>
     * limit - Limits the no of results to be returned.
     * <p>
     * limit with pagination - Limits the no of results with pagination included.
     * <p>
     * Ex: http://api.shoutcast.com/legacy/genresearch?k=[Your Dev ID]&genre=classic&limit=X,Y
     * <p>
     * Y is the number of results to return and X is the offset.
     * <p>
     * br - Filter the stations based on bitrate specified.
     * <p>
     * Ex: http://api.shoutcast.com/legacy/genresearch?k=[Your Dev ID]&genre=classic&br=64
     * <p>
     * mt - Filter the stations based on media type specified.
     * <p>
     * Ex: http://api.shoutcast.com/legacy/genresearch?k=[Your Dev ID]&genre=classic&mt=audio/mpeg
     * MP3 = audio/mpeg and AAC+ = audio/aacp
     *
     * @param devId
     * @param genre
     * @param limit
     * @param bitrate
     * @param mediaType
     * @return
     */
    public String getStationsByGenre(String devId, String genre, String limit, String bitrate, String
            mediaType) {

        String url = "http://api.shoutcast.com/legacy/genresearch?k=" + devId + "&genre=" + genre;
        if (limit != null)
            url = url + "&limit=" + limit;
        if (bitrate != null)
            url = url + "&br=" + bitrate;
        if (mediaType != null)
            url = url + "&mt=" + mediaType;
        return url;


    }

    /**
     * Get Stations Based on Now Playing Info
     * <p>
     * Description: Return stations which match a specified query in the now playing node.
     * <p>
     * URL: http://api.shoutcast.com/station/nowplaying?k=[Your Dev ID]&ct=rihanna&f=xml
     * <p>
     * Required Parameters:
     * <p>
     * ct - Query to search in Now Playing node. This parameter also supports querying multiple artists in the same query by using "||". ex: ct=madonna||u2||beyonce up to 10 artists
     * f - the response format (xml, json, rss). You can choose xml,json or rss based results.
     * k - API Dev ID.
     * <p>
     * Optional Parameters:
     * <p>
     * c - The callback function to invoke in the response (appropriate for JSON responses only).
     * limit - Limits the no of results to be returned in output.
     * br - Filter the stations based on bitrate specified.
     * mt - Filter the stations based on media type specified.
     * genre - Filter stations that match the genre passed.
     *
     * @param devID
     * @param queryString
     * @param limit
     * @param bitrate
     * @param mediaType
     * @return
     */
    public String getStationsJSONBasedOnNowPlaying(String devID, String queryString, String limit,
                                                   String bitrate,
                                                   String
                                                           mediaType) {

        String url = "http://api.shoutcast.com/station/nowplaying?k=" + devID + "&ct=" + queryString + "&f" +
                "=json";
        if (limit != null)
            url = url + "&limit=" + limit;
        if (bitrate != null)
            url = url + "&br=" + bitrate;
        if (mediaType != null)
            url = url + "&mt=" + mediaType;
        return url;

    }


    /**
     * Get Random Stations
     * <p>
     * Description: Get random stations on SHOUTcast Radio Directory. Random stations can be restricted to the Bitrate/Genre/Media type specified.
     * <p>
     * URL:
     * <p>
     * http://api.shoutcast.com/station/randomstations?k=[Your Dev ID]&f=xml
     * <p>
     * Returns a random station. This API by default returns one random station.
     * To get more random stations, set the number of stations to return by passing the limit parameter.
     * <p>
     * http://api.shoutcast.com/station/randomstations?k=[Your Dev ID]&f=xml&mt=audio/mpeg&br=128&genre=Fresh
     * <p>
     * Returns a random station. This API by default returns one random station.
     * To get more random stations, set the number of stations to return by passing the limit parameter.
     * <p>
     * Required Parameters:
     * <p>
     * f - the response format (xml, json, rss). You can choose xml,json or rss based results.
     * k - API Dev ID.
     * <p>
     * Optional Parameters:
     * <p>
     * c - The callback function to invoke in the response (appropriate for JSON responses only).
     * br - Bitrate to filter the station result.
     * mt - Media type to filter the station result.
     * genre - Genre to filter the station result.
     * limit - This API by default returns one station. To get more random stations, set the number of stations to return by passing the limit parameter.
     *
     * @param devID
     * @param genre
     * @param limit
     * @param bitrate
     * @param mediaType
     * @return
     */
    public String getStationsJSONRandom(String devID, String genre, String limit,
                                        String bitrate,
                                        String
                                                mediaType) {

        String url = "http://api.shoutcast.com/station/randomstations?k=" + devID + "&f=json";

        if (limit != null)
            url = url + "&limit=" + limit;
        if (bitrate != null)
            url = url + "&br=" + bitrate;
        if (mediaType != null)
            url = url + "&mt=" + mediaType;
        if (genre != null)
            url = url + "&genre=" + genre;
        return url;

    }

    /**
     * Get All Genres
     * <p>
     * Description: Get all the genres on SHOUTcast Radio Directory
     * <p>
     * URL: http://api.shoutcast.com/legacy/genrelist?k=[Your Dev ID]
     * <p>
     * Required Parameters:
     *
     * @param devID API Dev ID.
     * @return
     */
    public String getAllGenres(String devID) {

        return "http://api.shoutcast.com/legacy/genrelist?k=" + devID;
    }

    public String getPrimaryGenres(String devID, String jsonORxml) {
        return "http://api.shoutcast.com/genre/primary?k=[Your Dev ID]&f=" + jsonORxml;
    }

    /**
     * How To Tune Into A Station
     * <p>
     * To tune into a station, find the "id" of the station from the API results & make a call to http://yp.shoutcast.com<base>?id=[Station_id] by appending the station id.
     * <p>
     * The <base> value is taken from the tunein node and based on the playlist format required (as PLS, M3U and XSPF formats are supported) you then need to choose the appropriate attribute to get the complete playlist url to use.
     * <p>
     * Ex: If the station id is 1025, Call => http://yp.shoutcast.com/<base>?id=1025
     *
     * @param base
     * @param stationID
     * @return
     */
    public String getTuneInUrlStation(String base, String stationID) {
        return "http://yp.shoutcast.com/" + base + "?id=" + stationID;
    }


}
