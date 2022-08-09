package com.thallo.stage.database.history;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

class HistoryRepository {
    private LiveData<List<History>> allHistoriesLive;
    private HistoryDao historyDao;

    HistoryRepository(Context context) {
        com.thallo.stage.database.StageData stageData = com.thallo.stage.database.StageData.getDatabase(context.getApplicationContext());
        historyDao = stageData.getHistoryDao();
        allHistoriesLive = historyDao.getAllHistoriesLive();
    }

    void insertHistory(History... histories) {
        new InsertAsyncTask(historyDao).execute(histories);
    }

    void updateHistory(History... histories) {
        new UpdateAsyncTask(historyDao).execute(histories);
    }

    void deleteHistory(History... histories) {
        new DeleteAsyncTask(historyDao).execute(histories);
    }

    void deleteAllHistories(History... histories) {
        new DeleteAllAsyncTask(historyDao).execute();
    }


    LiveData<List<History>> getAllHistoriesLive() {
        return allHistoriesLive;
    }
    LiveData<List<History>> findHistoriesWithPattern(String pattern){
        return historyDao.findHistoriesWithPattern("%"+pattern+"%");
    }
    LiveData<List<History>> findWordsWithTitle(String pattern){
        return historyDao.findHistoriesWithTitle(pattern);
    }
    LiveData<List<History>> findWordsWithMix(String pattern){
        return historyDao.findHistoriesWithMix(pattern);
    }



    static class InsertAsyncTask extends AsyncTask<History, Void, Void> {
        private HistoryDao historyDao;

        InsertAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(History... histories) {
            historyDao.insertHistory(histories);
            return null;
        }

    }

    static class UpdateAsyncTask extends AsyncTask<History, Void, Void> {
        private HistoryDao historyDao;

        UpdateAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(History... histories) {
            historyDao.updateHistory(histories);
            return null;
        }

    }

    static class DeleteAsyncTask extends AsyncTask<History, Void, Void> {
        private HistoryDao historyDao;

        DeleteAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(History... histories) {
            historyDao.deleteHistory(histories);
            return null;
        }

    }

    static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private HistoryDao historyDao;

        DeleteAllAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            historyDao.deleteAllHistories();
            return null;
        }

    }
}
