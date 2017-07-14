package com.example.dhairyapujara.perfit_submit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;



import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.dhairyapujara.perfit_submit.R.id.textView;

//import com.example.dhairyapujara.perfit2.adapter.ImageAdapter;

public class Display_Images extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    // private int PICK_IMAGE_REQUEST =1;
    //private Uri filePath;

    private File[] listFile;

    private ImageView front;
    private ImageView left;
    private ImageView back;
    private ImageView background;
    private RadioGroup rg;
    private RadioButton rb;
    private Button sub;


    //private GridView gridView;
    //private GridViewAdapter gridAdapter;

    //Button b;
    //ImageView iv;

    String front_img;
    String left_img;
    String right_img;
    String back_img;


    private Bitmap b;
    private Bitmap b2;
    private Bitmap b3;
    private Bitmap b4;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_image);

        front = (ImageView)findViewById(R.id.iv1);
        left = (ImageView)findViewById(R.id.iv2);
        back = (ImageView)findViewById(R.id.iv3);
        background = (ImageView)findViewById(R.id.iv4);
        rg = (RadioGroup)findViewById(R.id.radioChoice);
        sub = (Button)findViewById(R.id.submit);

        //gridView = (GridView)findViewById(R.id.gridView);
        //gridAdapter = new GridViewAdapter(this,R.layout.activity_display_images_items,getData());
        //gridView.setAdapter(gridAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            verifyStoragePermissions(Display_Images.this);
        }


        String storageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d("path_che:",storageDir);
        String filename = "/Perfit";///IMG_20170331_105740.jpg";

        front_img = storageDir+filename+"/front.jpg";
        left_img = storageDir+filename+"/left.jpg";
        right_img = storageDir+filename+"/right.jpg";
        back_img = storageDir+filename+"/back.jpg";


        b = BitmapFactory.decodeFile(front_img);
        b2 = BitmapFactory.decodeFile(left_img);
        b3 = BitmapFactory.decodeFile(right_img);
        b4 = BitmapFactory.decodeFile(back_img);

        /*File f = new File(storageDir+filename);
        if(f.isDirectory()){
            listFile = f.listFiles();
        }*/

        //if(f.exists()){

           /* b = BitmapFactory.decodeFile(listFile[0].getAbsolutePath());
            b2 = BitmapFactory.decodeFile(listFile[1].getAbsolutePath());
            b3 = BitmapFactory.decodeFile(listFile[2].getAbsolutePath());
            b4 = BitmapFactory.decodeFile(listFile[3].getAbsolutePath());*/


        front.setImageBitmap(b);
        left.setImageBitmap(b2);
        back.setImageBitmap(b3);
        background.setImageBitmap(b4);






        /*}
        else{
            Log.e("File nathi madti","Mad ne");
        }*/

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int sel_Id = rg.getCheckedRadioButtonId();
                Log.d("Id:", String.valueOf(sel_Id));

                rb = (RadioButton)findViewById(sel_Id);
                Log.d("Text:",rb.getText().toString());
                final String url = "http://perfit.pwmky3qky6.us-west-2.elasticbeanstalk.com/getdefaultmodel";

                if(rb.getText().toString().equals("Yes")){
                    Log.d("Hanji:",rb.getText().toString());
                    Toast.makeText(Display_Images.this,"Congrats 3D Model is Created",Toast.LENGTH_LONG);
                    Intent i = new Intent(Display_Images.this,Congrats.class);
                    startActivity(i);
                    /*File file1 = new File(String.valueOf(b));
                    File file2 = new File(String.valueOf(b2));
                    //File file2 = new File(String.valueOf(b2));

                    try
                    {
                        HttpClient client = new DefaultHttpClient();
                        HttpPost post = new HttpPost(url);
                        FileBody bin1 = new FileBody(file1);
                        FileBody bin2 = new FileBody(file2);
                        //MultipartEntity reqEntity = new MultipartEntity();
                        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                        builder.addPart("file", bin1);
                        builder.addPart("file", bin2);

                        post.setEntity(builder.build());
                        HttpResponse response = client.execute(post);

                         Log.d("respo:", String.valueOf(response.getParams()));


                    }
                    catch (Exception ex){
                        Log.e("Debug", "error: " + ex.getMessage(), ex);
                    }*/

                    //uploadImage();

                    /*Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                b2.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                b3.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                b4.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                byte[] data = bos.toByteArray();
                                HttpClient httpClient = new DefaultHttpClient();
                                HttpPost postRequest = new HttpPost(url);
                                ByteArrayBody bab = new ByteArrayBody(data, "image");

                                // File file= new File("/mnt/sdcard/forest.png");
                                // FileBody bin = new FileBody(file);


                                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                                entityBuilder.addPart("file[]",bab);
                                //entityBuilder.setMimeSubtype("image/jpeg");

                                // add more key/value pairs here as needed
                                //HttpEntity entity = entityBuilder.build();

                                postRequest.setEntity(entityBuilder.build());

                                //String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGl0eSI6NTAsImlhdCI6MTQ5MzM2MDk1MywibmJmIjoxNDkzMzYwOTUzLCJleHAiOjE0OTQyMjQ5NTN9.i61wZeisQl3auBP_EIaE7-YRFnzqRZKgA9w-grWIf8c";
                                String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGl0eSI6NTIsImlhdCI6MTQ5MzQwOTQyMywibmJmIjoxNDkzNDA5NDIzLCJleHAiOjE0OTQyNzM0MjN9.Vzom938h8P13U264tSsT9TcH6hNpmuXeb-IhitGt8II";
                                //params.put("Content-Type", "multipart/form-data");
                                //params.put("Authorization","jwt "+token);

                                postRequest.setHeader("Content-Type", "multipart/form-data");
                                postRequest.setHeader("Authorization", "jwt "+token);




                                HttpResponse response = httpClient.execute(postRequest);
                                BufferedReader reader = new BufferedReader(new InputStreamReader(
                                        response.getEntity().getContent(), "UTF-8"));
                                String sResponse;
                                StringBuilder s = new StringBuilder();

                                while ((sResponse = reader.readLine()) != null) {
                                    s = s.append(sResponse);
                                }
                                Log.d("response:", String.valueOf(s));
                                //System.out.println("Response: " + s);
                            } catch (Exception e) {
                                // handle exception here
                                e.printStackTrace();
                                //Log.e(e.getClass().getName(), e.getMessage());
                            }
                        }
                    });
                    t.start();*/

















                    VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            //Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_SHORT).show();
                            int len = response.data.length;
                            Log.d("len:", String.valueOf(len));
                            Log.d("resp:", new String(response.data));

                            //Intent i = new Intent(Display_Images.this,ProductActivity.class);
                            //startActivity(i);

                            //response.toString()


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Nathi thatu", Toast.LENGTH_SHORT).show();
                            Log.i("Error", error.toString());
                            error.printStackTrace();
                        }
                    }) {
//                        @Override
//                        protected Map<String, String> getParams() {
//                            Map<String, String> params = new HashMap<>();
//                            params.put("api_token", "gh659gjhvdyudo973823tt9gvjf7i6ric75r76");
//                            return params;
//                        }

                        @Override
                        protected Map<String, DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            // file name could found file base or direct access from real path
                            // for now just get bitmap data from ImageView
                            DataPart d = new DataPart("front.jpg", bitmapToByteArray(b), "image/jpeg");
                            Log.d("datapart length=", String.valueOf(d.getContent().length));
                            params.put("file[]", new DataPart("front.jpg", bitmapToByteArray(b), "image/jpeg"));
                            //params.put("file[]", new DataPart("left.jpg", bitmapToByteArray(b2),  "image/jpeg"));
                            //params.put("file[]", new DataPart("back.jpg", bitmapToByteArray(b3),  "image/jpeg"));
                            //params.put("file[]", new DataPart("background.jpg", bitmapToByteArray(b4), "image/jpeg"));

                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String>  params = new HashMap<String, String>();
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            //Log.d("token", prefs.getString("token", "error"));
                            //String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGl0eSI6NDMsImlhdCI6MTQ5MzI4MzIxNSwibmJmIjoxNDkzMjgzMjE1LCJleHAiOjE0OTQxNDcyMTV9.OkCO4WdrXDBStJXD0GjWTVKboXRmVsO2oaxjsh1pK9E";
                            //String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGl0eSI6NDgsImlhdCI6MTQ5MzM1OTMzNywibmJmIjoxNDkzMzU5MzM3LCJleHAiOjE0OTQyMjMzMzd9.BOjd1deRuGfUxy_jHCY5iv8IDr27JF_oZ0X6UvBZieE";
                            //String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGl0eSI6NDksImlhdCI6MTQ5MzM1OTY3OCwibmJmIjoxNDkzMzU5Njc4LCJleHAiOjE0OTQyMjM2Nzh9.cmOQ0oCut0clDaz_AXgApNiYQpfQ0damGOEriqNPVOk";
                            String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGl0eSI6NTAsImlhdCI6MTQ5MzM2MDk1MywibmJmIjoxNDkzMzYwOTUzLCJleHAiOjE0OTQyMjQ5NTN9.i61wZeisQl3auBP_EIaE7-YRFnzqRZKgA9w-grWIf8c";
                            params.put("Content-Type", "multipart/form-data");
                            params.put("Authorization","jwt "+token);
                            return params;

                        }
                    };

                    //MyVolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);


                }
                else{
                    Log.d("Naji:",rb.getText().toString());
                    Toast.makeText(getApplicationContext(), "Please start the process again", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Display_Images.this,OrientationActivity.class);
                    startActivity(i);

                }


            }
        });







    }

    private void uploadImage() {

        /**
         * Progressbar to Display if you need
         */

        //Create Upload Server Client
        ApiService service = RetroClient.getApiService();

        //File creating from selected URL
        File file = new File(front_img);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        Call<Result> resultCall = service.uploadImage(body);

        resultCall.enqueue(new Callback<Result>() {




            @Override
            public void onResponse(Call<Result> call, retrofit2.Response<Result> response) {
                if (response.isSuccessful()) {

                    Log.d("response:", String.valueOf(response.body()));
                    Log.d("response_len:", String.valueOf(String.valueOf(response.body()).length()));
                    //if (response.body().getResult.equals("success"))
                        /*Snackbar.make(parentView, R.string.string_upload_success, Snackbar.LENGTH_LONG).show();
                    else
                        Snackbar.make(parentView, R.string.string_upload_fail, Snackbar.LENGTH_LONG).show();*/

                } else {
                    //Snackbar.make(parentView, R.string.string_upload_fail, Snackbar.LENGTH_LONG).show();
                    Log.d("fail_response:", "fail_response");
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

                Log.d("fail:", "fail");
                //Log.d("response_len:", String.valueOf(String.valueOf(response.body()).length()));

            }
        });
    }



    public byte[] bitmapToByteArray(Bitmap b)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    private ArrayList<ImageItem> getData()
    {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();

        String storageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String filename = "/DCIM/Camera";///IMG_20170331_105740.jpg";

        File f = new File(storageDir+filename);
        if(f.isDirectory()){
            listFile = f.listFiles();
        }

        if(f.exists()){

            for (int i = 0; i < 4; i++) {
                Bitmap bitmap = BitmapFactory.decodeFile(listFile[i].getAbsolutePath());
                imageItems.add(new ImageItem(bitmap));
            }

        }
        else{
            Log.e("File nathi madti","Mad ne");
        }


        return imageItems;
    }


    public  void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
