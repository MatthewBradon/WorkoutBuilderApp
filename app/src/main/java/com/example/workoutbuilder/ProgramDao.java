package com.example.workoutbuilder;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface ProgramDao {

    @Insert
    void insert(Program program);

    @Update
    void update(Program program);

    @Delete
    void delete(Program program);

    @Query("DELETE FROM program_table")
    void deleteAllPrograms();

    @Query("SELECT * FROM program_table ORDER BY name ASC")
    LiveData<List<Program>> getAllPrograms();
}
