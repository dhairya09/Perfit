package com.example.dhairyapujara.perfit_submit;

import android.content.Context;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by srinidhikarthikbs on 2/10/17.
 */
//reference taken from https://github.com/rexstjohn/UltimateAndroidCameraGuide/blob/master/camera/src/main/java/com/ultimate/camera/fragments/NativeCameraFragment.java
//reference taken from http://blog.rhesoft.com/2015/04/02/tutorial-how-to-use-camera-with-android-and-android-studio/
public class CameraView extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int cameraId=1;
    private int w,h;
    private static final String TAG = "CamTestActivity";
    private Context mCtx=null;
    private LinearLayout camera_view = null;
    private SurfaceHolder surfaceHolder;
    private int image_count = 1;
    public CameraView(Context context, LinearLayout camera_view, int cameraId, int w, int h){
        super(context);
        mCtx=context;
        this.camera_view = camera_view;
        //this.camera_view.addView(this);
        this.w=w;
        this.h=h;
        mCamera = null;
        this.cameraId=cameraId;
        //Toast.makeText(getContext(), String.valueOf("w="+w+" h="+h), Toast.LENGTH_SHORT).show();
        //when the surface is created, we can set the camera to draw images in this surfaceholder
        mCamera=Camera.open(this.cameraId);
        mCamera.setDisplayOrientation(90);
        //get the holder and set this class as the callback, so we can get camera data here
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try{
            this.surfaceHolder = surfaceHolder;
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            Camera.Size optimalSize = getOptimalPreviewSize(sizes, this.w, this.h);

            if (optimalSize != null) {
                parameters.setPreviewSize(optimalSize.width, optimalSize.height);
                //parameters.setPictureSize(this.w, this.h);
                //parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                //parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                //parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
                //parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
                //parameters.setExposureCompensation(0);
                parameters.setPictureFormat(ImageFormat.JPEG);
                //parameters.setJpegQuality(100);
                parameters.setRotation(270);
            }
            mCamera.setParameters(parameters);


        } catch (IOException e) {
            Log.d("ERROR", "Camera error on surfaceCreated " + e.getMessage());
        }
    }

    //referenced from http://stackoverflow.com/questions/17804309/android-camera-preview-wrong-aspect-ratio
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {

        //Toast.makeText(getContext(), "width: "+w +" Height: "+h, Toast.LENGTH_SHORT).show();
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w/h;

        if (sizes==null) return null;

        Camera.Size optimalSize = null;

        double minDiff = Double.MAX_VALUE;

        // Find size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - h);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - h);
                }
            }
        }
        return optimalSize;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        //before changing the application orientation, you need to stop the preview, rotate and then start it again
        if(mHolder.getSurface() == null)//check if the surface is ready to receive camera data
            return;

        try{
            mCamera.stopPreview();
        } catch (Exception e){
            //this will happen when you are trying the camera if it's not running
        }

        //now, recreate the camera preview
        try{
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("ERROR", "Camera error on surfaceChanged " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //our app has only one screen, so we'll destroy the camera in the surface
        //if you are unsing with more screens, please move this code your activity
        mCamera.stopPreview();
        mCamera.release();
        mCamera=null;
    }

    private void resetCam() {
        mCamera.startPreview();
        //this.camera_view.removeAllViews();
        //this.camera_view.addView(this);
    }

//    private void refreshGallery(File file) {
//        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        mediaScanIntent.setData(Uri.fromFile(file));
//        sendBroadcast(mediaScanIntent);
//    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            //			 Log.d(TAG, "onShutter'd");
        }
    };

    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //			 Log.d(TAG, "onPictureTaken - raw");
        }
    };

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            mCamera.startPreview();
            new SaveImageTask().execute(data);
            //resetCam();
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };

    public void takePicture(){
        mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            // Write to SD Card
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/Perfit");
                dir.mkdirs();

                String image_name = null;
                switch (image_count){
                    case 1: image_name = "background";break;
                    case 2: image_name = "front";break;
                    case 3: image_name = "right";break;
                    case 4: image_name = "back";break;
                    case 5: image_name = "left";break;
                    default: image_name = "error";
                }
                image_count++;

                //String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, image_name+".jpg");
                if(outFile.exists()) {
                    boolean delete = outFile.delete();
                }

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());
                //Toast.makeText(mCtx, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath(), Toast.LENGTH_SHORT);

                //refreshGallery(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

    }
}
