package com.example.workoutbuilder;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "step_count")
public class StepCountEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int stepCount;
    private int dayOfMonth;

    public StepCountEntity(int stepCount, int dayOfMonth) {
        this.stepCount = stepCount;
        this.dayOfMonth = dayOfMonth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {this.stepCount = stepCount;}

    public int getDayOfMonth() {
        return dayOfMonth;
    }
}
