package com.example.dhairyapujara.perfit_submit.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dhairyapujara.perfit_submit.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoDescriptionFragment extends Fragment {


    public VideoDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_video_description, container, false);
    }

}
