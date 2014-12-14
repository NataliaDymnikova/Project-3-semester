package com.shagalka.shagalka.shagalka2;

import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Timer;
import java.util.TimerTask;

/// Model of shagalka. Work with sensors. Can return new position,
/// when telephone is move.
public class ModelShagalka {

    public ModelShagalka(Game activity, PagePlay pagePlay) {

        sensorManager = (SensorManager) activity.getSystemService(activity.SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorLinAccel = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorMagnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        speed = new float[3];
        average = new float[3];
        acceleration  = new float[3];
        valuesAccel = new float[3];
        valuesLinAccel = new float[3];
        valuesMagnet = new float[3];
        valuesResult = new float[3];

        activ = activity;
        this.pagePlay = pagePlay;
    }

    public void onResume() {

        sensorManager.registerListener(listener, sensorAccel, sensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listener, sensorMagnet, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listener, sensorLinAccel, SensorManager.SENSOR_DELAY_NORMAL);

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                activ.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getDeviceOrientation();
                        getCurrentSpeed();
                        pagePlay.setNewPoint(GetNewPoint());
                        //activ.setContentView(pagePlay);

                    }
                });
            }
        };
        timer.schedule(task, 0, period);
    }

    /// Считывает и считает отклонение устройства от осей в радианах.
    public void getDeviceOrientation() {
        float[] r = new float[9];
        SensorManager.getRotationMatrix(r, null, valuesAccel, valuesMagnet);
        SensorManager.getOrientation(r, valuesResult);
    }

    /// Возвращает пройденное расстояние
    private float countAverage(float speed, float accel) {
        return speed * period / 1000 + accel * period / 1000 * period / 1000 / 2;
    }

    /// Обновляет перемещение, скорость.
    public void getCurrentSpeed() {
        for (int i = 0; i < 3; i++) {
            if (Math.abs(valuesLinAccel[i]) > 0.5) {
                acceleration[i] = valuesLinAccel[i] * (float) Math.sin(valuesResult[(i + 1) % 3]);
                average[i] += countAverage(speed[i], acceleration[i]);
                speed[i] += acceleration[i] * period / 1000;
            }

            else if (Math.abs(speed[i]) > 0.4) {
                average[i] += speed[i] * period / 1000;
            }
        }
    }

    /// Get new point.
    public Point GetNewPoint() {
        return new Point((int)(average[0]), (int)(average[1]));
    }

    private SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    for (int i = 0; i < 3; i++){
                        valuesAccel[i] = event.values[i];
                    }
                    break;

                case Sensor.TYPE_LINEAR_ACCELERATION:
                    for (int i = 0; i < 3; i++){
                        valuesLinAccel[i] = event.values[i];
                    }
                    break;

                case Sensor.TYPE_MAGNETIC_FIELD:
                    for (int i = 0; i < 3; i++){
                        valuesMagnet[i] = event.values[i];
                    }
                    break;
            }
        }
    };

    private float[] valuesAccel;
    private float[] valuesLinAccel;
    private float[] valuesMagnet;
    // наклон устройства в радианах по каждой из осей.
    private float[] valuesResult;

    private SensorManager sensorManager;
    private Sensor sensorMagnet;
    private Sensor sensorLinAccel;
    private Sensor sensorAccel;
    private float[] speed;
    private float[] average;
    private float[] acceleration;
    private int period = 100;

    private Timer timer;
    Game activ;
    PagePlay pagePlay;
}
