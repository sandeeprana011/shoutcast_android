package com.zilideus.jukebox;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.exoplayer.ExoPlaybackException;
import com.zilideus.jukebox.flags.Flags;
import com.zilideus.jukebox.flags.Url_format;
import com.zilideus.jukebox.fragment.AboutUs;
import com.zilideus.jukebox.fragment.Home;
import com.zilideus.jukebox.fragment.ListFragment;
import com.zilideus.jukebox.fragment.SearchFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener, OnChangePlayerState {
    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int BUFFER_SEGMENT_COUNT = 256;
    private static final String TAG = "JUKEBOX";
    //   private static ExoPlayer player;
    private Context context;
    private MyService myServiceEngine;
    private Fragment fragment;
    private SharedPreferences prefrences;
    private ServiceConnection connectionService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        prefrences = getSharedPreferences(Flags.SETTINGS, MODE_PRIVATE);
        setSupportActionBar(toolbar);
        connectionService = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MyService.ServiceBinder servicBinder = (MyService.ServiceBinder) iBinder;
                myServiceEngine = servicBinder.getService();
                myServiceEngine.setOnChangePlayerStateListener(MainActivity.this);
                Log.e(TAG, "SERVICE CONNECTED");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.e(TAG, "SERVICE DISCONNECTED");
            }
        };

        context = this;
        try {
            java.net.URL.setURLStreamHandlerFactory(new java.net.URLStreamHandlerFactory() {
                public java.net.URLStreamHandler createURLStreamHandler(String protocol) {
                    Log.d("LOG", "Asking for stream handler for protocol: '" + protocol + "'");
                    if ("icy".equals(protocol)) return new IcyURLStreamHandler();
                    return null;
                }
            });
        } catch (Throwable t) {
            Log.w("LOG", "Cannot set the ICY URLStreamHandler - maybe already set ? - " + t);
        }
//	  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//	  fab.setOnClickListener(new View.OnClickListener() {
//		 @Override
//		 public void onClick(View view) {
////			prepare();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        Intent intent = new Intent(context, MyService.class);
        bindService(intent, connectionService, BIND_AUTO_CREATE);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//	  if (id == R.id.action_settings) {
//		 return true;
//	  }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//	  final ParserXMLtoJSON parser = new ParserXMLtoJSON();

        final FragmentManager fragmentManager = getSupportFragmentManager();
//	  ArrayList<Station> stations = null;
//	  final StationList allStationsListWithTuneIn = null;
        Url_format url_format = new Url_format();

        if (id == R.id.nav_top_music) {
            // Handle the top music action
            fragment = new ListFragment();

            prefrences.edit().putString(Flags.JSON_URL, url_format.getTopStationsXML(Flags.DEV_ID,
                    "20", null, null)).apply();

            fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();

        } else if (id == R.id.nav_search_station) {
            fragment = new SearchFragment();
            fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();


        } else if (id == R.id.nav_home) {
            prefrences = getSharedPreferences(Flags.SETTINGS, MODE_PRIVATE);
            prefrences.registerOnSharedPreferenceChangeListener(this);
            if (prefrences.getBoolean("first", true)) {
                prefrences.edit().putBoolean("first", true).apply();
            }

            fragment = new Home();
            fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();


//	  } else if (id == R.id.nav_share) {
//	  } else if (id == R.id.nav_send) {
        } else if (id == R.id.nav_about) {
            fragment = new AboutUs();
            fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void timer(View view) {

    }

    public void playpause(View view) {
        ImageButton imageView = (ImageButton) view;

        if (myServiceEngine != null) {
            if (myServiceEngine.isPlaying()) {
                myServiceEngine.stop();
                imageView.setEnabled(false);
                imageView.setImageResource(android.R.drawable.ic_media_play);
            }
        }
    }

    public void listshow(View view) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new ListFragment();

        fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("state")) {
            if (sharedPreferences.getString(key, "value").equals("play")) {
                ImageButton button = (ImageButton) findViewById(R.id.but_media_play);
                button.setImageResource(android.R.drawable.ic_media_pause);
            } else {
                ImageButton button = (ImageButton) findViewById(R.id.but_media_play);
                button.setImageResource(android.R.drawable.ic_media_play);
            }
        }
    }

    public void searchstation(View view) {
        EditText editText = (EditText) findViewById(R.id.search_text_station);

        fragment = new ListFragment();
        prefrences.edit().putString(Flags.JSON_URL, new Url_format().getStationByKeywords(Flags.DEV_ID,
                editText.getText().toString(), "30", null, null)).apply();

        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, fragment).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unbindService(connectionService);
        } catch (Exception ex) {
            Log.e("error", "connection error");
        }
    }

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        Log.e(TAG, "PLayer state changed");
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.e(TAG, "Player Error : " + error.getMessage());
    }
}