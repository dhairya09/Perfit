package com.example.dhairyapujara.perfit_submit;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;

/**
 * Created by srinidhikarthikbs on 2/9/17.
 */
//Referenced from https://gist.github.com/abdelhady/501f6e48c1f3e32b253a#file-deviceorientation
class DeviceOrientation {
    private int smoothness = 5;
    private float averagePitch = 0;
    private float averageRoll = 0;
    private static final int FROM_RADS_TO_DEGS = -57;

    private float[] pitches;
    private float[] rolls;
    static DeviceOrientation instance = null;

    DeviceOrientation() {
        pitches = new float[smoothness];
        rolls = new float[smoothness];
    }

    static DeviceOrientation getInstance(){
        if(instance==null){
            instance = new DeviceOrientation();
            return instance;
        }
        else
            return instance;
    }

    SensorEventListener getEventListener() {
        return sensorEventListener;
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        float[] mGravity;
        float[] mGeomagnetic;

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mGravity = event.values;
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mGeomagnetic = event.values;
            if (mGravity != null && mGeomagnetic != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    float orientationData[] = new float[3];
                    SensorManager.getOrientation(R, orientationData);
                    averagePitch = addValue(orientationData[1], pitches);
                    averageRoll = addValue(orientationData[2], rolls);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }
    };

    float getRoll() {
        return averageRoll;
    }

    float getPitch() {
        return averagePitch;
    }

    private float addValue(float value, float[] values) {
        //return value*FROM_RADS_TO_DEGS;
        value = (float) Math.round((Math.toDegrees(value)));
        float average = 0;
        for (int i = 1; i < smoothness; i++) {
            values[i - 1] = values[i];
            average += values[i];
        }
        values[smoothness - 1] = value;
        average = (average + value) / smoothness;
        return average;
    }
}
