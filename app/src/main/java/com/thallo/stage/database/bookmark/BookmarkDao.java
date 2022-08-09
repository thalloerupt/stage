package com.thallo.stage.database.bookmark;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface BookmarkDao {
    @Insert
    void insertBookmark(Bookmark... bookmarks);
    @Update
    void updateBookmark(Bookmark... bookmarks);
    @Delete
    void deleteBookmark(Bookmark... bookmarks);
    @Query("Delete FROM BOOKMARK")
    void deleteAllBookmark();


    @Query("SELECT * FROM Bookmark ORDER BY ID DESC")
    LiveData<List<Bookmark>> getAllBookmarksLive();
    @Query("SELECT * FROM Bookmark WHERE url_info LIKE:pattern ORDER BY ID DESC")
    LiveData<List<Bookmark>> findBookmarksWithPattern(String pattern);
    @Query("SELECT * FROM Bookmark WHERE title_info LIKE:pattern ORDER BY ID DESC")
    LiveData<List<Bookmark>> findBookmarksWithTitle(String pattern);
    @Query("SELECT * FROM Bookmark WHERE show_info LIKE:pattern ORDER BY ID DESC")
    LiveData<List<Bookmark>> findBookmarksWithShow(Boolean pattern);
}
