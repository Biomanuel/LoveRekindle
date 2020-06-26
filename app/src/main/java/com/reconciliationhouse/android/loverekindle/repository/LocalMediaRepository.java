package com.reconciliationhouse.android.loverekindle.repository;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.reconciliationhouse.android.loverekindle.data.LocalDatabase;
import com.reconciliationhouse.android.loverekindle.data.LocalMedia;
import com.reconciliationhouse.android.loverekindle.data.LocalMediaDao;
import com.reconciliationhouse.android.loverekindle.models.MediaItem;

import java.util.HashMap;
import java.util.List;

public class LocalMediaRepository {
    private static final String TAG = LocalMediaRepository.class.getSimpleName();
    private final LiveData<List<LocalMedia>> mAllLocalMedia;
    private final LocalMediaDao mLocalMediaDao;

    public LocalMediaRepository(Application application) {
        LocalDatabase db = LocalDatabase.getInstance(application);
        mLocalMediaDao = db.mLocalMediaDao();
        mAllLocalMedia = mLocalMediaDao.getAll();
    }

    public LiveData<LocalMedia> getMedia(String mediaId) {
        if (mAllLocalMedia.getValue() != null) {
            for (LocalMedia media :
                    mAllLocalMedia.getValue()) {
                if (media.getMediaId().equals(mediaId)) return new MutableLiveData<>(media);
            }
        }
        return mLocalMediaDao.getMediaLiveData(mediaId);
    }

    public void insertMedia(LocalMedia media) {
        if (mAllLocalMedia.getValue() != null) mAllLocalMedia.getValue().add(media);
        new InsertAsyncTask(mLocalMediaDao).execute(media);
    }

    public void updateMedia(LocalMedia media) {
        //HashMap<String, LocalMedia> variable = new HashMap<>();
        //variable.put(UpdateAsyncTask.LOCAL_MEDIA, media);
        //new UpdateAsyncTask(mLocalMediaDao).execute(variable);
        new UpdateMediaTask(mLocalMediaDao).execute(media);
    }

    public void updateUrl(String mediaId, String fileUri) {
        HashMap<String, LocalMedia> variable = new HashMap<>();
        //variable.put(UpdateAsyncTask.LOCAL_MEDIA, media);
        new UpdateAsyncTask(mLocalMediaDao).execute(variable);
    }

    private static class PopulateAsyncTask extends AsyncTask<Void, Void, Void> {

        private final LocalMediaDao mDao;
        private final List<MediaItem> mMediaItems = MediaRepo.getInstance().getAllMedia().getValue();

        private PopulateAsyncTask(LocalDatabase db) {
            mDao = db.mLocalMediaDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            //mDao.deleteAll();

            for (MediaItem mediaItem : mMediaItems) {
                LocalMedia localMedia = new LocalMedia(mediaItem);
                mDao.insert(localMedia);
            }
            return null;
        }
    }

    private class InsertAsyncTask extends AsyncTask<LocalMedia, Void, Void> {

        LocalMediaDao mLocalMediaDao;
        MutableLiveData<LocalMedia> insertedMedia = new MutableLiveData<>();

        public InsertAsyncTask(LocalMediaDao localMediaDao) {
            mLocalMediaDao = localMediaDao;
        }

        @Override
        protected Void doInBackground(LocalMedia... localMedia) {
            try {
                mLocalMediaDao.insert(localMedia[0]);
                Log.e(TAG, "Media Inserted: " + localMedia[0].getMediaId());
            } catch (SQLiteConstraintException e) {
                Log.e(TAG, "doInBackground: InsetError", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    private class UpdateAsyncTask extends AsyncTask<HashMap<String, LocalMedia>, Void, Void> {

        public static final String LOCAL_MEDIA = "Local_Media";
        public static final String URI_KEY = "URI";
        public static final String CURRENT_STATE = "Current_State";
        LocalMediaDao mLocalMediaDao;

        public UpdateAsyncTask(LocalMediaDao localMediaDao) {
            mLocalMediaDao = localMediaDao;
        }

        @Override
        protected Void doInBackground(HashMap<String, LocalMedia>... hashMaps) {
            try {
                HashMap<String, LocalMedia> variable = hashMaps[0];
                for (String key : variable.keySet()) {
                    switch (key) {
                        case LOCAL_MEDIA:
                            mLocalMediaDao.update(variable.get(key));
                            break;
                        case URI_KEY:
                            mLocalMediaDao.updateUrl(variable.get(key).getMediaId(), variable.get(key).getMedia_uri());
                            break;
                        case CURRENT_STATE:
                            break;
                    }
                    Log.e(TAG, "Media Updated Successfully: " + variable.get(key).getMediaId());
                }
            } catch (RuntimeException e) {
                Log.e(TAG, "doInBackground: UpdateLocalMedia Failed", e);
                e.printStackTrace();
            }
            return null;
        }
    }

    private class UpdateMediaTask extends AsyncTask<LocalMedia, Void, Void> {

        LocalMediaDao mDao;

        public UpdateMediaTask(LocalMediaDao localMediaDao) {
            mDao = localMediaDao;
        }

        @Override
        protected Void doInBackground(LocalMedia... localMedia) {
            try {
                mDao.update(localMedia[0]);
                Log.e(TAG, "Media Updated Successfully: " + localMedia[0].getMediaId());
            } catch (RuntimeException e) {
                Log.e(TAG, "doInBackground: UpdateLocalMedia Failed", e);
                e.printStackTrace();
            }
            return null;
        }
    }
}
