package com.syncbridge.bestconnections;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
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


public class CoreGoalsIntroActivity extends MainActivity {

    public Context m_context;
    public static Handler h;
    public String lastQuoteId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_goals_intro);

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

        if(getIntent().getExtras().getString("LAST_QUOTE_ID") != null) {
            lastQuoteId = getIntent().getExtras().getString("LAST_QUOTE_ID");
        }

        m_context = this.getApplicationContext();

        SECTION_ID = "CG";
        activeActivity = 2;
        set(m_context);

        CommonFunctions.updateLeftView(m_context, SECTION_ID, 1);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutCoreGoalsIntro);
        relativeLayout.startAnimation(AnimationUtils.loadAnimation(CoreGoalsIntroActivity.this, R.anim.fadein));

        TextView textViewIntro = (TextView) findViewById(R.id.textViewIntro1);
        textViewIntro.setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        SpannableStringBuilder ssb = new SpannableStringBuilder( getResources().getText(R.string.string_core_goals_intro) );
        Bitmap apple = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cb);
        Bitmap resized = Bitmap.createScaledBitmap(apple, (int)(apple.getWidth()*2), (int)(apple.getHeight()*2), true);
        ssb.setSpan(new ImageSpan(resized), 55, 56, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        URLSpan urlSpan = new URLSpan( "Core Beliefs" )
        {
            @Override
            public void onClick( View widget )
            {
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("CB").getLast_quote();
                } catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                activeActivity = 1;
                CommonFunctions.closePreviousActivities(activeActivity);
                Intent i = new Intent(CoreGoalsIntroActivity.this, CoreBelifeIntroActivity.class);
                i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                startActivity(i);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        };
        ssb.setSpan( urlSpan, 461, 474, Spannable.SPAN_INCLUSIVE_INCLUSIVE );
        textViewIntro.setText( ssb, TextView.BufferType.SPANNABLE );
        textViewIntro.setMovementMethod(LinkMovementMethod.getInstance());

//        textViewIntro.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    sectionDataSource.open();
//                    lastQuoteId = sectionDataSource.getEntry("CB").getLast_quote();
//                } catch (SQLException ex) {
//                    Log.e(TAG, ex.getMessage(), ex);
//                }
//                activeActivity = 1;
//                CommonFunctions.closePreviousActivities(activeActivity);
//                Intent i = new Intent(CoreGoalsIntroActivity.this, CoreBelifeIntroActivity.class);
//                i.putExtra("LAST_QUOTE_ID", lastQuoteId);
//                startActivity(i);
//                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
//            }
//        });

        findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoreGoalsIntroActivity.this, CoreGoalsIntro2Activity.class);
                i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                startActivity(i);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

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

        activeActivity = 2;
        currentActivity = 2;
        CommonFunctions.updateLeftView(m_context, SECTION_ID, 1);

        super.onResume();
    }
}
