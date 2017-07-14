package com.example.dhairyapujara.perfit_submit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.hardware.Camera.*;

/**
 * Created by srinidhikarthikbs on 2/9/17.
 */

public class OrientationActivity extends AppCompatActivity{
    public static Activity orientation = null;
    TextView pitch=null, yaw=null, roll=null;
    Button proceed=null;
    Sensor accelerometer;
    Sensor magnetometer;
    Sensor mRotationSensor;
    DeviceOrientation deviceOrientation;
    SensorManager mSensorManager=null;
    private CameraView mCameraView = null;
    private static final int SENSOR_DELAY = 500 * 1000; // 500ms
    LinearLayout camera_view=null;
    int frame_width=0, frame_height=0;
    TableRow hr_green = null, hr_red = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientation_activity);
        orientation = this.getParent();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);

        hr_green = (TableRow) findViewById(R.id.hr_green);
        hr_red = (TableRow) findViewById(R.id.hr_red);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        deviceOrientation = DeviceOrientation.getInstance();

        mSensorManager.registerListener(deviceOrientation.getEventListener(), accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(deviceOrientation.getEventListener(), magnetometer, SensorManager.SENSOR_DELAY_UI);
        //mSensorManager.registerListener(deviceOrientation.getEventListener(), mRotationSensor, SensorManager.SENSOR_DELAY_UI);

        pitch=(TextView) findViewById(R.id.pitch);
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                Log.d("Camera", "Front Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }
    //pending - to prevent from crashing while coming back, by doing proper things in lifecycle functions
    @Override
    protected void onResume() {

        mSensorManager.registerListener(deviceOrientation.getEventListener(), accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(deviceOrientation.getEventListener(), magnetometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(deviceOrientation.getEventListener(), mRotationSensor, SensorManager.SENSOR_DELAY_UI);

        camera_view = (LinearLayout) findViewById(R.id.camera_preview);
        frame_width = (int) (this.getWindow().getWindowManager().getDefaultDisplay().getWidth()*0.8);
        frame_height = (int)((double)(this.getWindow().getWindowManager().getDefaultDisplay().getHeight()/this.getWindow().getWindowManager().getDefaultDisplay().getWidth())*frame_width);
        //Toast.makeText(getApplicationContext(), "screenw="+this.getWindow().getWindowManager().getDefaultDisplay().getWidth()+" screenh="+this.getWindow().getWindowManager().getDefaultDisplay().getHeight(), Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= 23)
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                doWork();
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        else
            doWork();

        super.onResume();
    }

    public void doWork(){
        //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) camera_view.getLayoutParams();
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) camera_view.getLayoutParams();
        params.width=frame_width;
        params.height=frame_width;
        camera_view.setLayoutParams(params);

        // do we have a camera?
        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {
            int cameraId = findFrontFacingCamera();
            //Toast.makeText(getApplicationContext(), String.valueOf(cameraId), Toast.LENGTH_SHORT).show();
            if (cameraId < 0) {
                Toast.makeText(this, "No front facing camera found.",
                        Toast.LENGTH_LONG).show();
            } else {
                mCameraView = new CameraView(this, camera_view, cameraId, frame_width, frame_height);//create a SurfaceView to show camera data
                camera_view.addView(mCameraView);//add the SurfaceView to the layout

                final float[] pitchValueLine = {deviceOrientation.getPitch()};
                final FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) hr_red.getLayoutParams();
                params1.setMargins(0, params.height/2, 0, 0);
                FrameLayout.LayoutParams green_params = (FrameLayout.LayoutParams) hr_green.getLayoutParams();
                green_params.height = (int)(10*params.height/180.0);
                green_params.setMargins(0, (int)(5*params.height/180.0), 0, 0);
                hr_green.setLayoutParams(green_params);
                final Handler line_handler = new Handler();
                final Runnable line_runnable = new Runnable() {
                    @Override
                    public void run() {
                        pitchValueLine[0] = deviceOrientation.getPitch();
                        if(pitchValueLine[0] < 0.0){
                            params1.setMargins(0, (int)(params.height/2 - Math.abs(pitchValueLine[0])*params.height/180.0), 0, 0);
                        }
                        if(pitchValueLine[0] >= 0.0){
                            params1.setMargins(0, (int)(params.height/2 + Math.abs(pitchValueLine[0])*params.height/180.0), 0, 0);
                        }
                        hr_red.setLayoutParams(params1);

                        line_handler.postDelayed(this, 100);
                    }
                };
                line_handler.postDelayed(line_runnable, 500);

                final float[] pitchValue = {deviceOrientation.getPitch()};
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        pitchValue[0] = deviceOrientation.getPitch();
                        //set the red line position
                        pitch.setText(String.valueOf("Angle: ")+String.valueOf(Math.abs(pitchValue[0])));//pending - display absolute value
                        if(pitchValue[0] < -75.0 && pitchValue[0] > -85.0){
                            handler.removeCallbacks(this);
                            line_handler.removeCallbacks(line_runnable);
                            //Toast.makeText(getApplicationContext(), "Angle: "+Math.abs(pitchValue), Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), CaptureActivityNew.class));

                        }
                        else
                            handler.postDelayed(this, 500);
                    }
                };
                handler.postDelayed(runnable,1000);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //finish();
        //super.onBackPressed();
    }

    @Override
    protected void onPause() {
        //mSensorManager.unregisterListener(deviceOrientation.getEventListener());
        //camera_view.removeAllViews();
        //mCameraView=null;
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //referenced from http://stackoverflow.com/questions/33162152/storage-permission-error-in-marshmallow?noredirect=1&lq=1

    public boolean isPermissionsGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permission "," granted");
                //Toast.makeText(getApplicationContext(), "All permissions granted", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Log.v("Permission "," revoked");
                //Toast.makeText(getApplicationContext(), "All permissions revoked", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permission "," granted");
            return true;
        }
    }
    //pending- yet to handle the case where user is interrupted while selecting the permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("Permission: ",permissions[0]+ "was "+grantResults[0]);
            //Toast.makeText(getApplicationContext(), "Permission: "+permissions[0]+ "was "+grantResults[0], Toast.LENGTH_SHORT).show();
            //resume tasks needing this permission
        }
    }
}
