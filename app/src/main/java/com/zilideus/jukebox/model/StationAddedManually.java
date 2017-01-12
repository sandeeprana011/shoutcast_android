package com.zilideus.jukebox.model;

import android.util.Log;

import java.util.UUID;

/**
 * Created by sandeeprana on 08/01/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

public class StationAddedManually extends Station {
    private static final String TAG = "StatoiinAddedManually";
    private String urlstation;

    public StationAddedManually() {
        super.setType(Station.TYPE_MANUALLY_ADDED);
    }

    @Override
    public int getType() {
        return Station.TYPE_MANUALLY_ADDED;
    }

    public String getUrlstation() {
        return urlstation;
    }

    public void setUrlstation(String urlstation) {
        this.urlstation = urlstation;
    }

    public long save(String name) {
        Log.d(TAG, name + " station saved.");
        if (getStationId() == null || getStationId().isEmpty()) {
            String id = UUID.randomUUID().toString();
            setStationId(id);
            super.setStationId(id);
            return super.save();
        } else {
            return super.save();
        }
    }

    public boolean isValidStation() {

        return this.getUrlstation() != null && !this.getUrlstation().isEmpty();
    }
}
