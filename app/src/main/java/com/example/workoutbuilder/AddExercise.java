package com.example.workoutbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class AddExercise extends AppCompatActivity {

    private EditText nameET, repsET, setET;
    private Button stepCounterBtn, workoutBtn, addBtn;
    private String programJSONString;
    private String workoutName;
    private  String exerciseToEditJSON;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        programJSONString = getIntent().getStringExtra("programJSONString");
        workoutName = getIntent().getStringExtra("workoutName");

        nameET = findViewById(R.id.editTVName);
        repsET = findViewById(R.id.editRepsTV);
        setET = findViewById(R.id.editSetTV);

        addBtn = findViewById(R.id.addBtn);
        stepCounterBtn = findViewById(R.id.btnStepCounter);
        workoutBtn = findViewById(R.id.btnWorkouts);

        workoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AddExercise.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        stepCounterBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AddExercise.this, StepCounter.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        addBtn.setOnClickListener(v -> {

            String name = nameET.getText().toString();
            String reps = repsET.getText().toString();
            String set = setET.getText().toString();

            Intent data = new Intent();

            data.putExtra("name", name);
            data.putExtra("reps", reps);
            data.putExtra("set", set);
            data.putExtra("programJSONString", programJSONString);
            data.putExtra("workoutName", workoutName);

            setResult(MainActivity.RESULT_ADD, data);
            finish();
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //This is used to hide the keyboard when the user clicks outside of an EditText
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            System.out.println("Test");
            View v = getCurrentFocus();
            if (v instanceof EditText) {

                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(event);
    }
}