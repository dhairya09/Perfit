package com.example.dhairyapujara.perfit_submit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by srinidhikarthikbs on 3/10/17.
 */

//referenced from https://github.com/josnidhin/Android-Camera-Example

public class CaptureActivity extends Activity{
    private static final String TAG = "CamTestActivity";
    Preview preview;
    Button buttonClick;
    Camera camera;
    Activity act;
    Context ctx;
    private CameraView mCameraView = null;
    LinearLayout camera_view=null;
    int frame_width=0, frame_height=0;
    Sensor accelerometer;
    Sensor magnetometer;
    Sensor mRotationSensor;
    DeviceOrientation deviceOrientation;
    SensorManager mSensorManager=null;
    TextToSpeech t1;
    boolean t1Initialized = false;
    Handler handler=null;
    Runnable runnable = null;
    TextView countdown = null;
    Handler cd_handler = null;
    Runnable cd_runnable = null;
    boolean[] pictures = {false, false, false, false, false};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        act = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        countdown = (TextView) findViewById(R.id.countdown);

        setContentView(R.layout.capture_activity);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    t1Initialized=true;
                }
            }
        });

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        deviceOrientation = DeviceOrientation.getInstance();

        mSensorManager.registerListener(deviceOrientation.getEventListener(), accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(deviceOrientation.getEventListener(), magnetometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(deviceOrientation.getEventListener(), mRotationSensor, SensorManager.SENSOR_DELAY_UI);

    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d("Camera", "Front Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    @Override
    protected void onResume() {

//        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if(status != TextToSpeech.ERROR) {
//                    t1.setLanguage(Locale.getDefault());
//                    t1Initialized=true;
//                }
//            }
//        });

        mSensorManager.registerListener(deviceOrientation.getEventListener(), accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(deviceOrientation.getEventListener(), magnetometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(deviceOrientation.getEventListener(), mRotationSensor, SensorManager.SENSOR_DELAY_UI);

        this.handler = new Handler();
        this.runnable = new Runnable() {
            @Override
            public void run() {
                float pitchValue = deviceOrientation.getPitch();
                if(pitchValue < -75.0 && pitchValue > -85.0){
                    handler.postDelayed(this, 1000);
                }
                else{
                    handler.removeCallbacks(this);
                    //cleanup();
                    finish();
                    startActivity(new Intent(getApplicationContext(), OrientationActivity.class));
                }
            }
        };
        //handler.postDelayed(runnable, 1000);



        camera_view = (LinearLayout) findViewById(R.id.camera_preview);
        frame_width = this.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        frame_height = this.getWindow().getWindowManager().getDefaultDisplay().getHeight();
        if (Build.VERSION.SDK_INT >= 23)
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                doWork();
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        else
            doWork();
        super.onResume();
    }

    public void checkOrientation(){
        float pitchValue = deviceOrientation.getPitch();
        if(pitchValue < -75.0 && pitchValue > -85.0){
            //handler.postDelayed(this, 1000);
        }
        else{
            //handler.removeCallbacks(this);
            //cleanup();
            finish();
            startActivity(new Intent(getApplicationContext(), OrientationActivity.class));
        }
    }

    public void take2ndPicture(){
        t1.speak("The background has been captured", TextToSpeech.QUEUE_ADD, null);
    }

    public void newMethod(){
        countdown(0);
//        t1.speak("Please place your phone at a stable angle to the wall", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Please step aside so that the background is captured", TextToSpeech.QUEUE_ADD, null); //a
//        t1.speak("The background will be captured in 3 seconds", TextToSpeech.QUEUE_ADD, null);
//        while (t1.isSpeaking());
//        countdown(0);
//        while(!pictures[0]);
//        t1.speak("The background has been captured", TextToSpeech.QUEUE_ADD, null);  //b
//        t1.speak("Please step into the camera view and position yourself so that you are visible fully", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Your front view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
//        while (t1.isSpeaking());
//        countdown(1);
//        while(!pictures[1]);
//        t1.speak("Your front view has been captured", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Please turn slowly to your left", TextToSpeech.QUEUE_ADD, null);    //c
//        t1.speak("Your right view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
//        while (t1.isSpeaking());
//        countdown(2);
//        while(!pictures[2]);
//        t1.speak("Your right view has been captured", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Please turn slowly to your left", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Your back view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
//        while (t1.isSpeaking());
//        countdown(3);
//        while(!pictures[3]);
//        t1.speak("Your back view has been captured", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Please turn slowly to your left", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Your left view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
//        while (t1.isSpeaking());
//        countdown(4);
//        while(!pictures[4]);
//        t1.speak("Your left view has been captured", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("All your images have been captured", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Please proceed with confirming your images", TextToSpeech.QUEUE_ADD, null);
//        while (t1.isSpeaking());
//        //OrientationActivity.orientation.finish();
//        //cleanup();
//        //mSensorManager.unregisterListener(deviceOrientation.getEventListener());
//        finish();
//        startActivity(new Intent(getApplicationContext(), nextscreen.class));
    }

    public void doWork(){
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) camera_view.getLayoutParams();
        params.width=frame_width;
        params.height=frame_height;

        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(this, "No front facing camera found.",
                        Toast.LENGTH_LONG).show();
            } else {
                mCameraView = new CameraView(this, camera_view, cameraId, frame_width, frame_height);//create a SurfaceView to show camera data
                camera_view.addView(mCameraView);//add the SurfaceView to the layout
//                camera_view.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mCameraView.takePicture();
//                    }
//                });


                //while(camera_view.getChildCount()<=0);
                //add the other parts

                //PENDING - to destroy this and Orientation activity if the user confirms the image on next activity
                //OTHERWISE - come back and redo the process

                //while(!t1Initialized);

                Handler backgroundImageCaptureHandler = new Handler();
                Runnable backgroundImageCapture = new Runnable() {
                    @Override
                    public void run() {

                        t1.speak("Please place your phone at a stable angle to the wall", TextToSpeech.QUEUE_ADD, null);
                        t1.speak("Please step aside so that the background is captured", TextToSpeech.QUEUE_ADD, null);
                        t1.speak("The background will be captured in 3 seconds", TextToSpeech.QUEUE_ADD, null);
                        while(t1.isSpeaking());

                        Handler oneHandler = new Handler();
                        Runnable one = new Runnable() {
                            @Override
                            public void run() {
                                //countdown.setText("1");
                                t1.speak("One", TextToSpeech.QUEUE_ADD, null);

                                while(t1.isSpeaking());
                            }
                        };
                        oneHandler.postDelayed(one, 1000);

                        Handler twoHandler = new Handler();
                        Runnable two = new Runnable() {
                            @Override
                            public void run() {
                                //countdown.setText("2");
                                t1.speak("Two", TextToSpeech.QUEUE_ADD, null);

                                while(t1.isSpeaking());
                            }
                        };
                        twoHandler.postDelayed(two, 2000);

                        final Handler threeHandler = new Handler();
                        Runnable three = new Runnable() {
                            @Override
                            public void run() {
                                while(t1.isSpeaking());
                                //countdown.setText("3");
                                t1.speak("Three", TextToSpeech.QUEUE_ADD, null);

                                while(t1.isSpeaking());
                                while (mCameraView==null);
                                if(mCameraView!=null) mCameraView.takePicture();
                                else{
                                    //threeHandler.removeCallbacks(this);
                                    cleanup();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), OrientationActivity.class));
                                }

                                t1.speak("The background has been captured", TextToSpeech.QUEUE_ADD, null);
                                t1.speak("Please step into the camera view and position yourself so that you are visible fully", TextToSpeech.QUEUE_ADD, null);
                                t1.speak("Your front view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
                                while(t1.isSpeaking());

                                Handler oneHandler = new Handler();
                                Runnable one = new Runnable() {
                                    @Override
                                    public void run() {
                                        //countdown.setText("1");
                                        t1.speak("One", TextToSpeech.QUEUE_ADD, null);
                                        while(t1.isSpeaking());
                                    }
                                };
                                oneHandler.postDelayed(one, 1000);

                                Handler twoHandler = new Handler();
                                Runnable two = new Runnable() {
                                    @Override
                                    public void run() {
                                        //countdown.setText("2");
                                        t1.speak("Two", TextToSpeech.QUEUE_ADD, null);
                                        while(t1.isSpeaking());
                                    }
                                };
                                twoHandler.postDelayed(two, 2000);

                                final Handler threeHandler = new Handler();
                                Runnable three = new Runnable() {
                                    @Override
                                    public void run() {
                                        while(t1.isSpeaking());
                                        //countdown.setText("3");
                                        t1.speak("Three", TextToSpeech.QUEUE_ADD, null);
                                        while(t1.isSpeaking());
                                        while (mCameraView==null);
                                        if(mCameraView!=null) mCameraView.takePicture();
                                        else{
                                            //threeHandler.removeCallbacks(this);
                                            cleanup();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), OrientationActivity.class));
                                        }

                                        t1.speak("Your front view has been captured", TextToSpeech.QUEUE_ADD, null);
                                        t1.speak("Please turn slowly to your left", TextToSpeech.QUEUE_ADD, null);
                                        t1.speak("Your right view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
                                        while(t1.isSpeaking());

                                        Handler oneHandler = new Handler();
                                        Runnable one = new Runnable() {
                                            @Override
                                            public void run() {
                                                //countdown.setText("1");
                                                t1.speak("One", TextToSpeech.QUEUE_ADD, null);
                                                while(t1.isSpeaking());
                                            }
                                        };
                                        oneHandler.postDelayed(one, 1000);

                                        Handler twoHandler = new Handler();
                                        Runnable two = new Runnable() {
                                            @Override
                                            public void run() {
                                                //countdown.setText("2");
                                                t1.speak("Two", TextToSpeech.QUEUE_ADD, null);
                                                while(t1.isSpeaking());
                                            }
                                        };
                                        twoHandler.postDelayed(two, 2000);

                                        final Handler threeHandler = new Handler();
                                        Runnable three = new Runnable() {
                                            @Override
                                            public void run() {
                                                while(t1.isSpeaking());
                                                //countdown.setText("3");
                                                t1.speak("Three", TextToSpeech.QUEUE_ADD, null);
                                                while(t1.isSpeaking());
                                                while (mCameraView==null);
                                                if(mCameraView!=null) mCameraView.takePicture();
                                                else{
                                                    //threeHandler.removeCallbacks(this);
                                                    cleanup();
                                                    finish();
                                                    startActivity(new Intent(getApplicationContext(), OrientationActivity.class));
                                                }

                                                t1.speak("Your right view has been captured", TextToSpeech.QUEUE_ADD, null);
                                                t1.speak("Please turn slowly to your left", TextToSpeech.QUEUE_ADD, null);
                                                t1.speak("Your back view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
                                                while(t1.isSpeaking());

                                                Handler oneHandler = new Handler();
                                                Runnable one = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //countdown.setText("1");
                                                        t1.speak("One", TextToSpeech.QUEUE_ADD, null);
                                                        while(t1.isSpeaking());
                                                    }
                                                };
                                                oneHandler.postDelayed(one, 1000);

                                                Handler twoHandler = new Handler();
                                                Runnable two = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //countdown.setText("2");
                                                        t1.speak("Two", TextToSpeech.QUEUE_ADD, null);
                                                        while(t1.isSpeaking());
                                                    }
                                                };
                                                twoHandler.postDelayed(two, 2000);

                                                final Handler threeHandler = new Handler();
                                                Runnable three = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        while(t1.isSpeaking());
                                                        //countdown.setText("3");
                                                        t1.speak("Three", TextToSpeech.QUEUE_ADD, null);
                                                        while(t1.isSpeaking());
                                                        while (mCameraView==null);
                                                        if(mCameraView!=null) mCameraView.takePicture();
                                                        else{
                                                            //threeHandler.removeCallbacks(this);
                                                            cleanup();
                                                            finish();
                                                            startActivity(new Intent(getApplicationContext(), OrientationActivity.class));
                                                        }

                                                        t1.speak("Your back view has been captured", TextToSpeech.QUEUE_ADD, null);
                                                        t1.speak("Please turn slowly to your left", TextToSpeech.QUEUE_ADD, null);
                                                        t1.speak("Your left view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
                                                        while(t1.isSpeaking());

                                                        Handler oneHandler = new Handler();
                                                        Runnable one = new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                //countdown.setText("1");
                                                                t1.speak("One", TextToSpeech.QUEUE_ADD, null);
                                                                while(t1.isSpeaking());
                                                            }
                                                        };
                                                        oneHandler.postDelayed(one, 1000);

                                                        Handler twoHandler = new Handler();
                                                        Runnable two = new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                //countdown.setText("2");
                                                                t1.speak("Two", TextToSpeech.QUEUE_ADD, null);
                                                                while(t1.isSpeaking());
                                                            }
                                                        };
                                                        twoHandler.postDelayed(two, 2000);

                                                        final Handler threeHandler = new Handler();
                                                        Runnable three = new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                while(t1.isSpeaking());
                                                                //countdown.setText("3");
                                                                t1.speak("Three", TextToSpeech.QUEUE_ADD, null);
                                                                while(t1.isSpeaking());
                                                                while (mCameraView==null);
                                                                if(mCameraView!=null) mCameraView.takePicture();
                                                                else{
                                                                    //threeHandler.removeCallbacks(this);
                                                                    cleanup();
                                                                    finish();
                                                                    startActivity(new Intent(getApplicationContext(), OrientationActivity.class));
                                                                }

                                                                t1.speak("Your left view has been captured", TextToSpeech.QUEUE_ADD, null);
                                                                t1.speak("All your images have been captured", TextToSpeech.QUEUE_ADD, null);
                                                                t1.speak("Please proceed with confirming your images", TextToSpeech.QUEUE_ADD, null);
                                                                while(t1.isSpeaking());

                                                                handler.removeCallbacks(runnable);
                                                                //OrientationActivity.orientation.finish();
                                                                //cleanup();
                                                                //mSensorManager.unregisterListener(deviceOrientation.getEventListener());
                                                                finish();
                                                                startActivity(new Intent(getApplicationContext(), Display_Images.class));
                                                            }
                                                        };
                                                        threeHandler.postDelayed(three, 3000);
                                                    }
                                                };
                                                threeHandler.postDelayed(three, 3000);
                                            }
                                        };
                                        threeHandler.postDelayed(three, 3000);
                                    }
                                };
                                threeHandler.postDelayed(three, 3000);
                            }
                        };
                        threeHandler.postDelayed(three, 3000);
                    }
                };
                //backgroundImageCaptureHandler.postDelayed(backgroundImageCapture, 2000);

                //try new method
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        newMethod();
//                    }
//                }, 3000);


                //invoke tts new method after thread?
                //the idea is to control the finishing of activity by myself
            }
        }
    }

    @Override
    protected void onPause() {
        //camera_view.removeAllViews();
        System.out.print("came to cleanup");
//        mCameraView=null;
//        mSensorManager.unregisterListener(deviceOrientation.getEventListener());
//        if(t1 !=null){
//            t1.stop();
//            t1.shutdown();
//        }
        super.onPause();
    }

    public void cleanup() {
        //camera_view.removeAllViews();
        System.out.print("came to cleanup");
        //mCameraView=null;
        //mSensorManager.unregisterListener(deviceOrientation.getEventListener());
//        if(t1 !=null){
//            t1.stop();
//            t1.shutdown();
//        }
    }

    //pending- yet to handle the case where user is interrupted while selecting the permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("Permission: ",permissions[0]+ "was "+grantResults[0]);
        }
    }

    @Override
    public void onBackPressed() {
        //finish();
    }

    public void countdown(final int picture_num){
//        t1.speak("Please place your phone at a stable angle to the wall", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Please step aside so that the background is captured", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("The background will be captured in 3 seconds", TextToSpeech.QUEUE_ADD, null);
//        while (t1.isSpeaking());
//        t1.speak("The background has been captured", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Please step into the camera view and position yourself so that you are visible fully", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Your front view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
//        while (t1.isSpeaking());
//        t1.speak("Your front view has been captured", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Please turn slowly to your left", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Your right view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
//        while (t1.isSpeaking());
//        t1.speak("Your right view has been captured", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Please turn slowly to your left", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Your back view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
//        while (t1.isSpeaking());
//        t1.speak("Your back view has been captured", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Please turn slowly to your left", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Your left view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
//        while (t1.isSpeaking());
//        t1.speak("Your left view has been captured", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("All your images have been captured", TextToSpeech.QUEUE_ADD, null);
//        t1.speak("Please proceed with confirming your images", TextToSpeech.QUEUE_ADD, null);
//        while (t1.isSpeaking());

                t1.speak("Please place your phone at a stable angle to the wall", TextToSpeech.QUEUE_ADD, null);
        t1.speak("Please step aside so that the background is captured", TextToSpeech.QUEUE_ADD, null);
        t1.speak("The background will be captured in 3 seconds", TextToSpeech.QUEUE_ADD, null);
        while (t1.isSpeaking());

        CountDownTimer cdt = new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {

                //try disabling the whole voice thing first, move it to new tts activity, then try old method, then try new method

                t1.speak(String.valueOf(millisUntilFinished/1000),TextToSpeech.QUEUE_ADD, null);
                while(t1.isSpeaking());
            }

            public void onFinish() {
                mCameraView.takePicture();
                //pictures[picture_num] = true;
                //picture_num++;

                        t1.speak("The background has been captured", TextToSpeech.QUEUE_ADD, null);
        t1.speak("Please step into the camera view and position yourself so that you are visible fully", TextToSpeech.QUEUE_ADD, null);
        t1.speak("Your front view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
        while (t1.isSpeaking());

                //next picture

                CountDownTimer cdt = new CountDownTimer(4000, 1000) {

                    public void onTick(long millisUntilFinished) {

                        //try disabling the whole voice thing first, move it to new tts activity, then try old method, then try new method

                        t1.speak(String.valueOf(millisUntilFinished/1000),TextToSpeech.QUEUE_ADD, null);
                        while(t1.isSpeaking());
                    }

                    public void onFinish() {
                        mCameraView.takePicture();
                        //pictures[picture_num] = true;
                        //picture_num++;

                        //next picture

                                t1.speak("Your front view has been captured", TextToSpeech.QUEUE_ADD, null);
        t1.speak("Please turn slowly to your left", TextToSpeech.QUEUE_ADD, null);
        t1.speak("Your right view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
        while (t1.isSpeaking());

                        CountDownTimer cdt = new CountDownTimer(4000, 1000) {

                            public void onTick(long millisUntilFinished) {

                                //try disabling the whole voice thing first, move it to new tts activity, then try old method, then try new method

                                t1.speak(String.valueOf(millisUntilFinished/1000),TextToSpeech.QUEUE_ADD, null);
                                while(t1.isSpeaking());
                            }

                            public void onFinish() {
                                mCameraView.takePicture();
                                //pictures[picture_num] = true;
                                //picture_num++;

                                //next picture

                                t1.speak("Your right view has been captured", TextToSpeech.QUEUE_ADD, null);
                                t1.speak("Please turn slowly to your left", TextToSpeech.QUEUE_ADD, null);
                                t1.speak("Your back view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
                                while (t1.isSpeaking());

                                CountDownTimer cdt = new CountDownTimer(4000, 1000) {

                                    public void onTick(long millisUntilFinished) {

                                        //try disabling the whole voice thing first, move it to new tts activity, then try old method, then try new method

                                        t1.speak(String.valueOf(millisUntilFinished/1000),TextToSpeech.QUEUE_ADD, null);
                                        while(t1.isSpeaking());
                                    }

                                    public void onFinish() {
                                        mCameraView.takePicture();
                                        //pictures[picture_num] = true;
                                        //picture_num++;

                                        //next picture

                                        t1.speak("Your back view has been captured", TextToSpeech.QUEUE_ADD, null);
                                        t1.speak("Please turn slowly to your left", TextToSpeech.QUEUE_ADD, null);
                                        t1.speak("Your left view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, null);
                                        while (t1.isSpeaking());


                                        CountDownTimer cdt = new CountDownTimer(4000, 1000) {

                                            public void onTick(long millisUntilFinished) {

                                                //try disabling the whole voice thing first, move it to new tts activity, then try old method, then try new method

                                                t1.speak(String.valueOf(millisUntilFinished/1000),TextToSpeech.QUEUE_ADD, null);
                                                while(t1.isSpeaking());
                                            }

                                            public void onFinish() {
                                                mCameraView.takePicture();
                                                //pictures[picture_num] = true;
                                                //picture_num++;

                                                //next picture

                                                t1.speak("Your left view has been captured", TextToSpeech.QUEUE_ADD, null);
                                                t1.speak("All your images have been captured", TextToSpeech.QUEUE_ADD, null);
                                                t1.speak("Please proceed with confirming your images", TextToSpeech.QUEUE_ADD, null);
                                                while (t1.isSpeaking());


                                            }
                                        };
                                        cdt.start();

                                    }
                                };
                                cdt.start();

                            }
                        };
                        cdt.start();


                    }
                };
                //cdt.start();


            }
        };
        cdt.start();
    }

    public void uploadImages(){
        String url = "http://www.angga-ari.com/api/something/awesome";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    String status = result.getString("status");
                    String message = result.getString("message");

                    if (status.equals("200")) {
                        // tell everybody you have succed upload image and post strings
                        Log.i("Messsage", message);
                    } else {
                        Log.i("Unexpected", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message+" Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message+ " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message+" Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //params.put("api_token", "gh659gjhvdyudo973823tt9gvjf7i6ric75r76");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView

                //params.put("avatar", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mAvatarImage.getDrawable()), "image/jpeg"));

                return params;
            }
        };

        MyVolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }
}
