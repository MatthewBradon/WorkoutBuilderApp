package com.example.workoutbuilder;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Program.class}, version = 1)
public abstract class ProgramDatabase extends RoomDatabase {

    private static ProgramDatabase instance;

    public abstract ProgramDao programDao();

    public static synchronized ProgramDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ProgramDatabase.class, "program_database"
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
        private final ProgramDao programDao;

        PopulateDbAsyncTask(ProgramDatabase instance) {
            programDao = instance.programDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Perform any initial data population for the Program entity here
            // Example: programDao.insert(new Program(...));
            return null;
        }
    }
}
