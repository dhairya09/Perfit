package com.example.dhairyapujara.perfit_submit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


import com.example.dhairyapujara.perfit_submit.entities.Video;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(Splash.this, VideoPlayerActivity.class);

                    Video video = new Video("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4");

                    video.setTitle("PERFIT - SHOP BETTER, RETURN NEVER");
                    video.setAuthor("Dhairya Pujara");
                    video.setDescription("Take few pictures of yourself in tight clothing. We generate a 3D model of you. Try clothes on yourself. See how it fits and drapes like a dressing room.");


                    //Log.e("layout:", String.valueOf(layout));
                    Log.e("author:",video.getAuthor());
                    Log.e("title:",video.getTitle());
                    Log.e("desc:",video.getDescription());
                    Log.e("url:",video.getUrl());
                    Log.e("String_che:",Video.class.getName());
                    // Launch the activity with some extras
                    intent.putExtra(ImmersiveVideoplayer.EXTRA_LAYOUT, "0");
                    intent.putExtra(Video.class.getName(), video);
                    startActivity(intent);

                }
            }
        };
        timer.start();
    }

}
