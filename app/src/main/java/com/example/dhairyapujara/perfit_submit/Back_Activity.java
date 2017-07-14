package com.example.dhairyapujara.perfit_submit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Back_Activity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST =1;
    private Uri filePath;
    private Bitmap bitmap;
    private Bitmap b;
    private Bitmap b2;
    ImageView iv;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_);

        if(getIntent().hasExtra("picture_front_passed")){
            b = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("picture_front_passed"),0,getIntent().getBooleanArrayExtra("picture_front_passed").length);
        }

        if(getIntent().hasExtra("picture_left")){
            b2 = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("picture_left"),0,getIntent().getBooleanArrayExtra("picture_left").length);
        }

        iv = (ImageView)findViewById(R.id.fragment_backface);
        btn = (Button)findViewById(R.id.buttonChoose);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filechooser();
            }
        });


        //Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        b.compress(Bitmap.CompressFormat.PNG, 100, stream1);
        b2.compress(Bitmap.CompressFormat.PNG, 100, stream2);

        byte[] byteArray = stream.toByteArray();
        byte[] byteArray1 = stream1.toByteArray();
        byte[] byteArray2 = stream2.toByteArray();


        Intent intent = new Intent(Back_Activity.this, Background_Activity.class);
        intent.putExtra("picture_front_passed_passed",byteArray1);
        intent.putExtra("picture_left_passed", byteArray2);
        intent.putExtra("picture_back", byteArray);
        startActivity(intent);
    }

    private void filechooser()
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(i.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
