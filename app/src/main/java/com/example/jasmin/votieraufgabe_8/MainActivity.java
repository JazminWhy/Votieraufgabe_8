package com.example.jasmin.votieraufgabe_8;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Runnable, SensorEventListener {

    String zustand;
    int int_zustand;
    View view;
    EditText setup;
    Handler timerhandler;
    int milli;
    SharedPreferences prefs;
    TextView milli_t;
    Button setup_button, new_game ;
    SensorManager manager;
    Sensor light;
    ArrayList<Float> light_values;
    float b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int_zustand = 0;
        view = findViewById(android.R.id.content);
        //SensorManager aufsetzen
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //Sensor auswählen
        light = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //Listener registrieren
        System.out.println("max" + light.getMaximumRange());
        setup_button = findViewById(R.id.setup);
        light_values = new ArrayList<Float>();
        prefs = getSharedPreferences("Time", MODE_PRIVATE);
        milli = prefs.getInt("Milli", 5000);
        setup = findViewById(R.id.setup_input);
        new_game = findViewById(R.id.neu);
        milli_t = findViewById(R.id.textView);
        getZustand(int_zustand);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        b = sensorEvent.values[0];
        System.out.println("" + b);
        light_values.add(b);
        if((b - light_values.get(0))>10000){
            getZustand(3);
        }
        }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void getZustand(int zustand){
        int color = 0;
        switch(zustand){
            case 0:
            color = Color.WHITE;

                milli_t.setVisibility(View.VISIBLE);
                setup.setVisibility(View.VISIBLE);
                setup_button.setVisibility(View.VISIBLE);
                break;
            case 1:
                setup.clearAnimation();
                setup_button.setVisibility(View.GONE);
                milli_t.setVisibility(View.GONE);
                setup.setVisibility(View.GONE);
                color = Color.YELLOW;

                break;
            case 2:
                color = Color.RED;
                manager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL); //normale ABrufhäufigkeit
                break;
            case 3:
                color = Color.GREEN;
                milli_t.setText("A special gift for a special person. With love, xoxo GossipGirl");
                milli_t.setVisibility(View.VISIBLE);
                new_game.setVisibility(View.VISIBLE);
                break;

        }
        view.setBackgroundColor(color);
    }

    public void clickSetup(View aView){
        String hilfe;
        hilfe = setup.getText().toString();
        if(hilfe.length()!=0){
            milli = Integer.parseInt(hilfe);
            SharedPreferences.Editor e = prefs.edit();
            e.putInt("Milli", milli);
            e.commit();
        }
       else{
            milli = prefs.getInt("Milli", 5000);
        }
        getZustand(1);
       timerhandler = new Handler();
       timerhandler.postDelayed(this, milli);

    }

    public void clickNew(View aView){
        getZustand(0);
        new_game.setVisibility(View.GONE);
    }
    @Override
    public void run() {
        getZustand(2);
    }
}
