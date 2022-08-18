package com.thallo.stage.download;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.thallo.stage.BaseActivity;
import com.thallo.stage.DataHolder;
import com.thallo.stage.database.download.Download;
import com.thallo.stage.database.download.DownloadViewModel;

import org.jetbrains.annotations.Nullable;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class DownloadUtils {
    //下载器
    private DownloadManager downloadManager;
    //上下文
    private Context mContext;
    //下载的ID
    private long downloadId;
    String fileName;
    URL url;
    DownloadViewModel downloadViewModel;
    DownloadManager.Query downloadQuery;
    long id;
    Cursor cursor;
    int status;
    public  DownloadUtils(Context context,@Nullable long id){
        this.mContext = context;
        this.id=id;
        downloadViewModel=new ViewModelProvider((ViewModelStoreOwner) mContext).get(DownloadViewModel.class);
        downloadQuery = new DownloadManager.Query();
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadQuery.setFilterById(id);
        cursor = downloadManager.query(downloadQuery);
    }



    //下载apk
    public void open(String url1) {
        fileName=URLUtil.guessFileName(url1,null,null);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url1));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedOverRoaming(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        final DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);
        Download download=new Download(downloadId);
        downloadViewModel.insertWords(download);
        // 监听下载成功状态
        mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }
    @SuppressLint("Range")

    public String queryName() {
        String fileName1 = null;
        if (cursor != null && cursor.moveToFirst()) {
            int fileUri = cursor.getColumnIndex(DownloadManager.COLUMN_URI);
            String fu = cursor.getString(fileUri);
            fileName1=URLUtil.guessFileName(fu,null,null);




        }
        return fileName1;
    }
    public String queryUrl() {
        String fileUrl = null;
        if (cursor != null && cursor.moveToFirst()) {
            int fileUri = cursor.getColumnIndex(DownloadManager.COLUMN_URI);
            fileUrl = cursor.getString(fileUri);


        }
        return fileUrl;
    }
    public int querySize() {
        int i=0;
        if (cursor != null && cursor.moveToFirst()) {
            int totalSizeBytesIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            int bytesDownloadSoFarIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            // 下载的文件总大小
            int totalSizeBytes = cursor.getInt(totalSizeBytesIndex);
            i=totalSizeBytes;

        }
        return i;
    }
    public void close()
    {
        cursor.close();
    }

    //广播接受者，接收下载状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                //检查下载状态
                checkDownloadStatus(downloadId );
            }
        }

        //检查下载状态
        private void checkDownloadStatus(long downloadId ) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId );//筛选下载任务，传入任务ID，可变参数
            Cursor c = downloadManager.query(query);
            if (c.moveToFirst()) {
                @SuppressLint("Range") int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    case DownloadManager.STATUS_PAUSED:
                        status=0;
                    case DownloadManager.STATUS_PENDING:
                    case DownloadManager.STATUS_RUNNING:
                        status=2;
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        status=1;
                        break;
                    case DownloadManager.STATUS_FAILED:
                        status=-1;
                        break;
                }
            }
        }
    };
}

