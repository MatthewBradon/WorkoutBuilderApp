package com.example.workoutbuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayedWorkout extends AppCompatActivity {

    Button stepCounterBtn, workoutsBtn;

    TextView titleTV;
    RecyclerView workoutRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayed_workout);

        stepCounterBtn = findViewById(R.id.btnStepCounter);
        workoutsBtn = findViewById(R.id.btnWorkouts);
        titleTV = findViewById(R.id.programNameTV);
        workoutRV = findViewById(R.id.workoutRV);

        String programJSONString = getIntent().getStringExtra("programJsonString");

        try {

            Program program = Program.fromJSON(new JSONObject(programJSONString));
            titleTV.setText(program.getName());

            WorkoutAdapter workoutAdapter = new WorkoutAdapter(this, program.getWorkouts());
            workoutRV.setLayoutManager(new LinearLayoutManager(this));
            workoutRV.setAdapter(workoutAdapter);

        } catch (JSONException e){
            e.printStackTrace();
        }



        workoutsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayedWorkout.this, MainActivity.class);
                finish();
            }
        });

        stepCounterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayedWorkout.this, StepCounter.class);
                startActivity(intent);
            }
        });
    }
}