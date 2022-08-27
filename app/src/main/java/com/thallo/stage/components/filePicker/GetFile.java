package com.thallo.stage.components.filePicker;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.thallo.stage.BaseActivity;
import com.thallo.stage.components.filePicker.FilePicker;

public class GetFile {
    Uri uri;
    Handler mHandler ;
    public GetFile(){
        BaseActivity.filePicker.setUriListener(new FilePicker.UriListener() {
            @Override
            public void UriGet(Uri uri) {
                close(uri);
            }
        });
    }
    public void open(Activity activity){
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                // process incoming messages here
                //super.handleMessage(msg);
                throw new RuntimeException();
            }
        };
        BaseActivity.filePicker.open(activity);
        try {
            Looper.getMainLooper().loop();
        }
        catch(RuntimeException e2)
        {
        }

    }
    public void close(Uri uri){
        setUri(uri);
        Message m = mHandler.obtainMessage();
        mHandler.sendMessage(m);
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
