package com.syncbridge.bestconnections;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class VidioPlayTesting extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_vidio);

        VideoView vv = (VideoView)this.findViewById(R.id.videoViewC4L);
        String uriString = "android.resource://raw.video_c4l.mp4";
        System.out.println("#########################################"+getPackageName()+"###################################################3");
        vv.setVideoURI(Uri.parse(uriString));
        vv.start();

        System.out.println(getPackageName());
    }
}
