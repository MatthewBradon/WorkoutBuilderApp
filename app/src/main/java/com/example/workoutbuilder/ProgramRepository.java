package com.example.workoutbuilder;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ProgramRepository {

    private ProgramDao programDao;
    private LiveData<List<Program>> allPrograms;

    public ProgramRepository(Application application) {
        ProgramDatabase database = ProgramDatabase.getInstance(application);
        programDao = database.programDao();
        allPrograms = programDao.getAllPrograms();
    }

    public void insert(Program program) {
        new InsertProgramAsyncTask(programDao).execute(program);
    }

    public void update(Program program) {
        new UpdateProgramAsyncTask(programDao).execute(program);
    }

    public void delete(Program program) {
        new DeleteProgramAsyncTask(programDao).execute(program);
    }

    public void deleteAllPrograms() {
        new DeleteAllProgramsAsyncTask(programDao).execute();
    }

    public LiveData<List<Program>> getAllPrograms() {
        return allPrograms;
    }

    private static class InsertProgramAsyncTask extends AsyncTask<Program, Void, Void> {
        private ProgramDao programDao;

        private InsertProgramAsyncTask(ProgramDao programDao) {
            this.programDao = programDao;
        }

        @Override
        protected Void doInBackground(Program... programs) {
            programDao.insert(programs[0]);
            return null;
        }
    }

    private static class UpdateProgramAsyncTask extends AsyncTask<Program, Void, Void> {
        private ProgramDao programDao;

        private UpdateProgramAsyncTask(ProgramDao programDao) {
            this.programDao = programDao;
        }

        @Override
        protected Void doInBackground(Program... programs) {
            programDao.update(programs[0]);
            return null;
        }
    }

    private static class DeleteProgramAsyncTask extends AsyncTask<Program, Void, Void> {
        private ProgramDao programDao;

        private DeleteProgramAsyncTask(ProgramDao programDao) {
            this.programDao = programDao;
        }

        @Override
        protected Void doInBackground(Program... programs) {
            programDao.delete(programs[0]);
            return null;
        }
    }

    private static class DeleteAllProgramsAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgramDao programDao;

        private DeleteAllProgramsAsyncTask(ProgramDao programDao) {
            this.programDao = programDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            programDao.deleteAllPrograms();
            return null;
        }
    }
}
