package com.reconciliationhouse.android.loverekindle.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocalMediaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LocalMedia media);

    @Update
    void update(LocalMedia media);

    @Delete
    void delete(LocalMedia media);

    /**
     * Gets a list of all the local media in the database.
     */
    @Query("SELECT * FROM local_media_table")
    LiveData<List<LocalMedia>> getAll();

    /**
     * @param mediaId mediaId of the media to fetch
     *                Gets the LiveData of a local media with mediaId in the database.
     */
    @Query("SELECT * FROM local_media_table WHERE mediaId=:mediaId")
    LiveData<LocalMedia> getMediaLiveData(String mediaId);

    @Query("SELECT * FROM local_media_table WHERE mediaId=:mediaId")
    LocalMedia getMedia(String mediaId);

    @Query("UPDATE local_media_table SET media_uri=:file_uri WHERE mediaId=:id")
    void updateUrl(String id, String file_uri);

    @Query("DELETE FROM local_media_table")
    void deleteAll();

}
