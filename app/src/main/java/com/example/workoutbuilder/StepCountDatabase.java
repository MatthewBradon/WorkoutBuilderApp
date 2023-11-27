package com.example.workoutbuilder;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

// StepCountDatabase.java
@Database(entities = {StepCountEntity.class}, version = 1)
public abstract class StepCountDatabase extends RoomDatabase {

    private static StepCountDatabase instance;

    public abstract StepCountDao stepCountDao();

    public static synchronized StepCountDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            StepCountDatabase.class, "step_count_database"
                    ).fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private final StepCountDao stepCountDao;

        PopulateDbAsyncTask(StepCountDatabase instance) {
            stepCountDao = instance.stepCountDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Perform any initial data population for the Program entity here
            // Example: programDao.insert(new Program(...));
            return null;
        }
    }
}
