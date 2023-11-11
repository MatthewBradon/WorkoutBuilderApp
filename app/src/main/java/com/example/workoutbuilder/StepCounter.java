package com.example.workoutbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StepCounter extends AppCompatActivity {

    Button stepCounterButton, workoutsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        stepCounterButton = findViewById(R.id.btnStepCounter);
        workoutsButton = findViewById(R.id.btnWorkouts);

        //Navigation Buttons to other activities
        workoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}