package com.syncbridge.bestconnections;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.sql.SQLException;

import Classes.CommonFunctions;
import Classes.LoadData;
import Classes.MusicManager;
import DBHelper.SectionDataSource;


public class MainActivity extends AppCompatActivity {

    //public static ActionBar mActionBar;

    private int currentTrackId = 0;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    public static boolean isRunning = false;

    //1-cb, 2-cg, 3-cs
    public static int activeActivity = 0;
    public static int currentActivity = 0;
    public static boolean continueMusic = true;
    public static String SECTION_ID = "";
    public static String TAG = "BestConnections";

    Typeface typeFace;
    ImageView imageViewSpeaker;

    SectionDataSource sectionDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //protected void onCreateDrawer(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewSpeaker = (ImageView) findViewById(R.id.imageViewSpeaker);
        imageViewSpeaker.setTag("unmute");

        set(MainActivity.this);

        final VideoView videoView = (VideoView)findViewById(R.id.videoViewC4L);
        final ImageView imageViewVideo = (ImageView) findViewById(R.id.imageViewVideo);
        videoView.setVisibility(View.INVISIBLE);


        imageViewVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {

                    playVideo();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                return true;
            }
        });

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (videoView.isPlaying()) {
                    // to stop it
                    videoView.stopPlayback();
                    videoView.setVisibility(View.INVISIBLE);
                    imageViewVideo.setVisibility(View.VISIBLE);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
                return true;
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.setVisibility(View.INVISIBLE);
                //imageViewVideo.setVisibility(View.VISIBLE);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        //region on home menu items

        findViewById(R.id.textViewBestConnectionMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(MainActivity.this, CoreBelifeIntroActivity.class);
//                startActivity(i);
//                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                SECTION_ID = "CB";
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("CB").getLast_quote();
                } catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                if (activeActivity != 1) {
                    if (activeActivity != 0) {
                        activeActivity = 1;
                        CommonFunctions.closePreviousActivities(activeActivity);
                    }
                    activeActivity = 1;
                    Intent i = new Intent(MainActivity.this, CoreBelifeIntroActivity.class);
                    i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            }
        });

        findViewById(R.id.textViewLifeFoundationsMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SECTION_ID = "LFBO";
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("LFBO").getLast_quote();
                }catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                if (activeActivity != 4) {
                    if (activeActivity != 0) {
                        activeActivity = 4;
                        CommonFunctions.closePreviousActivities(activeActivity);
                    }
                    activeActivity = 4;
                    if(lastQuoteId.isEmpty()) {
                        Intent i = new Intent(MainActivity.this, LifeFoundationActivity.class);
                        i.putExtra("SECTION", "LFBO");
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    } else {
                        Intent i = new Intent(MainActivity.this, LifeFoundationActivity.class);
                        i.putExtra("SECTION", "LFBO");
                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                }
            }
        });

        findViewById(R.id.textViewExploringOptionsMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SECTION_ID = "UC";
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("UC").getLast_quote();
                }catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                if (activeActivity != 7) {
                    if (activeActivity != 0) {
                        activeActivity = 7;
                        CommonFunctions.closePreviousActivities(activeActivity);
                    }
                    activeActivity = 7;
                    if(lastQuoteId.isEmpty()) {
                        Intent i = new Intent(MainActivity.this, UnconnectedActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    } else {
                        Intent i = new Intent(MainActivity.this, UnconnectedActivity.class);
                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                }
            }
        });

        findViewById(R.id.textViewJournalMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SECTION_ID = "JN";
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("JN").getLast_quote();
                }catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                if (activeActivity != 9) {
                    if (activeActivity != 0) {
                        activeActivity = 9;
                        CommonFunctions.closePreviousActivities(activeActivity);
                    }
                    activeActivity = 9;
                    if(lastQuoteId.isEmpty()) {
                        Intent i = new Intent(MainActivity.this, JournalIntroActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    } else {
                        Intent i = new Intent(MainActivity.this, JournalIntroActivity.class);
                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                }
            }
        });

        findViewById(R.id.textViewAboutC4LMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activeActivity != 0) {
                    activeActivity = 11;
                    CommonFunctions.closePreviousActivities(activeActivity);
                }
                activeActivity = 11;
                Intent i = new Intent(MainActivity.this, AboutIntroActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        findViewById(R.id.textViewResources).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SECTION_ID = "RS";
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("RS").getLast_quote();
                } catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                if (activeActivity != 10) {
                    if (activeActivity != 0) {
                        activeActivity = 10;
                        CommonFunctions.closePreviousActivities(activeActivity);
                    }
                    activeActivity = 10;
                    if (lastQuoteId.isEmpty()) {
                        Intent i = new Intent(MainActivity.this, ResourcesIntroActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    } else {
                        Intent i = new Intent(MainActivity.this, ResourcesIntroActivity.class);
                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                }
            }
        });

        //endregion

    }

    private void playVideo() {
        final VideoView videoView = (VideoView)findViewById(R.id.videoViewC4L);
        final ImageView imageViewVideo = (ImageView) findViewById(R.id.imageViewVideo);
        //Toast.makeText(getApplicationContext(),""+videoView.isPlaying()+" - "+View.INVISIBLE ,Toast.LENGTH_LONG).show();
        if (videoView.getVisibility() == View.INVISIBLE) {

            imageViewVideo.setVisibility(View.INVISIBLE);
            videoView.setVisibility(View.VISIBLE);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

            if (!videoView.isPlaying()) {
                VideoView vv = (VideoView)this.findViewById(R.id.videoViewC4L);

                //Set video name (no extension)
                String myVideoName = "video_c4l";
                //Set app package
                String myAppPack = "com.syncbridge.bestconnections";
                //Get video URI from raw directory
                Uri myVideoUri = Uri.parse("android.resource://"+myAppPack+"/raw/"+myVideoName);
                //Set the video URI
                vv.setVideoURI(myVideoUri);

                //Play the video
                vv.start();
            }
        }
    }

    public void set(final Context context) {

        sectionDataSource = new SectionDataSource(context);

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (LinearLayout) findViewById(R.id.left_drawer);

        typeFace = CommonFunctions.getTypeFace(context, 1);

        ((TextView) findViewById(R.id.textViewBestConnection)).setTypeface(typeFace);
        ((TextView) findViewById(R.id.textViewCB)).setTypeface(typeFace);
        ((TextView) findViewById(R.id.textViewCG)).setTypeface(typeFace);
        ((TextView) findViewById(R.id.textViewCS)).setTypeface(typeFace);

        ((TextView) findViewById(R.id.textViewLifeFoundations)).setTypeface(typeFace);
        ((TextView) findViewById(R.id.textViewBody)).setTypeface(typeFace);
        ((TextView) findViewById(R.id.textViewSoul)).setTypeface(typeFace);
        ((TextView) findViewById(R.id.textViewSpirit)).setTypeface(typeFace);

        ((TextView) findViewById(R.id.textViewExploringOptions)).setTypeface(typeFace);
        ((TextView) findViewById(R.id.textViewUC)).setTypeface(typeFace);
        ((TextView) findViewById(R.id.textViewDC)).setTypeface(typeFace);

        ((TextView) findViewById(R.id.textViewMyJournal)).setTypeface(typeFace);
        ((TextView) findViewById(R.id.textViewResourcesMenu)).setTypeface(typeFace);
        ((TextView) findViewById(R.id.textViewAbout)).setTypeface(typeFace);

        // enabling action bar app icon and behaving it as toggle button

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeButtonEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                mDrawerLayout,
                R.mipmap.ic_drawer_custom, // nav menu toggle icon
                R.string.app_name, // nav drawer open - description for
                R.string.app_name // nav drawer close - description for
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                supportInvalidateOptionsMenu();
            }
        };
        //mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawerLayout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //region onclicks
        mDrawerList.findViewById(R.id.cb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*mDrawerLayout.closeDrawers();*/
                SECTION_ID = "CB";
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("CB").getLast_quote();
                } catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                if (activeActivity != 1) {
                    if (activeActivity != 0) {
                        activeActivity = 1;
                        CommonFunctions.closePreviousActivities(activeActivity);
                    }
                    activeActivity = 1;

                    Intent i = null;
                    Class<?> activity = CommonFunctions.getActivityClassByLeftView(context, SECTION_ID);
                    if(activity != null) {
                        i = new Intent(MainActivity.this, activity);
                    } else {
                        i = new Intent(MainActivity.this, CoreBelifeIntroActivity.class);
                    }

                    i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            }
        });

        mDrawerList.findViewById(R.id.cg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SECTION_ID = "CG";
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("CG").getLast_quote();
                } catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                if (activeActivity != 2) {
                    if (activeActivity != 0) {
                        activeActivity = 2;
                        CommonFunctions.closePreviousActivities(activeActivity);
                    }
                    activeActivity = 2;
                    Intent i = null;
                    Class<?> activity = CommonFunctions.getActivityClassByLeftView(context, SECTION_ID);
                    if (activity != null) {
                        i = new Intent(MainActivity.this, activity);
                    } else {
                        i = new Intent(MainActivity.this, CoreGoalsIntroActivity.class);
                    }

                    i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            }
        });

        mDrawerList.findViewById(R.id.cs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SECTION_ID = "CS";
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("CS").getLast_quote();
                }catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                if (activeActivity != 3) {
                    if (activeActivity != 0) {
                        activeActivity = 3;
                        CommonFunctions.closePreviousActivities(activeActivity);
                    }
                    activeActivity = 3;
                    if(lastQuoteId.isEmpty()) {
                        Intent i = new Intent(MainActivity.this, CoreStatusIntroActivity.class);
                        i.putExtra("SECTION", "INTRO");
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    } else {
                        Intent i = new Intent(MainActivity.this, CoreStatusIntroActivity.class);
                        i.putExtra("SECTION", "INTRO");
                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                }
            }
        });

        mDrawerList.findViewById(R.id.body).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SECTION_ID = "LFBO";
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("LFBO").getLast_quote();
                }catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                if (activeActivity != 4) {
                    if (activeActivity != 0) {
                        activeActivity = 4;
                        CommonFunctions.closePreviousActivities(activeActivity);
                    }
                    activeActivity = 4;
                    if(lastQuoteId.isEmpty()) {
                        Intent i = new Intent(MainActivity.this, LifeFoundationActivity.class);
                        i.putExtra("SECTION", "LFBO");
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    } else {
                        Intent i = new Intent(MainActivity.this, LifeFoundationActivity.class);
                        i.putExtra("SECTION", "LFBO");
                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                }
            }
        });

        mDrawerList.findViewById(R.id.soul).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SECTION_ID = "LFSO";
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("LFSO").getLast_quote();
                }catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                if (activeActivity != 5) {
                    if (activeActivity != 0) {
                        activeActivity = 5;
                        CommonFunctions.closePreviousActivities(activeActivity);
                    }
                    activeActivity = 5;
                    if(lastQuoteId.isEmpty()) {
                        Intent i = new Intent(MainActivity.this, LifeFoundationActivity.class);
                        i.putExtra("SECTION", "LFSO");
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    } else {
                        Intent i = new Intent(MainActivity.this, LifeFoundationActivity.class);
                        i.putExtra("SECTION", "LFSO");
                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                }
            }
        });
        mDrawerList.findViewById(R.id.spirit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SECTION_ID = "LFSP";
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("LFSP").getLast_quote();
                }catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                if (activeActivity != 6) {
                    if (activeActivity != 0) {
                        activeActivity = 6;
                        CommonFunctions.closePreviousActivities(activeActivity);
                    }
                    activeActivity = 6;
                    if(lastQuoteId.isEmpty()) {
                        Intent i = new Intent(MainActivity.this, LifeFoundationActivity.class);
                        i.putExtra("SECTION", "LFSP");
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    } else {
                        Intent i = new Intent(MainActivity.this, LifeFoundationActivity.class);
                        i.putExtra("SECTION", "LFSP");
                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                }
            }
        });

        mDrawerList.findViewById(R.id.uc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonFunctions.isSectionFinished(context, "LFSP")) {
                    SECTION_ID = "UC";
                    String lastQuoteId = "";
                    try {
                        sectionDataSource.open();
                        lastQuoteId = sectionDataSource.getEntry("UC").getLast_quote();
                    } catch (SQLException ex) {
                        Log.e(TAG, ex.getMessage(), ex);
                    }
                    if (activeActivity != 7) {
                        if (activeActivity != 0) {
                            activeActivity = 7;
                            CommonFunctions.closePreviousActivities(activeActivity);
                        }
                        activeActivity = 7;

                        Intent i = null;
                        Class<?> activity = CommonFunctions.getActivityClassByLeftView(context, SECTION_ID);
                        if (activity != null) {
                            i = new Intent(MainActivity.this, activity);
                        } else {
                            i = new Intent(MainActivity.this, UnconnectedActivity.class);
                        }

                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                } else {
                    Toast.makeText(context, "Life foundation - sprit section still not complete.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDrawerList.findViewById(R.id.dc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
                if(CommonFunctions.isSectionFinished(context, "LFSP")) {
                    SECTION_ID = "DC";
                    String lastQuoteId = "";
                    try {
                        sectionDataSource.open();
                        lastQuoteId = sectionDataSource.getEntry("DC").getLast_quote();
                    } catch (SQLException ex) {
                        Log.e(TAG, ex.getMessage(), ex);
                    }
                    if (activeActivity != 8) {
                        if (activeActivity != 0) {
                            activeActivity = 8;
                            CommonFunctions.closePreviousActivities(activeActivity);
                        }
                        activeActivity = 8;

                        Intent i = null;
                        Class<?> activity = CommonFunctions.getActivityClassByLeftView(context, SECTION_ID);
                        if (activity != null) {
                            i = new Intent(MainActivity.this, activity);
                        } else {
                            i = new Intent(MainActivity.this, DisconnectedActivity.class);
                        }

                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                } else {
                    Toast.makeText(context, "Life foundation - sprit section still not complete.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDrawerList.findViewById(R.id.textViewMyJournal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SECTION_ID = "JN";
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("JN").getLast_quote();
                }catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                if (activeActivity != 9) {
                    if (activeActivity != 0) {
                        activeActivity = 9;
                        CommonFunctions.closePreviousActivities(activeActivity);
                    }
                    activeActivity = 9;
                    Intent i = new Intent(MainActivity.this, JournalIntroActivity.class);
                    i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            }
        });

        mDrawerList.findViewById(R.id.textViewResourcesMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SECTION_ID = "RS";
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("RS").getLast_quote();
                }catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                if (activeActivity != 10) {
                    if (activeActivity != 0) {
                        activeActivity = 10;
                        CommonFunctions.closePreviousActivities(activeActivity);
                    }
                    activeActivity = 10;

                    Intent i = null;
                    Class<?> activity = CommonFunctions.getActivityClassByLeftView(context, SECTION_ID);
                    if (activity != null) {
                        i = new Intent(MainActivity.this, activity);
                    } else {
                        i = new Intent(MainActivity.this, ResourcesIntroActivity.class);
                    }

                    i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            }
        });

        mDrawerList.findViewById(R.id.textViewAbout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
                if (activeActivity != 0) {
                    activeActivity = 11;
                    CommonFunctions.closePreviousActivities(activeActivity);
                }
                activeActivity = 11;
                Intent i = new Intent(MainActivity.this, AboutIntroActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        //endregion

        if(continueMusic) {
            if (activeActivity == 0 && currentActivity != 0) {
                MusicManager.pause();
            } else {
                currentActivity = activeActivity;
                MusicManager.start(context, getMusic());
            }
        }

        if (!continueMusic) {
            imageViewSpeaker.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mute));
            imageViewSpeaker.setTag("mute");
            imageViewSpeaker.requestLayout();
            mDrawerLayout.requestLayout();
        } else {
            imageViewSpeaker.setImageDrawable(getResources().getDrawable(R.mipmap.ic_unmute));
            imageViewSpeaker.setTag("unmute");
            imageViewSpeaker.requestLayout();
            mDrawerLayout.requestLayout();
        }

        mDrawerList.findViewById(R.id.imageViewSpeaker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mDrawerLayout.closeDrawers();
                if (continueMusic) {
                    imageViewSpeaker.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mute));
                    imageViewSpeaker.requestLayout();
                    imageViewSpeaker.setTag("mute");
                    MusicManager.pause();
                    continueMusic = false;
                } else {
                    imageViewSpeaker.setImageDrawable(getResources().getDrawable(R.mipmap.ic_unmute));
                    imageViewSpeaker.requestLayout();
                    imageViewSpeaker.setTag("unmute");
                    MusicManager.start(MainActivity.this, getMusic(), true);
                    continueMusic = true;
                }
            }
        });
    }

    public static int getMusic() {
        switch (currentActivity) {
            case 1:
                return R.raw.core_beliefs;
            case 2:
                return R.raw.core_goals;
            case 3:
                return R.raw.core_status;
            case 4:
                return R.raw.life_foundations_body;
            case 5:
                return R.raw.life_foundations_soul;
            case 6:
                return R.raw.life_foundations_spirit;
            case 7:
                return R.raw.unconnected;
            case 8:
                return R.raw.disconnected;
            case 9:
                return R.raw.journal;
            case 10:
                return R.raw.resources;
            default:
                return 0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(!continueMusic) {
                MusicManager.pause();
                imageViewSpeaker.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mute));
                imageViewSpeaker.requestLayout();
            } else {
                if(activeActivity != 0 && currentActivity != 0) {
                    MusicManager.start(MainActivity.this, getMusic(), true);
                }
                imageViewSpeaker.setImageDrawable(getResources().getDrawable(R.mipmap.ic_unmute));
                imageViewSpeaker.requestLayout();
            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MusicManager.pause();
        CommonFunctions.saveSettingBoolean(MainActivity.this, CommonFunctions.SETTING_KEY_IS_CONTINUE, continueMusic);
    }

    @Override
    public void onStart() {
        super.onStart();
        continueMusic = CommonFunctions.getSettingBoolean(MainActivity.this, CommonFunctions.SETTING_KEY_IS_CONTINUE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
//        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
//                && keyCode == KeyEvent.KEYCODE_BACK
//                && event.getRepeatCount() == 0) {
//            Log.d("CDA", "onKeyDown Called");
//            onBackPressed();
//        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
//        Intent setIntent = new Intent(Intent.ACTION_MAIN);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(setIntent);
        if(currentActivity == 0 && activeActivity == 0) {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(activeActivity == 0) {
            MusicManager.release();
            CommonFunctions.saveSettingBoolean(MainActivity.this, CommonFunctions.SETTING_KEY_IS_CONTINUE, continueMusic);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();

        if (CommonFunctions.getSettingBoolean(MainActivity.this, CommonFunctions.SETTING_KEY_IS_FIRST_TIME)) {
            CommonFunctions.saveSettingBoolean(MainActivity.this, CommonFunctions.SETTING_KEY_IS_FIRST_TIME, false);
            playVideo();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {

        public CustomActionBarDrawerToggle(Activity mActivity,
                                           DrawerLayout mDrawerLayout) {
            super(mActivity, mDrawerLayout, true, R.mipmap.ic_launcher,
                    R.string.app_name, R.string.app_name);
        }

        @Override
        public void onDrawerClosed(View view) {
            if(getActionBar() != null)
                getActionBar().setTitle(mTitle);
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            if(getActionBar() != null)
                getActionBar().setTitle(mDrawerTitle);
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }
    }
}