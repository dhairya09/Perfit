package com.example.dhairyapujara.perfit_submit;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Belal on 18/09/16.
 */


public class mk extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        View view = inflater.inflate(R.layout.mk_fragment, container, false);
        final ImageView txt_show1 = (ImageView) view.findViewById(R.id.imageView5);
        final ImageView txt_show2 = (ImageView) view.findViewById(R.id.imageView4);
        final TextView txt=(TextView) view.findViewById(R.id.heading);
        final TextView txt1=(TextView) view.findViewById(R.id.Details);
        txt_show1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_show1.setBackgroundResource(R.drawable.high);
                txt_show2.setBackgroundResource(R.drawable.highlight);
                txt.setBackgroundColor(Color.parseColor("#ffffff"));
                txt.setText("Mara Hoffman Women's Drop Wst Midi Dress");
                txt1.setBackgroundColor(Color.parseColor("#FFF0F5"));
                txt1.setText("Navy blue printed Jodhpuri jumpsuit, has a shirt collar with a V-neckline, sleeveless, a half button placket with a short zip closure, two insert pockets, harem pants with pleated detail, drawstring fastenings towards the hems\n\n");
                txt1.append(" • 95% Viscose, 5% Elastane \n • Imported \n • Hand Wash \n • Exclusive Print \n");
                txt1.append("\n");
                txt1.append("Price: $99.99 & Free Returns \n");
                txt1.append("Colors: Red,Blue,Pink,green\n");
                txt1.append("Sizes: XS,S,M,L,XL,XXL");

            Intent intent=new Intent(getContext(),MainActivity.class);
                startActivity(intent);

            }
        });

        txt_show2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_show1.setBackgroundResource(R.drawable.highlight);
                txt_show2.setBackgroundResource(R.drawable.high);
                txt.setBackgroundColor(Color.parseColor("#ffffff"));
                txt.setText("HARVARD Black Puffer Jacket with Detachable Hood");
                txt1.setBackgroundColor(Color.parseColor("#FFF0F5"));
                txt1.setText("Black woven puffer jacket, padded, has a mock collar, detachable hood with faux fur trimming and press-button closures, long sleeves, a full press-button placket with a concealed zip closure, two insert pockets, an attached lining\n\n");
                txt1.append(" • 95% Viscose, 5% Elastane \n • Imported \n • Hand Wash \n • Exclusive Print \n");
                txt1.append("\n");
                txt1.append("Price: $150.99 & Free Returns \n");
                txt1.append("Colors: Red,Blue\n");
                txt1.append("Sizes: XS,S,M,L,XL,XXL");

            }
        });

        return view;





    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Michael Kors");



    }



}