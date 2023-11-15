package com.example.workoutbuilder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayedWorkout extends AppCompatActivity {

    private Button stepCounterBtn, workoutsBtn;

    private TextView titleTV;
    private RecyclerView workoutRV;

    public static final int EDIT_EXERCISE_REQUEST = 1;

    public static final int ADD_EXERCISE_REQUEST = 2;



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

            WorkoutAdapter workoutAdapter = new WorkoutAdapter(this, program.getWorkouts(), programJSONString);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println("test 3");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_EXERCISE_REQUEST){
            if(resultCode == RESULT_OK){
                setResult(RESULT_OK, data);
                finish();
            }
            else if(resultCode == MainActivity.RESULT_DELETE) {
                setResult(MainActivity.RESULT_DELETE, data);
                finish();
            }
        }
        if(requestCode == ADD_EXERCISE_REQUEST && resultCode == MainActivity.RESULT_ADD){
            System.out.println("Test456");
            setResult(MainActivity.RESULT_ADD, data);
            finish();
        }
    }

}
