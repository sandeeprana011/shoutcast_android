package com.zilideus.jukebox;

import com.google.android.exoplayer.ExoPlaybackException;

/**
 * Created by sandeeprana on 14/12/16.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */
interface OnChangePlayerState {
    void onStateChanged(boolean playWhenReady, int playbackState);
    void onPlayerError(ExoPlaybackException error);
}
