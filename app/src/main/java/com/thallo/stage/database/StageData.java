package com.thallo.stage.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.thallo.stage.database.bookmark.Bookmark;

import com.thallo.stage.database.bookmark.BookmarkDao;
import com.thallo.stage.database.download.Download;
import com.thallo.stage.database.download.DownloadDao;
import com.thallo.stage.database.history.History;
import com.thallo.stage.database.history.HistoryDao;

@Database(entities = {Bookmark.class, History.class,Download.class},version = 2,exportSchema = false)
public abstract class StageData extends RoomDatabase {
    private static StageData INSTANCE;
    public static synchronized StageData getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), StageData.class,"database")
                    .build();
        }
        return INSTANCE;
    }

    public abstract HistoryDao getHistoryDao();
    public abstract BookmarkDao getBookmarkDao();
    public abstract DownloadDao getDownloadDAO();



}
