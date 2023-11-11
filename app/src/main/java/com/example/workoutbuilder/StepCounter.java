package com.example.workoutbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StepCounter extends AppCompatActivity implements SensorEventListener{

    Button stepCounterButton, workoutsButton;
    TextView stepCounterTV;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;

    private int stepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        stepCounterButton = findViewById(R.id.btnStepCounter);
        workoutsButton = findViewById(R.id.btnWorkouts);
        stepCounterTV = findViewById(R.id.stepCountTV);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(stepCounterSensor == null){
            stepCounterTV.setText("Step Counter not available");
        }



        //Navigation Buttons to other activities
        workoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepCounter.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        if(stepCounterSensor != null){
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    /*
    @Override
    protected void onStop() {
        super.onStop();

        if(stepCounterSensor != null){
            sensorManager.unregisterListener(this);
        }
    }
     */

    //Methods to implement from SensorEventListener
    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        if(sensorEvent.sensor.getType() != Sensor.TYPE_STEP_COUNTER) return;

        stepCount = (int) sensorEvent.values[0];
        stepCounterTV.setText(String.valueOf(stepCount));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i ){

    }
}