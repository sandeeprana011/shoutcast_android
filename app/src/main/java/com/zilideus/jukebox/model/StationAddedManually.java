package com.zilideus.jukebox.model;

/**
 * Created by sandeeprana on 08/01/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

public class StationAddedManually extends Station {
    public StationAddedManually() {
    }

    @Override
    public int getType() {
        int i = super.getType();
        return Station.TYPE_MANUALLY_ADDED;
    }


}
