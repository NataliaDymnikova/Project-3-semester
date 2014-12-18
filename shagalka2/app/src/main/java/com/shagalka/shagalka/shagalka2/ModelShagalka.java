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

        steps = 0;
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
                        pagePlay.setNumberOfSteps(steps);
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
    private void getCurrentSpeed() {
        setToAccelerationValues();
        for (int i = 0; i < 3; i++) {
            average[i] += countAverage(speed[i], acceleration[i]);
            speed[i] += acceleration[i] * period / 1000;
        }

        if (Math.abs(acceleration[0]) < 0.1)
            acceleration[0] = 0;
        if (Math.abs(acceleration[1]) < 0.1)
            acceleration[1] = 0;

        CountSteps();
    }

    private void setToAccelerationValues() {

        float cosA = (float) Math.cos(valuesResult[1]);
        float cosB = (float) Math.cos(valuesResult[2]);
        float cosC = (float) Math.cos(valuesResult[0]);
        float sinA = (float) Math.sin(valuesResult[1]);
        float sinB = (float) Math.sin(valuesResult[2]);
        float sinC = (float) Math.sin(valuesResult[0]);
        float x = valuesLinAccel[0];
        float y = valuesLinAccel[1];
        float z = valuesLinAccel[2];

        acceleration[0] = x * cosB * cosC - y * (sinA * sinB * cosC + cosA * sinC)
                + z * (cosA * sinB * cosC - sinA * sinC);
        acceleration[1] = x * cosB * sinC - y * (sinA * sinB * sinC - cosA * cosC)
                + z * (cosA * sinB * sinC + sinA * cosC);
        acceleration[2] = -x * sinB - y * sinA * cosB + z * cosA * cosB;
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

    private void CountSteps() {
        if (!counter && acceleration[2] > accZ)
            counter = true;
        else if (counter && acceleration[2] < -accZ) {
            counter = false;
            steps++;
        }
    }

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
    // Количество шагов.
    private int steps;
    // Чувствительность шагомера.
    private float accZ = 1;
    private boolean counter = false;

    /// Parent game for sensors.
    private Game activ;
    /// Listener of changes.
    private PagePlay pagePlay;
}
