package com.zilideus.jukebox.parser;

import com.zilideus.jukebox.model.Station;

import java.util.ArrayList;

/**
 * Created by sandeeprana on 19/10/15.
 */
public class StationList {
   private ArrayList<Station> arrayListStations;
   private TuneIn tuneIn;

   public StationList() {
   }

   public ArrayList<Station> getArrayListStations() {
	  return arrayListStations;
   }

   public void setArrayListStations(ArrayList<Station> arrayListStations) {
	  this.arrayListStations = arrayListStations;
   }

   public TuneIn getTuneIn() {
	  return tuneIn;
   }

   public void setTuneIn(TuneIn tuneIn) {
	  this.tuneIn = tuneIn;
   }
}
