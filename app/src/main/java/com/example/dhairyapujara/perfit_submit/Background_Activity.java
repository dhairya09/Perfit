package com.example.dhairyapujara.perfit_submit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Background_Activity extends AppCompatActivity {


    private int PICK_IMAGE_REQUEST =1;
    private Uri filePath;

    private Bitmap bitmap;
    private Bitmap b;
    private Bitmap b2;
    private Bitmap b3;
    ImageView iv;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_);

        if(getIntent().hasExtra("picture_front_passed_passed")){
            b = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("picture_front_passed_passed"),0,getIntent().getBooleanArrayExtra("picture_front_passed_passed").length);
        }

        if(getIntent().hasExtra("picture_left_passed")){
            b2 = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("picture_left_passed"),0,getIntent().getBooleanArrayExtra("picture_left_passed").length);
        }

        if(getIntent().hasExtra("picture_back")){
            b3 = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("picture_back"),0,getIntent().getBooleanArrayExtra("picture_back").length);
        }

        iv = (ImageView)findViewById(R.id.fragment_background);
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
        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        b.compress(Bitmap.CompressFormat.PNG, 100, stream1);
        b2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
        b3.compress(Bitmap.CompressFormat.PNG, 100, stream3);

        byte[] byteArray = stream.toByteArray();
        byte[] byteArray1 = stream1.toByteArray();
        byte[] byteArray2 = stream2.toByteArray();
        byte[] byteArray3 = stream3.toByteArray();


        Intent intent = new Intent(Background_Activity.this, Display_Images.class);
        intent.putExtra("picture_front_passed_passed_passed",byteArray1);
        intent.putExtra("picture_left_passed_passed", byteArray2);
        intent.putExtra("picture_back_passed", byteArray3);
        intent.putExtra("picture_background", byteArray);
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
