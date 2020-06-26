package com.reconciliationhouse.android.loverekindle.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {LocalMedia.class}, version = 1, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {

    private static volatile LocalDatabase INSTANCE;
    private static RoomDatabase.Callback mOpeningCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

        }
    };

    public static LocalDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDatabase.class, "love_rekindle_db")
                            .fallbackToDestructiveMigration()
                            .addCallback(mOpeningCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract LocalMediaDao mLocalMediaDao();

    private static class ActivateAsyncTask extends AsyncTask<Void, Void, Void> {

        private final LocalMediaDao mDao;
        LocalMedia dummyMedia = new LocalMedia("0", " ", 0, " ", " ", true, " ", " ");

        private ActivateAsyncTask(LocalDatabase db) {
            mDao = db.mLocalMediaDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //I did this to instantiate the database.
            mDao.insert(dummyMedia);
            return null;
        }
    }
}
