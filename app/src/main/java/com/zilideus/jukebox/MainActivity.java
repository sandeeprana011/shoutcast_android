package com.zilideus.jukebox;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.zilideus.jukebox.flags.Flags;
import com.zilideus.jukebox.flags.Url_format;
import com.zilideus.jukebox.fragment.AboutUs;
import com.zilideus.jukebox.fragment.Favourite;
import com.zilideus.jukebox.fragment.Home;
import com.zilideus.jukebox.fragment.ListFragment;
import com.zilideus.jukebox.fragment.SearchFragment;
import com.zilideus.jukebox.fragment.TopListFragment;
import com.zilideus.jukebox.model.Station;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnChangePlayerState {
    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int BUFFER_SEGMENT_COUNT = 256;
    private static final String TAG = "JUKEBOX";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 14;
    PagerAdapter adapter;
    //   private static ExoPlayer player;
    private Context context;
    private MyService myServiceEngine;
    private Fragment fragment;
    private SharedPreferences prefrences;
    private ServiceConnection connectionService;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        prefrences = getSharedPreferences(Flags.SETTINGS, MODE_PRIVATE);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ScreenSliderPagerFragment(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);


        this.askForRuntimePermissions();
        prepareDatabase();


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

    private void askForRuntimePermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

                Log.e(TAG, "Should show record Audio permission");

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void prepareDatabase() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            /**
             * This block of code is for fixing the SugarORM bug. Related to SDK VERSION
             * tables not found
             */
            long station_id = Station.save(new Station());
            try {
                Station station = Station.findById(Station.class, station_id);
                Station.delete(station);
            } catch (Exception ignored) {
                Log.e("Exception Raisd", "EX : " + ignored.getMessage());
            }
        } else {
            Station.findById(Station.class, (long) 1);
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

        final FragmentManager fragmentManager = getSupportFragmentManager();
        //noinspection SimplifiableIfStatement
        FragmentTransaction trasaction = fragmentManager.beginTransaction();

        if (id == R.id.action_search) {
//            fragment = new SearchFragment();
//            trasaction.addToBackStack(SearchFragment.TITLE);
            viewPager.setCurrentItem(0);
//            trasaction.replace(R.id.container_fragment, fragment).commit();
            return true;
        } else if (id == R.id.action_favourite) {
//            fragment = new Favourite();
//            trasaction.addToBackStack(Favourite.TITLE);
            viewPager.setCurrentItem(2);
//            trasaction.replace(R.id.container_fragment, fragment).commit();
        }

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

//        if (id == R.id.nav_top_music) {
//            // Handle the top music action
//            fragment = new ListFragment();
//
//            prefrences.edit().putString(Flags.JSON_URL, url_format.getTopStationsXML(Flags.DEV_ID,
//                    "20", null, null)).apply();
//
//            fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();
//
//        } else if (id == R.id.nav_search_station) {
//            fragment = new SearchFragment();
//            fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();
//
//
//        } else if (id == R.id.nav_home) {
//
//            fragment = new Home();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.replace(R.id.container_fragment, fragment, Home.TITLE).commit();
//
//
//        } else if (id == R.id.nav_about) {
//            fragment = new AboutUs();
//            fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
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

//        fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit();

    }


    public void searchstation(View view) {
        EditText editText = (EditText) findViewById(R.id.search_text_station);

        //Hiding IME
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


        String urlToSearch = new Url_format().getStationByKeywords(Flags.DEV_ID,
                editText.getText().toString(), "30", null, null);

        viewPager.setCurrentItem(2);


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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        ImageButton imageButtonPlayStop = (ImageButton) findViewById(R.id.but_media_play);
        RotateAnimation rotateAnimation;

        rotateAnimation = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(10000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        if (imageButtonPlayStop == null) return;
        switch (playbackState) {
            case ExoPlayer.STATE_READY:
                Log.e(TAG, "State Ready");
                imageButtonPlayStop.startAnimation(rotateAnimation);
                rotateAnimation.cancel();
                rotateAnimation.reset();
                imageButtonPlayStop.setEnabled(true);
                imageButtonPlayStop.setImageResource(R.drawable.ic_stop);

                fragment = new Home();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.container_fragment, fragment, Home.TITLE).commit();


                Home home = (Home) this.getSupportFragmentManager().findFragmentByTag(Home.TITLE);
                if (home != null) {
                    home.initAudio();
                }


                break;
            case ExoPlayer.STATE_BUFFERING:
                imageButtonPlayStop.setEnabled(false);
                imageButtonPlayStop.setImageResource(R.drawable.ic_buffering);
                imageButtonPlayStop.setAnimation(rotateAnimation);
                Snackbar.make((View) imageButtonPlayStop.getParent(), "Buffering...", Snackbar.LENGTH_SHORT).show();
//                rotateAnimation.start();
                imageButtonPlayStop.startAnimation(rotateAnimation);

                Log.e(TAG, "State Buffering");
                break;
            case ExoPlayer.STATE_ENDED:
                rotateAnimation.cancel();
                imageButtonPlayStop.setEnabled(true);
                imageButtonPlayStop.setImageResource(R.drawable.ic_play);
                Log.e(TAG, "State Ended");
                break;
            case ExoPlayer.STATE_IDLE:
                rotateAnimation.cancel();
                imageButtonPlayStop.setImageResource(R.drawable.ic_idle);
                Log.e(TAG, "State Idle");
                break;
            case ExoPlayer.STATE_PREPARING:
                Snackbar.make((View) imageButtonPlayStop.getParent(), "Preparing...", Snackbar.LENGTH_SHORT).show();
                imageButtonPlayStop.setImageResource(R.drawable.ic_preparing);
                Log.e(TAG, "State Preparing");
                break;
            default:
                Log.e(TAG, "Default Unknown state");
                break;
        }
//        Log.e(TAG, "PLayer state changed");
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.e(TAG, "Player Error : " + error.getMessage());
    }

    public void changeVisualization(View view) {
        ((VisualizerView) view).changeTypeEqualizer();
    }

    private class ScreenSliderPagerFragment extends FragmentStatePagerAdapter {

        private int NUMBER_PAGES = 5;

        public ScreenSliderPagerFragment(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new SearchFragment();
                    break;
                case 1:
                    fragment = new Home();
                    break;
//                case 2:
//                    fragment = new ListFragment();
//                    break;
                case 2:
                    fragment = new Favourite();
                    break;
                case 3:
                    fragment = new TopListFragment();
                    break;
                case 4:
                    fragment = new AboutUs();
                    break;
                default:
                    fragment = new Home();
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "SEARCH";
                case 1:
                    return "HOME";
//                case 2:
//                    return "CURRENT LIST";
                case 2:
                    return "FAVOURITE";
                case 3:
                    return "TOP CHANNELS";
                case 4:
                    return "ABOUT US";
                default:
                    return "HOME";

            }

        }

        @Override
        public int getCount() {
            return NUMBER_PAGES;
        }
    }
}