package com.example.workoutbuilder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button stepCounterBtn, createWorkoutBtn;

    private RecyclerView programRV;

    private static final int ADD_COURSE_REQUEST = 1;

    private ViewModal viewModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepCounterBtn = findViewById(R.id.btnStepCounter);
        createWorkoutBtn = findViewById(R.id.createworkoutbutton);





        stepCounterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StepCounter.class);
                startActivity(intent);
            }
        });

        createWorkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateWorkout.class);
                startActivityForResult(intent, ADD_COURSE_REQUEST);

            }
        });

        programRV = findViewById(R.id.idRVprograms);

        programRV.setLayoutManager(new LinearLayoutManager(this));
        programRV.setHasFixedSize(true);

        final ProgramRVAdapter adapter = new ProgramRVAdapter();

        programRV.setAdapter(adapter);

        viewModal = ViewModelProviders.of(this).get(ViewModal.class);

        viewModal.getAllPrograms().observe(this, new Observer<List<Program>>() {
            @Override
            public void onChanged(List<Program> programs) {
                adapter.submitList(programs);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_COURSE_REQUEST && resultCode == RESULT_OK) {
            String programJSONString = data.getStringExtra("program");

            try {
                Program program = Program.fromJSON(new JSONObject(programJSONString));
                viewModal.insert(program);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Program saved", Toast.LENGTH_SHORT).show();
        }
    }
}