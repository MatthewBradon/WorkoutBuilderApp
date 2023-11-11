package com.example.workoutbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateWorkout extends AppCompatActivity {

    Button stepCounterBtn, workoutsBtn, createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        stepCounterBtn = findViewById(R.id.btnStepCounter);
        workoutsBtn = findViewById(R.id.btnWorkouts);
        createBtn = findViewById(R.id.createbutton);

        workoutsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        stepCounterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateWorkout.this, StepCounter.class);
                startActivity(intent);
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                finish();
            }
        });
    }
}