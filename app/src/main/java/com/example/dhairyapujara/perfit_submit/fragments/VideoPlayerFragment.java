package com.example.dhairyapujara.perfit_submit.fragments;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import com.example.dhairyapujara.perfit_submit.R;
import com.example.dhairyapujara.perfit_submit.SignupActivity;
import com.example.dhairyapujara.perfit_submit.VideoPlayerActivity;
import com.example.dhairyapujara.perfit_submit.nextscreen;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPlayerFragment extends Fragment {



    public VideoPlayerFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);
        final Button skip = (Button)view.findViewById(R.id.Skip);
        final Button next = (Button)view.findViewById(R.id.Next);
        final Button replay = (Button)view.findViewById(R.id.replay);
        final VideoView videoView = (VideoView)view.findViewById(R.id.fragmentvideoplayer_videoview);

        next.setVisibility(View.GONE);
        next.setEnabled(false);

        replay.setEnabled(false);
        replay.setVisibility(View.GONE);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),nextscreen.class);
                startActivity(i);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),nextscreen.class);
                startActivity(i);
            }
        });

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.start();
                replay.setVisibility(View.GONE);
                replay.setEnabled(false);
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                skip.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                replay.setVisibility(View.VISIBLE);

                next.setEnabled(true);
                skip.setEnabled(false);
                replay.setEnabled(true);
            }
        });

        //return inflater.inflate(R.layout.fragment_video_player, container, false);
        return view;
    }

}
