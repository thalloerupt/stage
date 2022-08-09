package com.thallo.stage.database.history;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface HistoryDao {
    @Insert
    void insertHistory(com.thallo.stage.database.history.History... histories) ;

    @Update
    void updateHistory(com.thallo.stage.database.history.History... histories);

    @Delete
    void deleteHistory(com.thallo.stage.database.history.History... histories);

    @Query("DELETE FROM HISTORY")
    void deleteAllHistories();


    @Query("SELECT * FROM HISTORY ORDER BY ID DESC")
    LiveData<List<com.thallo.stage.database.history.History>> getAllHistoriesLive();

    @Query("SELECT * FROM History WHERE url_info LIKE:pattern ORDER BY ID DESC")
    LiveData<List<History>> findHistoriesWithPattern(String pattern);

    @Query("SELECT * FROM History WHERE title_info LIKE:pattern ORDER BY ID DESC")
    LiveData<List<History>> findHistoriesWithTitle(String pattern);
    @Query("SELECT * FROM History WHERE mix LIKE:pattern ORDER BY ID DESC")
    LiveData<List<History>> findHistoriesWithMix(String pattern);
}
