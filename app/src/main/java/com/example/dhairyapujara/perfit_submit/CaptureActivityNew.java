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

public class CaptureActivityNew extends Activity implements TextToSpeech.OnInitListener {
    private static final String TAG = "CamTestActivity";
    Preview preview;
    Button buttonClick;
    Camera camera;
    Activity act;
    Context ctx;
    int result;
    private CameraView mCameraView = null;
    LinearLayout camera_view=null;
    int frame_width=0, frame_height=0;
    Sensor accelerometer;
    Sensor magnetometer;
    Sensor mRotationSensor;
    DeviceOrientation deviceOrientation;
    SensorManager mSensorManager=null;
    TextToSpeech t1;
    TextToSpeech tts;
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


        tts = new TextToSpeech(this, this);

//        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if(status != TextToSpeech.ERROR) {
//                    t1.setLanguage(Locale.UK);
//                    t1Initialized=true;
//                }
//            }
//        });

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        deviceOrientation = DeviceOrientation.getInstance();

        mSensorManager.registerListener(deviceOrientation.getEventListener(), accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(deviceOrientation.getEventListener(), magnetometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(deviceOrientation.getEventListener(), mRotationSensor, SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            result = tts.setLanguage(Locale.getDefault());
            //tts.setPitch(1f);
            //tts.setSpeechRate(1f);
            TextToSpeech.OnUtteranceCompletedListener listener=new TextToSpeech.OnUtteranceCompletedListener(){
                @Override
                public void onUtteranceCompleted(final String utteranceId) {
                    checkOrientation();


                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            HashMap<String, String> myHash = new HashMap<String, String>();
                            //Toast.makeText(getApplicationContext(), "came here", Toast.LENGTH_SHORT).show();
                            switch (utteranceId){
                                case "start":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"a1");
                                    tts.speak("Please place your phone at a stable angle to the wall", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "a1":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"a2");
                                    tts.speak("Please step aside so that the background is captured", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "a2":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"a3");
                                    tts.speak("The background will be captured in 3 seconds", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "a3":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"act1");
                                    tts.speak("One..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "act1":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"act2");
                                    tts.speak("Two..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "act2":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"act3");
                                    tts.speak("Three..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "act3":
                                    //do camera click
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"background_click");
                                    mCameraView.takePicture();
                                    tts.speak("..", TextToSpeech.QUEUE_ADD, myHash);break;



                                case "background_click":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"b1");
                                    tts.speak("The background has been captured", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "b1":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"b2");
                                    tts.speak("Please step into the camera view and position yourself so that you are visible fully", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "b2":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"b3");
                                    tts.speak("Your front view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "b3":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"bct1");
                                    tts.speak("One..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "bct1":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"bct2");
                                    tts.speak("Two..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "bct2":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"bct3");
                                    tts.speak("Three..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "bct3":
                                    //do camera click
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"front_click");
                                    mCameraView.takePicture();
                                    tts.speak("..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "front_click":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"c1");
                                    tts.speak("your front view has been captured", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "c1":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"c2");
                                    tts.speak("Please turn slowly to your left", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "c2":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"c3");
                                    tts.speak("Your right view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "c3":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"cct1");
                                    tts.speak("One..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "cct1":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"cct2");
                                    tts.speak("Two..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "cct2":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"cct3");
                                    tts.speak("Three..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "cct3":
                                    //do camera click
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"right_click");
                                    mCameraView.takePicture();
                                    tts.speak("..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "right_click":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"d1");
                                    tts.speak("Your right view has been captured", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "d1":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"d2");
                                    tts.speak("Please turn slowly to your left", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "d2":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"d3");
                                    tts.speak("Your back view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "d3":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"dct1");
                                    tts.speak("One..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "dct1":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"dct2");
                                    tts.speak("Two..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "dct2":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"dct3");
                                    tts.speak("Three..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "dct3":
                                    //do camera click
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"back_click");
                                    mCameraView.takePicture();
                                    tts.speak("..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "back_click":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"e1");
                                    tts.speak("Your back view has been captured", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "e1":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"e2");
                                    tts.speak("Please turn slowly to your left", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "e2":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"e3");
                                    tts.speak("Your left view image will be taken in 3 seconds", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "e3":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"ect1");
                                    tts.speak("One..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "ect1":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"ect2");
                                    tts.speak("Two..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "ect2":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"ect3");
                                    tts.speak("Three..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "ect3":
                                    //do camera click
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"left_click");
                                    mCameraView.takePicture();
                                    tts.speak("..", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "left_click":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"f1");
                                    tts.speak("Your left view has been captured", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "f1":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"f2");
                                    tts.speak("All your images have been captured", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "f2":
                                    myHash.clear();
                                    myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"f3");
                                    tts.speak("Please proceed with confirming your images", TextToSpeech.QUEUE_ADD, myHash);break;

                                case "f3":
                                    //myHash.clear();
                                    //myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"ect1");
                                    tts.speak("Done!!.", TextToSpeech.QUEUE_ADD, null);
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), Display_Images.class));
                                    break;

                                default: Toast.makeText(getApplicationContext(), "default", Toast.LENGTH_SHORT).show();break;
                            }
                        }
                    });

                }
            };
            tts.setOnUtteranceCompletedListener(listener);
            HashMap myHash = new HashMap<String, String>();
            myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"start");
            tts.speak("Get ready to begin!", TextToSpeech.QUEUE_ADD, myHash);
        } else
            Log.e("TTS", "Init failed");

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
}
