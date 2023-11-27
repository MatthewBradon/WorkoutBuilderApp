package com.example.workoutbuilder;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface StepCountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStepCount(StepCountEntity stepCountEntity);

    @Query("SELECT * FROM step_count WHERE dayOfMonth = :dayOfMonth LIMIT 1")
    StepCountEntity getStepCountByDay(int dayOfMonth);

    @Query("SELECT * FROM step_count")
    List<StepCountEntity> getAllStepCounts();

    @Update
    void updateStepCount(StepCountEntity stepCountEntity);

}