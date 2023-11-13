package com.example.workoutbuilder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModal extends AndroidViewModel {

    private ProgramRepository repository;
    private LiveData<List<Program>> allPrograms;

    public ViewModal(@NonNull Application application) {
        super(application);
        repository = new ProgramRepository(application);
        allPrograms = repository.getAllPrograms();
    }

    public void insert(Program program) {
        repository.insert(program);
    }

    public void update(Program program) {
        repository.update(program);
    }

    public void delete(Program program) {
        repository.delete(program);
    }

    public void deleteAllPrograms() {
        repository.deleteAllPrograms();
    }

    public LiveData<List<Program>> getAllPrograms() {
        return allPrograms;
    }
}