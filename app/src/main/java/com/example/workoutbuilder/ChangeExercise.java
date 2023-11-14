package com.example.workoutbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

public class ChangeExercise extends AppCompatActivity {

    private EditText nameET, repsET, setET;
    private Button changeBtn, workoutBtn;
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

        workoutBtn = findViewById(R.id.btnWorkouts);

        workoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ChangeExercise.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}