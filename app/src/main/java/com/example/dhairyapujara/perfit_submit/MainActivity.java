package com.example.dhairyapujara.perfit_submit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.dhairyapujara.perfit_submit.entities.Video;

public class MainActivity extends BaseActivity {

    final Activity activity = this;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //goToVideoPlayerActivity();
        //finish();
        //startActivity(new Intent(this, OrientationActivity.class));
    }

    public void goToVideoPlayerActivity(){
        Intent intent = null;
        try {
            intent = new Intent(MainActivity.this,VideoPlayerActivity.class);
        } finally {
            // Create a video object to be passed to the activity
            //Video video = new Video("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4");
            //Video video = new Video("http://www.ebookfrenzy.com/android_book/movie.mp4");
            Video video = new Video("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4");
            video.setTitle("Big Buck Bunny");
            video.setAuthor("the Blender Institute");
            video.setDescription("A short computer animated film by the Blender Institute, part of the Blender Foundation. Like the foundation's previous film Elephants Dream, the film was made using Blender, a free software application for animation made by the same foundation. It was released as an Open Source film under Creative Commons License Attribution 3.0.");


            //Log.e("layout:", String.valueOf(layout));
            Log.e("author:",video.getAuthor());
            Log.e("title:",video.getTitle());
            Log.e("desc:",video.getDescription());
            Log.e("url:",video.getUrl());
            Log.e("String_che:",Video.class.getName());
            // Launch the activity with some extras
            intent.putExtra(ImmersiveVideoplayer.EXTRA_LAYOUT, "1");
            intent.putExtra(Video.class.getName(), video);
            startActivity(intent);
        }
    }
}
