package com.syncbridge.bestconnections;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import Classes.CommonFunctions;
import Classes.MusicManager;


public class ResourcesClose2Activity extends MainActivity {

    public Context m_context;
    public static Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources_close2);

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

        SECTION_ID = "RS";
        activeActivity = 10;
        set(m_context);

        ((TextView)findViewById(R.id.textViewClose1)).setTypeface(CommonFunctions.getTypeFace(m_context, 3));
        ((TextView)findViewById(R.id.textViewClose5)).setTypeface(CommonFunctions.getTypeFace(m_context, 11));
        ((TextView)findViewById(R.id.textViewClose6)).setTypeface(CommonFunctions.getTypeFace(m_context, 11));
        ((TextView)findViewById(R.id.textViewClose2)).setTypeface(CommonFunctions.getTypeFace(m_context, 3));
        ((TextView)findViewById(R.id.textViewClose3)).setTypeface(CommonFunctions.getTypeFace(m_context, 3));
        ((TextView)findViewById(R.id.textViewClose4)).setTypeface(CommonFunctions.getTypeFace(m_context, 3));

        findViewById(R.id.imageViewClose1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //http://connect4life.org.uk/church-growth-resources
                Uri uri = Uri.parse("http://connect4life.org.uk/church-growth-resources");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        findViewById(R.id.textViewClose5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //http://connect4life.org.uk/church-growth-resources
                Uri uri = Uri.parse("http://www.findachurch.org.uk");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        findViewById(R.id.textViewClose6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //http://connect4life.org.uk/church-growth-resources
                Uri uri = Uri.parse("http://www.groundwire.org.uk/index.php/connect-menu/chat-now");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        findViewById(R.id.imageViewCloseTwitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //http://connect4life.org.uk/best_connections_android/"
                Uri uri = Uri.parse("http://connect4life.org.uk/best_connections_android/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        findViewById(R.id.imageViewCloseGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //http://connect4life.org.uk/best_connections_android/"
                Uri uri = Uri.parse("http://connect4life.org.uk/best_connections_android/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        findViewById(R.id.imageViewCloseFacebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //http://connect4life.org.uk/best_connections_android/"
                Uri uri = Uri.parse("http://connect4life.org.uk/best_connections_android/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
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

        if(id == R.id.action_menu_back) {
            //SECTION_ID = "";
            //mediaPlayer.stop();
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
