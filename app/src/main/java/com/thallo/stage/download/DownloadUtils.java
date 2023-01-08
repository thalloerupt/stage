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
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.liulishuo.okdownload.DownloadContext;
import com.liulishuo.okdownload.DownloadContextListener;
import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.UnifiedListenerManager;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.thallo.stage.BaseActivity;
import com.thallo.stage.DataHolder;
import com.thallo.stage.database.download.Download;
import com.thallo.stage.database.download.DownloadViewModel;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class DownloadUtils {
    //上下文
    private Context mContext;
    private DownloadContext.Builder builder;
    private DownloadViewModel downloadViewModel;

    public DownloadUtils(Context mContext) {
        this.mContext = mContext;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            builder = new DownloadContext.QueueSet()
                    .setParentPathFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
                    .setPassIfAlreadyCompleted(true)
                    .setMinIntervalMillisCallbackProcess(30)
                    .commit();
        }
        downloadViewModel=new ViewModelProvider((ViewModelStoreOwner) mContext).get(DownloadViewModel.class);





    }
    public void startTask(String url){


        DownloadTask task=builder.bind(url);
        DownloadContext context = builder.build();
        Download download=new Download(task.getId());
        downloadViewModel.insertWords(download);
        UnifiedListenerManager manager = new UnifiedListenerManager();
        



        context.startOnParallel(new DownloadListener() {
            @Override
            public void taskStart(@NonNull DownloadTask task) {
                Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void connectTrialStart(@NonNull DownloadTask task, @NonNull Map<String, List<String>> requestHeaderFields) {


            }

            @Override
            public void connectTrialEnd(@NonNull DownloadTask task, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {


            }

            @Override
            public void downloadFromBeginning(@NonNull DownloadTask task, @NonNull BreakpointInfo info, @NonNull ResumeFailedCause cause) {


            }

            @Override
            public void downloadFromBreakpoint(@NonNull DownloadTask task, @NonNull BreakpointInfo info) {


            }

            @Override
            public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {


            }

            @Override
            public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {


            }

            @Override
            public void fetchStart(@NonNull DownloadTask task, int blockIndex, long contentLength) {


            }

            @Override
            public void fetchProgress(@NonNull DownloadTask task, int blockIndex, long increaseBytes) {

            }

            @Override
            public void fetchEnd(@NonNull DownloadTask task, int blockIndex, long contentLength) {

            }

            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause) {
                Toast.makeText(mContext, "下载完成", Toast.LENGTH_SHORT).show();

            }
        });

    }

}

