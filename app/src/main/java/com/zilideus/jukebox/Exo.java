package com.zilideus.jukebox;

import com.google.android.exoplayer.ExoPlayer;

/**
 * Created by sandeeprana on 31/10/2015 AD.
 */
public class Exo {
    private static Exo ourInstance = new Exo();
    private static ExoPlayer exoPlayer = ExoPlayer.Factory.newInstance(1);


    public static Exo getInstance() {
        return ourInstance;
    }

    public static ExoPlayer getPlayer() {
//	  exoPlayer.
//	  Flags.AUDIO_SESSION_ID=new PlayerControl(exoPlayer).getAudioSessionId();
        return exoPlayer;
    }

}
