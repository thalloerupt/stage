package com.thallo.stage.database.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {
    private HistoryRepository historyRepository;
    public HistoryViewModel(@NonNull Application application) {
        super(application);
        historyRepository = new HistoryRepository(application);
    }

     public LiveData<List<com.thallo.stage.database.history.History>> getAllHistoriesLive()
     {
        return historyRepository.getAllHistoriesLive();
     }
     LiveData<List<History>>findHistoriesWithPattern(String pattern){
        return historyRepository.findHistoriesWithPattern(pattern);
    }

    LiveData<List<History>>findHistoriesWithTitle(String pattern){
        return historyRepository.findWordsWithTitle(pattern);
    }
    LiveData<List<History>>findHistoriesWithMix(String pattern){
        return historyRepository.findWordsWithMix(pattern);
    }


    public void insertWords(com.thallo.stage.database.history.History... histories) {
        historyRepository.insertHistory(histories);
    }
    public void updateWords(com.thallo.stage.database.history.History... histories) {
        historyRepository.updateHistory(histories);
    }
    public void deleteWords(com.thallo.stage.database.history.History... histories) {
        historyRepository.deleteHistory(histories);
    }
    public void deleteAllWords() {
        historyRepository.deleteAllHistories();
    }


}