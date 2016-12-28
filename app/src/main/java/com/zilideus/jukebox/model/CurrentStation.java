package com.zilideus.jukebox.model;

import android.net.Uri;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeeprana on 27/12/16.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

public class CurrentStation extends SugarRecord {

    @Unique
    private String key;

    private String name;
    private String brbitrate;
    private String ctquerystring;
    private String genre;
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


    public CurrentStation() {
        this.key = "121";
    }

    public CurrentStation(Station station) {
//        this.setId(121L);
        this.key = "121";
        this.name = station.getName();
        this.brbitrate = station.getBrbitrate();
        this.ctquerystring = station.getCtqueryString();
        this.genre = station.getGenre();
        this.stationid = station.getStationId();
        this.lc = station.getLc();
        this.mt = station.getMt();
        this.logo = station.getLogo();
        this.ml = station.getMl();
        this.genre2 = station.getGenre2();
        this.genre3 = station.getGenre3();
        this.cst = station.getCst();
    }

    public static boolean isFavourite(String stationid) {
        if (stationid != null && !stationid.equals("")) {
            List<Station> stationList = Station.find(Station.class, " stationid=?", stationid);
            if (stationList != null && stationList.size() > 0) {
                return true;
            } else {
                return false;
            }
        } else
            return false;
    }

    public static CurrentStation getCurrentStation() {
//        List<Station> listStations = Station.listAll(Station.class);
        List<CurrentStation> list = CurrentStation.listAll(CurrentStation.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else return null;
    }

    public static CurrentStation build() {
        List<CurrentStation> listStations = CurrentStation.find(CurrentStation.class, " KEY=?", "121");
        if (listStations != null && listStations.size() > 0) {
            return listStations.get(0);
        } else {
            return null;
        }
    }

//    @Override
//    public static long save(CurrentStation station) {
//        station.setId(121L);
//        return save(station);
//    }

    public Station getStation() {
        Station station = new Station();
        station.setName(this.name);
        station.setBrbitrate(this.brbitrate);
        station.setCtqueryString(this.ctquerystring);
        station.setGenre(this.genre);
        station.setStationId(this.stationid);
        station.setLc(this.lc);
        station.setMt(this.mt);
        station.setLogo(this.logo);
        station.setMl(this.ml);
        station.setGenre2(this.genre2);
        station.setGenre3(this.genre3);
        station.setCst(this.cst);
        return station;
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
            List<Station> stationList = Station.find(Station.class, " stationid=?", this.getStationId());
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
        this.setId(121L);
        this.key = "121";
        return super.save();
    }
}
