package com.example.workoutbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class DeleteProgram extends AppCompatActivity {

    private Button deleteBtn, quitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_program);

        deleteBtn = findViewById(R.id.yesBtn);
        quitBtn = findViewById(R.id.noBtn);
        String programJSONString = getIntent().getStringExtra("programJSONString");

        quitBtn.setOnClickListener(v -> {
            finish();
        });

        deleteBtn.setOnClickListener(v -> {
            Intent data = new Intent();
            data.putExtra("programJSONString", programJSONString);
            setResult(RESULT_OK, data);
            finish();
        });
    }
}