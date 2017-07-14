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

public class Left_Activity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST =1;
    private Uri filePath;
    private Bitmap bitmap;
    private Bitmap b;
    ImageView iv;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_);
        btn = (Button)findViewById(R.id.buttonChoose);


        if(getIntent().hasExtra("picture_front")){
            b = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("picture_front"),0,getIntent().getBooleanArrayExtra("picture_front").length);
        }

        iv = (ImageView)findViewById(R.id.fragment_leftface);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filechooser();
            }
        });


        //Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        b.compress(Bitmap.CompressFormat.PNG, 100, stream1);

        byte[] byteArray = stream.toByteArray();
        byte[] byteArray1 = stream1.toByteArray();

        Intent intent = new Intent(Left_Activity.this, Back_Activity.class);
        intent.putExtra("picture_front_passed",byteArray1);
        intent.putExtra("picture_left", byteArray);
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
