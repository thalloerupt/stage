package com.thallo.stage.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

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


    static final Migration MIGRATION_TASK_TO_DOWNLOAD=new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Download ADD COLUMN id_task ");

        }
    };
    static final Migration MIGRATION_ID_TO_DOWNLOAD=new Migration(3,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Download ADD COLUMN id_id INTEGER");

        }
    };
    static final Migration MIGRATION_IDs_OUT_DOWNLOAD=new Migration(4,5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE Download_s (id INTEGER PRIMARY KEY NOT NULL,id_id INTEGER NOT NULL DEFAULT 0)");
            database.execSQL("INSERT INTO Download_s (id  ,id_id ) SELECT id,id_id FROM Download");
            database.execSQL("DROP TABLE Download");
            database.execSQL("ALTER TABLE Download_s RENAME TO Download");

        }
    };

}
