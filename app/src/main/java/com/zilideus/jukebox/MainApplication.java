package com.zilideus.jukebox;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.orm.SugarContext;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by sandeeprana on 14/12/16.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

public class MainApplication extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
            super.attachBaseContext(base);

        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/futura_regular.ttf");
//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/futura_thin.ttf");
//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/futura_bold.ttf");
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Quicksand-Bold.ttf");
//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/FuturaMedium.ttf");

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
