package com.syncbridge.bestconnections;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.SQLException;

import Classes.CommonFunctions;
import Classes.MusicManager;


public class CoreStatusClose1Activity extends MainActivity {

    public Context m_context;
    public static Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_status_close1);

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

        m_context = this.getApplicationContext();

        SECTION_ID = "CS";
        activeActivity = 3;
        set(m_context);

        final RelativeLayout relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        final RelativeLayout relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);

        relativeLayout1.setVisibility(View.VISIBLE);
        relativeLayout1.startAnimation(AnimationUtils.loadAnimation(CoreStatusClose1Activity.this, R.anim.fadein));

        ((TextView)findViewById(R.id.textViewClose1)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewClose2)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewClose11)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewClose22)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewClose33)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout1.setVisibility(View.INVISIBLE);
                relativeLayout2.setVisibility(View.VISIBLE);
                relativeLayout2.startAnimation(AnimationUtils.loadAnimation(CoreStatusClose1Activity.this, R.anim.fadein));
            }
        });

        //region next buttons

        findViewById(R.id.imageViewNextBeliefs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Class<?> activity = CommonFunctions.getActivityClassByLeftView(m_context, SECTION_ID);
                    if(activity != null) {
                        i = new Intent(CoreStatusClose1Activity.this, activity);
                    } else {
                        i = new Intent(CoreStatusClose1Activity.this, CoreBelifeIntroActivity.class);
                    }

                    i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            }
        });

        findViewById(R.id.imageViewNextGoals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Class<?> activity = CommonFunctions.getActivityClassByLeftView(m_context, SECTION_ID);
                    if (activity != null) {
                        i = new Intent(CoreStatusClose1Activity.this, activity);
                    } else {
                        i = new Intent(CoreStatusClose1Activity.this, CoreGoalsIntroActivity.class);
                    }

                    i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            }
        });

        findViewById(R.id.imageViewNextJournal).setOnClickListener(new View.OnClickListener() {
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
                        Intent i = new Intent(CoreStatusClose1Activity.this, JournalIntroActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    } else {
                        Intent i = new Intent(CoreStatusClose1Activity.this, JournalIntroActivity.class);
                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                }
            }
        });

        findViewById(R.id.imageViewNextLifeFoundations).setOnClickListener(new View.OnClickListener() {
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
                        Intent i = new Intent(CoreStatusClose1Activity.this, LifeFoundationActivity.class);
                        i.putExtra("SECTION", "LFBO");
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    } else {
                        Intent i = new Intent(CoreStatusClose1Activity.this, LifeFoundationActivity.class);
                        i.putExtra("SECTION", "LFBO");
                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                }
            }
        });

        //endregion

        handleSpeakerIcon();
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
            SECTION_ID = "";
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
