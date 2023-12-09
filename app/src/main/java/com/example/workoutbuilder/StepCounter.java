package com.example.workoutbuilder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import java.util.Calendar;

public class StepCounter extends AppCompatActivity implements SensorEventListener{

    Button stepCounterButton, workoutsButton;
    TextView stepCounterTV;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;

    private static final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 123;

    private int stepCount = 0;

    private StepCountDatabase stepCountDatabase;
    private StepCountDao stepCountDao;

    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        stepCounterButton = findViewById(R.id.btnStepCounter);
        workoutsButton = findViewById(R.id.btnWorkouts);
        stepCounterTV = findViewById(R.id.stepCountTV);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setMax(10000);
        progressBar.setProgress(0);

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
        ) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                    MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION
            );
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(stepCounterSensor == null){
            stepCounterTV.setText("Step Counter not available");
        }

        // Initialize the StepCountDatabase and StepCountDao
        stepCountDatabase = StepCountDatabase.getInstance(this);
        stepCountDao = stepCountDatabase.stepCountDao();

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
        //displayStepsForToday();
    }


    @Override
    protected void onStop() {
        super.onStop();

        if(stepCounterSensor != null){
            sensorManager.unregisterListener(this);
        }
    }

    //Methods to implement from SensorEventListener
    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        if(sensorEvent.sensor.getType() != Sensor.TYPE_STEP_COUNTER) return;

        stepCount = (int) sensorEvent.values[0];
        //stepCounterTV.setText(String.valueOf(stepCount));

        // Get today's date
        Calendar calendar = Calendar.getInstance();
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Check if there is an entry for today in the database using AsyncTask
        new CheckStepCountAsyncTask(stepCountDao, currentDayOfMonth, stepCount, stepCounterTV, progressBar).execute();


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i ){

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, register the sensor listener
                if (stepCounterSensor != null) {
                    sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST
                    );
                }
            } else {
                Toast.makeText(StepCounter.this,"You need physical activity permissions to use this feature", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class CheckStepCountAsyncTask extends AsyncTask<Void, Void, StepCountEntity> {
        private StepCountDao stepCountDao;
        private int currentDayOfMonth;
        private int stepCount;

        private TextView stepCounterTV;

        private ProgressBar progressBar;

        CheckStepCountAsyncTask(StepCountDao stepCountDao, int currentDayOfMonth, int stepCount, TextView stepCounterTV, ProgressBar progressBar) {
            this.stepCountDao = stepCountDao;
            this.currentDayOfMonth = currentDayOfMonth;
            this.stepCount = stepCount;
            this.stepCounterTV = stepCounterTV;
            this.progressBar = progressBar;
        }

        @Override
        protected StepCountEntity doInBackground(Void... voids) {

            StepCountEntity yesterdayStepCount = stepCountDao.getStepCountByDay(currentDayOfMonth-1);
            StepCountEntity todayStepCount = stepCountDao.getStepCountByDay(currentDayOfMonth);


            if(todayStepCount != null) {
                todayStepCount.setStepCount(stepCount);
                if(yesterdayStepCount != null) {
                    int difference = todayStepCount.getStepCount() - yesterdayStepCount.getStepCount();
                    todayStepCount.setStepCountDifference(difference);
                }
            }
            return todayStepCount;
        }

        @Override
        protected void onPostExecute(StepCountEntity todayStepCount) {
            // Process the result on the main thread
            if (todayStepCount != null) {
                int difference = todayStepCount.getStepCountDifference();
                if(difference < 0){
                    //Counter has been reseted need to delete all step entities in db
                    new DeleteAllStepCountAsyncTask(stepCountDao).execute();
                    //Create a new entry for new step count
                    StepCountEntity newStepCountEntity = new StepCountEntity(stepCount, currentDayOfMonth);
                    new InsertStepCountAsyncTask(stepCountDao).execute(newStepCountEntity);
                    stepCounterTV.setText(String.valueOf(newStepCountEntity.getStepCount()));
                    progressBar.setProgress(newStepCountEntity.getStepCount());
                }
                stepCounterTV.setText(String.valueOf(difference == 0 ? stepCount : difference));
                progressBar.setProgress(difference == 0 ? stepCount : difference);
                // Update the existing entry in the database using AsyncTask
                new UpdateStepCountAsyncTask(stepCountDao).execute(todayStepCount);
            } else {
                // Insert a new entry for today in the database using AsyncTask
                //TODO Check for yesterday to calculate difference
                StepCountEntity newStepCountEntity = new StepCountEntity(stepCount, currentDayOfMonth);
                newStepCountEntity.setStepCountDifference(0);
                new InsertStepCountAsyncTask(stepCountDao).execute(newStepCountEntity);
                stepCounterTV.setText(String.valueOf(newStepCountEntity.getStepCount()));
                progressBar.setProgress(newStepCountEntity.getStepCount());

            }
        }
    }

    private static class UpdateStepCountAsyncTask extends AsyncTask<StepCountEntity, Void, Integer> {
        private StepCountDao stepCountDao;




        UpdateStepCountAsyncTask(StepCountDao stepCountDao) {
            this.stepCountDao = stepCountDao;

        }

        @Override
        protected Integer doInBackground(StepCountEntity... stepCountEntities) {
            stepCountDao.updateStepCount(stepCountEntities[0]);
            return stepCountEntities[0].getStepCount();
        }
    }

    private static class InsertStepCountAsyncTask extends AsyncTask<StepCountEntity, Void, Integer> {
        private StepCountDao stepCountDao;


        InsertStepCountAsyncTask(StepCountDao stepCountDao) {
            this.stepCountDao = stepCountDao;
        }

        @Override
        protected Integer doInBackground(StepCountEntity... stepCountEntities) {
            stepCountDao.insertStepCount(stepCountEntities[0]);
            return stepCountEntities[0].getStepCount();
        }
    }

    private static class DeleteAllStepCountAsyncTask extends AsyncTask<StepCountEntity, Void, Integer> {
        private StepCountDao stepCountDao;


        DeleteAllStepCountAsyncTask(StepCountDao stepCountDao) {
            this.stepCountDao = stepCountDao;
        }

        @Override
        protected Integer doInBackground(StepCountEntity... stepCountEntities) {
            stepCountDao.deleteAllStepCount();
            return 0;
        }
    }
}