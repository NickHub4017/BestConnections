package com.syncbridge.bestconnections;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import Classes.MusicManager;


public class MyJournalActivity extends MainActivity {

    String SECTION;
    public Context m_context;

    MediaPlayer mediaPlayer;
    private boolean isPlaying = true;

    TabHost tabHost;
    LocalActivityManager mlam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_journal);

        m_context = this.getApplicationContext();
        SECTION = getIntent().getExtras().getString("SECTION");
        activeActivity = 9;

        set(MyJournalActivity.this);

        mlam = new LocalActivityManager(this, false);
        mlam.dispatchCreate(savedInstanceState);
        Resources ressources = getResources();
        tabHost = (TabHost)findViewById(R.id.tab_host);
        tabHost.setup(mlam);


        tabHost.addTab(createTab(MyJournalQuotesActivity.class, "Journalling", "JOURNALLING"));
        tabHost.addTab(createTab(JournalAnswersActivity.class, "Answers", "ANSWERS"));
        tabHost.addTab(createTab(JournalCommentsActivity.class, "Comments", "COMMENTS"));

        tabHost.setOnTabChangedListener(tabChangeListener);
        setInitialTab();

        handleSpeakerIcon();
    }

    private TabHost.TabSpec createTab(final Class<?> intentClass, final String tag,
                                      final String title)
    {
        final Intent intent = new Intent().setClass(this, intentClass);
        intent.putExtra("SECTION", SECTION);

        final View tab = LayoutInflater.from(tabHost.getContext()).
                inflate(R.layout.layout_tab, null);
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

    private void setInitialTab() {
        tabHost.setCurrentTab(0);
//        Intent i = new Intent(JournalActivity.this, JournalQuotesActivity.class);
//        i.putExtra("SECTION", SECTION);
//        startActivity(i);
//        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
        @Override
        public void onTabChanged(String s) {
            int currentTab = tabHost.getCurrentTab();
            if(currentTab == 0) {
                //security
//                Intent i = new Intent(JournalActivity.this, JournalQuotesActivity.class);
//                i.putExtra("SECTION", SECTION);
//                startActivity(i);
//                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            } else if(currentTab == 1) {
                JournalAnswersActivity.reload();
            } else if(currentTab == 2) {
                JournalCommentsActivity.reload();
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
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
