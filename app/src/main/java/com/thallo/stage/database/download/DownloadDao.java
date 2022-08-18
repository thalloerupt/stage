package com.thallo.stage.database.download;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.thallo.stage.database.bookmark.Bookmark;

import java.util.List;

@Dao
public interface DownloadDao {
    @Insert
    void insertDownload(Download... downloads);
    @Update
    void updateDownload(Download... downloads);
    @Delete
    void deleteDownload(Download... downloads);
    @Query("Delete FROM DOWNLOAD")
    void deleteAllDownload();


    @Query("SELECT * FROM Download ORDER BY ID DESC")
    LiveData<List<Download>> getAllDownloadsLive();

}
