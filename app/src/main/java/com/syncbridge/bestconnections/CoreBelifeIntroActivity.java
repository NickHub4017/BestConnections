package com.syncbridge.bestconnections;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.sql.SQLException;

import Classes.CommonFunctions;
import Classes.MusicManager;


public class CoreBelifeIntroActivity extends MainActivity {

    public Context m_context;

    public static Handler h;

    RelativeLayout relativeLayout;
    RelativeLayout relativeLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_belife_intro);

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

        final String lastQuoteId = getIntent().getExtras().getString("LAST_QUOTE_ID");
        if(!lastQuoteId.isEmpty()) {
            Intent i = new Intent(CoreBelifeIntroActivity.this, CoreBeliefsActivity.class);
            i.putExtra("LAST_QUOTE_ID", lastQuoteId);
            startActivity(i);
            //overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

        m_context = this.getApplicationContext();

        SECTION_ID = "CB";
        activeActivity = 1;
        set(m_context);

        CommonFunctions.updateLeftView(m_context, SECTION_ID, 1);
        handleSpeakerIcon();

        ((TextView)findViewById(R.id.textViewIntro1)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));
        ((TextView)findViewById(R.id.textViewIntro)).setTypeface(CommonFunctions.getTypeFace(m_context, 2));

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutCoreBelifesIntro);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayoutCoreBelifesIntro2);

        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayout.startAnimation(AnimationUtils.loadAnimation(CoreBelifeIntroActivity.this, R.anim.fadein));


        TextView textViewIntro = (TextView) relativeLayout2.findViewById(R.id.textViewIntro);
        SpannableStringBuilder ssb = new SpannableStringBuilder( getResources().getText(R.string.string_core_belifes_intro2) );
        URLSpan urlSpan = new URLSpan( "Add to Journal" )
        {
            @Override
            public void onClick( View widget )
            {
                SECTION_ID = "JN";
                String lastQuoteId = "";
                try {
                    sectionDataSource.open();
                    lastQuoteId = sectionDataSource.getEntry("JN").getLast_quote();
                }catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                if (activeActivity != 9) {
                    activeActivity = 9;
                    if(lastQuoteId.isEmpty()) {
                        Intent i = new Intent(CoreBelifeIntroActivity.this, JournalIntroActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    } else {
                        Intent i = new Intent(CoreBelifeIntroActivity.this, JournalIntroActivity.class);
                        i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                }
            }
        };
        ssb.setSpan( urlSpan, 40, 54, Spannable.SPAN_INCLUSIVE_INCLUSIVE );
        textViewIntro.setText(ssb, TextView.BufferType.SPANNABLE);
        textViewIntro.setMovementMethod(LinkMovementMethod.getInstance());


        findViewById(R.id.imageViewNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.INVISIBLE);
                relativeLayout2.setVisibility(View.VISIBLE);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        findViewById(R.id.imageViewNext2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoreBelifeIntroActivity.this, CoreBeliefsActivity.class);
                i.putExtra("LAST_QUOTE_ID", lastQuoteId);
                startActivity(i);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });
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

        activeActivity = 1;
        currentActivity = 1;
        CommonFunctions.updateLeftView(m_context, SECTION_ID, 1);

        super.onResume();
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        SECTION_ID = "";
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        activeActivity = 0;
//        currentActivity = 0;
//        this.finish();
//    }

//    private class UpdateUI extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (continueMusic) {
//                MusicManager.pause();
//                continueMusic = false;
//            } else {
//                MusicManager.start(m_context, getMusic(), true);
//                continueMusic = true;
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void voidd) {
//            if (!continueMusic) {
//                imageViewSpeaker.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mute));
//                imageViewSpeaker.requestLayout();
//                imageViewSpeaker.setTag("mute");
//            } else {
//                imageViewSpeaker.setImageDrawable(getResources().getDrawable(R.mipmap.ic_unmute));
//                imageViewSpeaker.requestLayout();
//                imageViewSpeaker.setTag("unmute");
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//        }
//    }
}

