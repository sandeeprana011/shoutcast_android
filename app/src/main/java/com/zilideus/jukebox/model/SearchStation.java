package com.zilideus.jukebox.model;

import android.net.Uri;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeeprana on 06/01/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

public class SearchStation extends SugarRecord {
    private String name;
    private String brbitrate;
    private String ctquerystring;
    private String genre;

    @Unique
    private String stationid;
    private String lc;
    private String mt;
    private String logo;
    private String ml;
    private String genre2;
    private String genre3;
    private String cst;

    @Ignore
    private ArrayList<Uri> uriArrayList;


    public SearchStation() {

    }

    public static boolean isFavourite(String stationid) {
        if (stationid != null && !stationid.equals("")) {
            List<SearchStation> stationList = SearchStation.find(SearchStation.class, " stationid=?", stationid);
            return stationList != null && stationList.size() > 0;
        } else
            return false;
    }

    public static Station getStation(SearchStation cs) {
        Station station = new Station();
        station.setName(cs.getName());
        station.setStationId(cs.getStationId());
        station.setCst(cs.getCst());
        station.setMt(cs.getMt());
        station.setGenre3(cs.getGenre3());
        station.setGenre(cs.getGenre());
        station.setUriArrayList(cs.getUriArrayList());
        station.setBrbitrate(cs.getBrbitrate());
        station.setCtqueryString(cs.getCtqueryString());
        station.setLc(cs.getLc());
        station.setLogo(cs.getLogo());
        station.setMl(cs.getMl());
        return station;
    }


    public static SearchStation getStation(Station cs) {
        SearchStation station = new SearchStation();
        station.setName(cs.getName());
        station.setStationId(cs.getStationId());
        station.setCst(cs.getCst());
        station.setMt(cs.getMt());
        station.setGenre3(cs.getGenre3());
        station.setGenre(cs.getGenre());
        station.setUriArrayList(cs.getUriArrayList());
        station.setBrbitrate(cs.getBrbitrate());
        station.setCtqueryString(cs.getCtqueryString());
        station.setLc(cs.getLc());
        station.setLogo(cs.getLogo());
        station.setMl(cs.getMl());
        return station;
    }


    public static SearchStation getStationRandom() {
        List<SearchStation> list = SearchStation.listAll(SearchStation.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else
            return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrbitrate() {
        return brbitrate;
    }

    public void setBrbitrate(String brbitrate) {
        this.brbitrate = brbitrate;
    }

    public String getCtqueryString() {
        return ctquerystring;
    }

    public void setCtqueryString(String ctqueryString) {
        this.ctquerystring = ctqueryString;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStationId() {
        return stationid;
    }

    public void setStationId(String id) {
        this.stationid = id;
    }

    public String getLc() {
        return lc;
    }

    public void setLc(String lc) {
        this.lc = lc;
    }

    public String getMt() {
        return mt;
    }

    public void setMt(String mt) {
        this.mt = mt;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMl() {
        return ml;
    }

    public void setMl(String ml) {
        this.ml = ml;
    }

    public String getGenre2() {
        return genre2;
    }

    public void setGenre2(String genre2) {
        this.genre2 = genre2;
    }

    public String getGenre3() {
        return genre3;
    }

    public void setGenre3(String genre3) {
        this.genre3 = genre3;
    }

    public String getCst() {
        return cst;
    }

    public void setCst(String cst) {
        this.cst = cst;
    }

    public ArrayList<Uri> getUriArrayList() {
        return uriArrayList;
    }

    public void setUriArrayList(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    public boolean isFavourite() {
        if (this.getStationId() != null && !this.getStationId().equals("")) {
            List<Station> stationList = Station.find(Station.class, " STATIONID=?", this.getStationId());
            if (stationList != null && stationList.size() > 0) {
                this.setId(stationList.get(0).getId());
                return true;
            } else {
                return false;
            }
        } else
            return false;
    }

    @Override
    public long save() {
        if (this.getStationId() != null && !this.getStationId().equals("")) {
            return super.save();
        } else return -1;

    }
}
