package com.zilideus.jukebox.fragment;

import com.zilideus.jukebox.model.Station;
import com.zilideus.jukebox.model.StationAddedManually;

/**
 * Created by sandeeprana on 24/12/16.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */
public interface FavouriteClickCallbacks {
    public void favouriteAdded(Station station, int position);

    public void favouriteRemoved(Station station, int position);

    public void favrouriteDeleted(StationAddedManually manually, int adapterPosition);
}
