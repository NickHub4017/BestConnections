package com.syncbridge.bestconnections;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import Classes.CommonFunctions;
import Classes.MusicManager;

public class LifeFoundationActivity extends MainActivity {

    public Context m_context;
    DrawerLayout drawerLayout;

    MediaPlayer mediaPlayer;
    private boolean isPlaying = true;
    private int currentTrack = 0;

    public static TabHost tabHost;
    LocalActivityManager mlam;


    public static Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_foundation);

        h = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch(msg.what) {

                    case 0:
                        finish();
                        break;
                }
            }
        };

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        m_context = this.getApplicationContext();
        SECTION_ID = "LF";
        set(m_context);

        mlam = new LocalActivityManager(this, false);
        mlam.dispatchCreate(savedInstanceState);

        tabHost = (TabHost)findViewById(R.id.tab_host);
        tabHost.setup(mlam);

        tabHost.addTab(createTab(LifeFoundationBodyActivity.class, "LFBO", "BODY"));
        tabHost.addTab(createTab(LifeFoundationSoulActivity.class, "LFSO", "SOUL"));
        tabHost.addTab(createTab(LifeFoundationSpiritActivity.class, "LFSP", "SPIRIT"));

        tabHost.setOnTabChangedListener(tabChangeListener);
        setInitialTab(getIntent().getExtras().getString("SECTION"));

        handleSpeakerIcon();
    }

    private TabHost.TabSpec createTab(final Class<?> intentClass, final String tag, final String title) {
        final Intent intent = new Intent().setClass(this, intentClass);
        intent.putExtra("SECTION", tag);

        final View tab = LayoutInflater.from(tabHost.getContext()).inflate(R.layout.layout_tab, null);
        ((TextView)tab.findViewById(R.id.tab_text)).setText(title);

        return tabHost.newTabSpec(tag).setIndicator(tab).setContent(intent);
    }

    private void handleSpeakerIcon() {

        if (!continueMusic) {
            ((ImageView) findViewById(R.id.imageViewSpeaker)).setImageResource(R.mipmap.ic_mute);
        } else {
            ((ImageView) findViewById(R.id.imageViewSpeaker)).setImageResource(R.mipmap.ic_unmute);
        }

        ((ImageView) findViewById(R.id.imageViewSpeaker)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mDrawerLayout.closeDrawers();
                if (continueMusic) {
                    ((ImageView) findViewById(R.id.imageViewSpeaker)).setImageResource(R.mipmap.ic_mute);
                    ((ImageView) findViewById(R.id.imageViewSpeaker)).requestLayout();
                    MusicManager.pause();
                    continueMusic = false;
                } else {
                    currentActivity = activeActivity;
                    ((ImageView) findViewById(R.id.imageViewSpeaker)).setImageResource(R.mipmap.ic_unmute);
                    ((ImageView) findViewById(R.id.imageViewSpeaker)).requestLayout();
                    MusicManager.start(m_context, getMusic(), true);
                    continueMusic = true;
                }
            }
        });
    }

    private void setInitialTab(String sec) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        switch (sec) {
            case "LFBO":
                currentTrack = R.raw.life_foundations_body;

                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    drawerLayout.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_lf_bo_background));
                } else {
                    drawerLayout.setBackground(getResources().getDrawable(R.mipmap.ic_lf_bo_background));
                }
                tabHost.setCurrentTab(0);
                break;
            case "LFSO":

                currentTrack = R.raw.life_foundations_soul;


                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    drawerLayout.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_lf_so_background));
                } else {
                    drawerLayout.setBackground(getResources().getDrawable(R.mipmap.ic_lf_so_background));
                }
                tabHost.setCurrentTab(1);
                break;
            case "LFSP":

                currentTrack = R.raw.life_foundations_spirit;


                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    drawerLayout.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_lf_sp_background));
                } else {
                    drawerLayout.setBackground(getResources().getDrawable(R.mipmap.ic_lf_sp_background));
                }
                tabHost.setCurrentTab(2);
                break;
        }
    }

    TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
        @Override
        public void onTabChanged(String s) {
            int sdk = android.os.Build.VERSION.SDK_INT;
            int currentTab = tabHost.getCurrentTab();
            if (currentTab == 0) {
                currentTrack = R.raw.life_foundations_body;
                activeActivity = 4;
                set(LifeFoundationActivity.this);
                if (continueMusic) {
                    MusicManager.start(getApplicationContext(), currentTrack, true);
                }
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    drawerLayout.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_lf_bo_background));
                } else {
                    drawerLayout.setBackground(getResources().getDrawable(R.mipmap.ic_lf_bo_background));
                }
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            } else if (currentTab == 1) {
                if (CommonFunctions.isSectionFinished(m_context, "LFBO")) {
                    currentTrack = R.raw.life_foundations_soul;
                    activeActivity = 5;
                    set(LifeFoundationActivity.this);
                    if (continueMusic) {
                        MusicManager.start(getApplicationContext(), currentTrack, true);
                    }
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        drawerLayout.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_lf_so_background));
                    } else {
                        drawerLayout.setBackground(getResources().getDrawable(R.mipmap.ic_lf_so_background));
                    }
                } else {
                    tabHost.setCurrentTab(0);
                    Toast.makeText(m_context, "Previous section still not complete.", Toast.LENGTH_SHORT).show();
                }
            } else if (currentTab == 2) {
                if (CommonFunctions.isSectionFinished(m_context, "LFSO")) {
                    currentTrack = R.raw.life_foundations_spirit;
                    activeActivity = 6;
                    set(LifeFoundationActivity.this);
                    if (continueMusic) {
                        MusicManager.start(getApplicationContext(), currentTrack, true);
                    }
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        drawerLayout.setBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_lf_sp_background));
                    } else {
                        drawerLayout.setBackground(getResources().getDrawable(R.mipmap.ic_lf_sp_background));
                    }
                } else {
                    tabHost.setCurrentTab(1);
                    Toast.makeText(m_context, "Previous section still not complete.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_menu_back) {

            switch (tabHost.getCurrentTab()) {
                case 0:
                    if(LifeFoundationBodyActivity.backControl()) {
                        activeActivity = 0;
                        currentActivity = 0;
                        this.finish();
                    }
                    break;
                case 1:
                    if(LifeFoundationSoulActivity.backControl()) {
                        activeActivity = 0;
                        currentActivity = 0;
                        this.finish();
                    }
                    break;
                case 2:
                    if(LifeFoundationSpiritActivity.backControl()) {
                        activeActivity = 0;
                        currentActivity = 0;
                        this.finish();
                    }
                    break;
                default:
                    activeActivity = 0;
                    currentActivity = 0;
                    this.finish();
                    break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {

//        activeActivity = 8;
//        currentActivity = 8;
        //CommonFunctions.updateLeftView(m_context, SECTION_ID, 1);

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        if(currentActivity == 0 && activeActivity == 0) {
            super.onBackPressed();
        }
    }
}
