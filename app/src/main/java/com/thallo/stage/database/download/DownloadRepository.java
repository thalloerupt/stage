package com.thallo.stage.database.download;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;



import java.util.List;

public class DownloadRepository {
    DownloadDao downloadDao;
    LiveData<List<Download>> allDownloadLive;

    DownloadRepository(Context context){
        com.thallo.stage.database.StageData stageData = com.thallo.stage.database.StageData.getDatabase(context.getApplicationContext());
        downloadDao = stageData.getDownloadDAO();
        allDownloadLive = downloadDao.getAllDownloadsLive();
    }
    public LiveData<List<Download>> getAllDownloadLive() {
        return allDownloadLive;
    }

    public void insertDownload(Download... downloads) {
        new DownloadRepository.InsertAsyncTask(downloadDao).execute(downloads);
    }

    public void updateDownload(Download... downloads) {
        new DownloadRepository.UpdateAsyncTask(downloadDao).execute(downloads);
    }

    public void deleteDownload(Download... downloads) {
        new DownloadRepository.DeleteAsyncTask(downloadDao).execute(downloads);
    }

    public void deleteAlldownloads() {
        new DownloadRepository.DeleteAllAsyncTask(downloadDao).execute();
    }
    

    static class InsertAsyncTask extends AsyncTask<Download, Void, Void> {
        private DownloadDao downloadDao;

        InsertAsyncTask(DownloadDao downloadDao) {
            this.downloadDao = downloadDao;
        }

        @Override
        protected Void doInBackground(Download... downloads) {
            downloadDao.insertDownload(downloads);
            return null;
        }

    }

    static class UpdateAsyncTask extends AsyncTask<Download, Void, Void> {
        private DownloadDao downloadDao;

        UpdateAsyncTask(DownloadDao downloadDao) {
            this.downloadDao = downloadDao;
        }

        @Override
        protected Void doInBackground(Download... downloads) {
            downloadDao.updateDownload(downloads);
            return null;
        }

    }

    static class DeleteAsyncTask extends AsyncTask<Download, Void, Void> {
        private DownloadDao downloadDao;

        DeleteAsyncTask(DownloadDao downloadDao) {
            this.downloadDao = downloadDao;
        }

        @Override
        protected Void doInBackground(Download... downloads) {
            downloadDao.deleteDownload(downloads);
            return null;
        }

    }

    static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private DownloadDao downloadDao;

        DeleteAllAsyncTask(DownloadDao downloadDao) {
            this.downloadDao = downloadDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            downloadDao.deleteAllDownload();
            return null;
        }

    }
}
