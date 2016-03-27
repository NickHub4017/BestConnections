package com.syncbridge.bestconnections;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import DBHelper.QuoteDataSource;


public class CoreStatusIntroActivity extends MainActivity {

    public Context m_context;
    public static Handler h;
    public String lastQuoteId = "";
    public String lastQuoteSection = "";
    String SECTION;


    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_status_intro);

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

        if(getIntent().getExtras().getString("LAST_QUOTE_ID") != null) {
            lastQuoteId = getIntent().getExtras().getString("LAST_QUOTE_ID");
        }

        SECTION = getIntent().getExtras().getString("SECTION");

        if (!lastQuoteId.isEmpty()) {
            try {
                QuoteDataSource quoteDataSource = new QuoteDataSource(m_context);
                quoteDataSource.open();
                lastQuoteSection = quoteDataSource.getEntry(lastQuoteId).getSection_id();
                Intent i;
                switch (lastQuoteSection) {
                    case "CSS":
                        i = new Intent(CoreStatusIntroActivity.this, CoreStatusSecurityIntroActivity.class);
                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        break;
                    case "CSH":
                        i = new Intent(CoreStatusIntroActivity.this, CoreStatusHappinessIntroActivity.class);
                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        break;
                    case "CSM":
                        i = new Intent(CoreStatusIntroActivity.this, CoreStatusMotivationIntroActivity.class);
                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        break;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Intent i = null;
            Class<?> activity = null;

            if (CommonFunctions.getActivityClassByLeftView(m_context, "CSS") != null) {
                activity = CommonFunctions.getActivityClassByLeftView(m_context, "CSS");
                SECTION = "CSS";
            }
            if (CommonFunctions.getActivityClassByLeftView(m_context, "CSH") != null) {
                activity = CommonFunctions.getActivityClassByLeftView(m_context, "CSH");
                SECTION = "CSH";
            }
            if (CommonFunctions.getActivityClassByLeftView(m_context, "CSM") != null) {
                activity = CommonFunctions.getActivityClassByLeftView(m_context, "CSM");
                SECTION = "CSM";
            }

            if (activity != null) {
                i = new Intent(CoreStatusIntroActivity.this, activity);
                i.putExtra("SECTION", SECTION);
                i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                startActivity(i);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        }

        SECTION_ID = "CS";
        activeActivity = 3;
        set(CoreStatusIntroActivity.this);

        CommonFunctions.updateLeftView(m_context, SECTION_ID, 1);

        ((TextView)findViewById(R.id.textViewIntro1)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewS)).setTypeface(CommonFunctions.getTypeFace(m_context, 6));
        ((TextView)findViewById(R.id.textViewH)).setTypeface(CommonFunctions.getTypeFace(m_context, 6));
        ((TextView)findViewById(R.id.textViewM)).setTypeface(CommonFunctions.getTypeFace(m_context, 6));
        ((TextView)findViewById(R.id.textViewIntro1S)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewIntroH)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewIntro2)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewIntro3)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewIntro1M)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewIntro2M)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewIntro3M)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutCoreStatus);
        final RelativeLayout relativeLayoutS = (RelativeLayout) findViewById(R.id.relativeLayoutCoreStatusS);
        final RelativeLayout relativeLayoutH = (RelativeLayout) findViewById(R.id.relativeLayoutCoreStatusH);
        final RelativeLayout relativeLayoutM = (RelativeLayout) findViewById(R.id.relativeLayoutCoreStatusM);

        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayout.startAnimation(AnimationUtils.loadAnimation(CoreStatusIntroActivity.this, R.anim.fadein));

        findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.INVISIBLE);
                relativeLayoutS.setVisibility(View.VISIBLE);
                relativeLayoutS.startAnimation(AnimationUtils.loadAnimation(CoreStatusIntroActivity.this, R.anim.fadein));
            }
        });

        findViewById(R.id.imageViewNextS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutS.setVisibility(View.INVISIBLE);
                relativeLayoutH.setVisibility(View.VISIBLE);
                relativeLayoutH.startAnimation(AnimationUtils.loadAnimation(CoreStatusIntroActivity.this, R.anim.fadein));
            }
        });

        findViewById(R.id.imageViewNextH).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutH.setVisibility(View.INVISIBLE);
                relativeLayoutM.setVisibility(View.VISIBLE);
                relativeLayoutM.startAnimation(AnimationUtils.loadAnimation(CoreStatusIntroActivity.this, R.anim.fadein));
            }
        });

        findViewById(R.id.imageViewNextM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoreStatusIntroActivity.this, CoreStatusSecurityIntroActivity.class);
                //Intent i = new Intent(CoreStatusIntroActivity.this, CoreStatusSecurityQuizActivity.class);
                i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                startActivity(i);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

                relativeLayoutM.setVisibility(View.INVISIBLE);
                //relativeLayout.setVisibility(View.VISIBLE);
            }
        });

        //region cs on click

        findViewById(R.id.textViewS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoreStatusIntroActivity.this, CoreStatusSecurityIntroActivity.class);
                i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                startActivity(i);
                //finish();
                // close this activity
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        findViewById(R.id.textViewH).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoreStatusIntroActivity.this, CoreStatusHappinessIntroActivity.class);
                i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                startActivity(i);
                //finish();
                // close this activity
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        findViewById(R.id.textViewM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoreStatusIntroActivity.this, CoreStatusMotivationIntroActivity.class);
                i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                startActivity(i);
                //finish();
                // close this activity
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
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

        if (id == R.id.action_menu_back) {
            activeActivity = 0;
            currentActivity = 0;
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        relativeLayout.setVisibility(View.VISIBLE);

        activeActivity = 3;
        currentActivity = 3;
        CommonFunctions.updateLeftView(m_context, SECTION_ID, 1);

        super.onResume();
    }
}
