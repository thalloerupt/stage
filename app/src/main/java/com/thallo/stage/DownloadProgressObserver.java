package com.thallo.stage;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DownloadProgressObserver extends ViewModel {

    // Create a LiveData with a String
    private MutableLiveData<Integer> progress;

    public MutableLiveData<Integer> getProgress() {
        if (progress == null) {
            progress = new MutableLiveData<Integer>();
        }
        return progress;
    }

// Rest of the ViewModel...
}
