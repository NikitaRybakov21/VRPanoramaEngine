package com.example.vrpanoramaengine.panorama.render;

import static android.content.Context.SENSOR_SERVICE;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.vrpanoramaengine.MainActivity;

public class RotateSceneInertialSensorListener {
    private final PanoramaRender scene3D;
    private final SensorManager sensorManager;
    private boolean listenerIsEnabled = true;

    double mAngleRotY = -1000;
    double mAngleRotX = -1000;
    double az;

    public RotateSceneInertialSensorListener(PanoramaRender scene3D, MainActivity mainActivity) {
        sensorManager = (SensorManager) mainActivity.getSystemService(SENSOR_SERVICE);
        this.scene3D = scene3D;
        listenerLinearAcceleration();
        listenerInertialSensors();
    }

    private void listenerLinearAcceleration() {
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                az = sensorEvent.values[2];
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        }, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    private void listenerInertialSensors() {
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                calculateAngleRotateAxisY(sensorEvent);
                calculateAngleRotateAxisX(sensorEvent);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        }, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_GAME);
    }

    private void calculateAngleRotateAxisY(SensorEvent sensorEvent) {
        float[] orientation = new float[3];
        float[] rMat = new float[9];

        SensorManager.getRotationMatrixFromVector(rMat, sensorEvent.values);
        double newAzimuth = (((((Math.toDegrees(SensorManager.getOrientation( rMat, orientation )[0]) + 360) % 360)
                - (Math.toDegrees( SensorManager.getOrientation( rMat, orientation )[2]))) +360) % 360);
        newAzimuth *= -1;

        if(mAngleRotY == -1000) {
            mAngleRotY = newAzimuth;
        }
        double dAngle = newAzimuth - mAngleRotY;
        if(listenerIsEnabled) {
            scene3D.rotateScenePanoramaAxisY(dAngle);
        }
        mAngleRotY = newAzimuth;
    }

    private void calculateAngleRotateAxisX(SensorEvent sensorEvent) {
        float[] orientation = new float[3];
        float[] rMat = new float[9];

        boolean isPositive = az > 0;

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, sensorEvent.values);
            SensorManager.getOrientation(rMat, orientation);
            double pitch = -(Math.toDegrees(orientation[1]));

            if(mAngleRotX == -1000) {
                mAngleRotX = pitch;
            }
            double dAngle = pitch - mAngleRotX;
            if (isPositive) {
                dAngle *= -1;
            }
            if(listenerIsEnabled) {
                scene3D.rotateScenePanoramaAxisX(dAngle);
            }
            mAngleRotX = pitch;
        }
    }

    public void sensorEnabled() {
        listenerIsEnabled = true;
    }

    public void sensorDisabled() {
        listenerIsEnabled = false;
    }
}
