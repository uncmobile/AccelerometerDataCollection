package com.example.accelerometerdatacollection;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    int total_samples = 100; //we save only this many samples
    int timegap = 100000; //100000 usec between consecutive samples
    ArrayList<Float> ax = new ArrayList<Float>(100);
    ArrayList<Float> bx = new ArrayList<Float>(100);
    ArrayList<Float> cx = new ArrayList<Float>(100);

    SensorManager sm = null;
    Sensor s = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        s = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public void foo(View v)
    {
        sm.unregisterListener(this);
        ax.clear();bx.clear();cx.clear();
        sm.registerListener(this, s, timegap);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        ax.add(sensorEvent.values[0]);
        bx.add(sensorEvent.values[1]);
        cx.add(sensorEvent.values[2]);

        if(ax.size() >= total_samples) {
            sm.unregisterListener(this);
            String filename = ((EditText)findViewById(R.id.edt)).getText().toString().trim();
            try{
                PrintWriter pr = new PrintWriter(new FileOutputStream(new File(getExternalFilesDir("MyDataFolder"),filename)));
                for(int i = 0; i < total_samples; i++) {
                    pr.println(ax.get(i) + "\t" + bx.get(i) + "\t" + cx.get(i));
                }
                pr.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            Log.v("MYTAG", filename + " is written.");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}