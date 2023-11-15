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

import com.google.android.material.textfield.TextInputEditText;

public class ChangeExercise extends AppCompatActivity {

    private EditText nameET, repsET, setET;
    private Button changeBtn, workoutBtn, deleteBtn;
    private String programJSONString;
    private String workoutName;
    private  String exerciseToEditJSON;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_exercise);

        programJSONString = getIntent().getStringExtra("programJSONString");
        workoutName = getIntent().getStringExtra("workoutName");
        exerciseToEditJSON = getIntent().getStringExtra("exerciseToEditJSON");

        nameET = findViewById(R.id.editTVName);
        repsET = findViewById(R.id.editRepsTV);
        setET = findViewById(R.id.editSetTV);

        changeBtn = findViewById(R.id.changeBtn);
        deleteBtn = findViewById(R.id.deleteButton);

        System.out.println(programJSONString);
        System.out.println(workoutName);
        System.out.println(exerciseToEditJSON);


        changeBtn.setOnClickListener(v -> {
            String name = nameET.getText().toString();
            String reps = repsET.getText().toString();
            String set = setET.getText().toString();

            Intent data = new Intent();

            data.putExtra("name", name);
            data.putExtra("reps", reps);
            data.putExtra("set", set);
            data.putExtra("programJSONString", programJSONString);
            data.putExtra("workoutName", workoutName);
            data.putExtra("exerciseToEditJSON", exerciseToEditJSON);
            setResult(RESULT_OK, data);

            finish();
        });

        deleteBtn.setOnClickListener(v -> {
            Intent data = new Intent();

            data.putExtra("programJSONString", programJSONString);
            data.putExtra("workoutName", workoutName);
            data.putExtra("exerciseToEditJSON", exerciseToEditJSON);

            setResult(MainActivity.RESULT_DELETE, data);
            finish();
        });

        workoutBtn = findViewById(R.id.btnWorkouts);

        workoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ChangeExercise.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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