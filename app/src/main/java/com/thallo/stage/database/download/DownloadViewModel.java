package com.thallo.stage.database.download;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.thallo.stage.database.bookmark.Bookmark;

import java.util.List;

public class DownloadViewModel extends AndroidViewModel {
    DownloadRepository downloadRepository;
    public DownloadViewModel(@NonNull Application application) {
        super(application);
        downloadRepository=new DownloadRepository(application);
    }

    public LiveData<List<Download>> getAllDownloadsLive() {
        return downloadRepository.getAllDownloadLive();
    }

    public void insertWords(Download... downloads) {
        downloadRepository.insertDownload(downloads);
    }
    public void updateWords(Download... downloads) {
        downloadRepository.updateDownload(downloads);
    }
    public void deleteWords(Download... downloads) {
        downloadRepository.deleteDownload(downloads);
    }
    public void deleteAllWords() {
        downloadRepository.deleteAlldownloads();
    }
}
