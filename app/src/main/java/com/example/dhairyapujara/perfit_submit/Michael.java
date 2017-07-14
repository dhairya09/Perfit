package com.example.dhairyapujara.perfit_submit;

/*import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import min3d.core.Object3dContainer;
import min3d.core.RendererActivity;
import min3d.core.RendererActivity;
import min3d.parser.IParser;
import min3d.parser.Parser;
import min3d.vos.Light;*/
import android.app.AlertDialog;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import min3d.core.RendererActivity;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.rajawali3d.surface.RajawaliSurfaceView;


public class Michael extends AppCompatActivity {

    // private Object3dContainer faceObject3D;
    GLSurfaceView.Renderer renderer;
    public RajawaliSurfaceView rajawaliSurface;
    //private Object3dContainer objModel;
    private Button flipButton;
    private Button saluteButton;
    private static final String TAG = "LOGGGGGGGGGGGGGGGGGGGG";
    public AlertDialog alertDialog;
    String[] SPINNERLIST = {"XS", "S", "M", "L", "XL"};
    String[] COLORLIST = {"Red", "Blue", "Green", "Black"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_michael);
        rajawaliSurface = (RajawaliSurfaceView) findViewById(R.id.rajawali_surface);
        Renderer renderer = new Renderer(this);
        rajawaliSurface.setSurfaceRenderer(renderer);
     /*   RelativeLayout ll = (RelativeLayout) this.findViewById(R.id.scene1Holder);
        ll.addView(_glSurfaceView);*/

        /*flipButton = (Button) this.findViewById(R.id.button2);

        flipButton.setOnClickListener(this);
        saluteButton = (Button) this.findViewById(R.id.button3);
        saluteButton.setOnClickListener(this);*/

        final ImageView txt_show1 = (ImageView) findViewById(R.id.imageView5);
        final ImageView txt_show2 = (ImageView) findViewById(R.id.imageView4);
        final TextView txt = (TextView) findViewById(R.id.heading);
        // final TextView txt1 = (TextView) findViewById(R.id.Details);

        txt_show1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearn = (LinearLayout) findViewById(R.id.ln1);
                linearn.setVisibility(View.VISIBLE);
                txt_show1.setBackgroundResource(R.drawable.high);
                txt_show2.setBackgroundResource(R.drawable.highlight);
                txt.setBackgroundColor(Color.parseColor("#ffffff"));
                txt.setText("Mara Hoffman Women's Drop Wst Midi Dress - 29.99$");


            }
        });
        txt_show2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearn = (LinearLayout) findViewById(R.id.ln1);
                linearn.setVisibility(View.VISIBLE);
                txt_show1.setBackgroundResource(R.drawable.highlight);
                txt_show2.setBackgroundResource(R.drawable.high);
                txt.setBackgroundColor(Color.parseColor("#ffffff"));
                txt.setText("HARVARD Black Puffer Jacket with Detachable Hood - 34.45$");


            }
        });


        ImageView info = (ImageView) findViewById(R.id.info);



        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = (TextView) findViewById(R.id.txtalert);
                txt.setText("Black woven puffer jacket, padded, has a mock collar, detachable hood with faux fur trimming and press-button closures, long sleeves, a full press-button placket with a concealed zip closure, two insert pockets, an attached lining\n");
                LinearLayout lvdialog = (LinearLayout) findViewById(R.id.tran);
                lvdialog.setVisibility(View.VISIBLE);


            }
        });

        ImageView clobut = (ImageView) findViewById(R.id.close);
        clobut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout lvdialog = (LinearLayout) findViewById(R.id.tran);
                lvdialog.setVisibility(View.INVISIBLE);
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        MaterialBetterSpinner betterSpinner = (MaterialBetterSpinner) findViewById(R.id.size);
        betterSpinner.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, COLORLIST);
        MaterialBetterSpinner betterSpinner1 = (MaterialBetterSpinner) findViewById(R.id.colorSpinner);
        betterSpinner1.setAdapter(arrayAdapter1);

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}





/* info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this,  R.style.full_screen_dialog));

               // final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Information");
                // set dialog message
                alertDialogBuilder.setMessage("Black woven puffer jacket, padded, has a mock collar, detachable hood with faux fur trimming and press-button closures, long sleeves, a full press-button placket with a concealed zip closure, two insert pockets, an attached lining\n" +
                        "\n").setCancelable(false);

                // cre ate alert dialog
                alertDialogBuilder.setNegativeButton("Okay",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                alertDialog = alertDialogBuilder.create();



                // show it
                alertDialog.show();




            }
        });*/