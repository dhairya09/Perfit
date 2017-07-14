package com.example.dhairyapujara.perfit_submit;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Belal on 18/09/16.
 */


public class dg extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        final View view = inflater.inflate(R.layout.dg_fragment, container, false);

        ImageView txt_show1 = (ImageView) view.findViewById(R.id.imageDG);

        txt_show1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ImageClicked",
                        Toast.LENGTH_SHORT).show();

            }
        });

        return inflater.inflate(R.layout.dg_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles 
        getActivity().setTitle("Dolce Gabbana");
    }
}