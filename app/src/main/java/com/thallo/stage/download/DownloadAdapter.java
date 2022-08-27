package com.thallo.stage.download;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.thallo.stage.R;
import com.thallo.stage.database.download.Download;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadAdapterHolder> {
    List<Download> allDownload = new ArrayList<>();
    TextView textView,url,size;
    ProgressBar progressBar;
    DownloadUtils downloadUtils;
    DownloadManager downloadManager;
    ImageView delete;
    Intent intent;


    @NonNull
    @Override
    public DownloadAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.download_item,parent,false);
        downloadManager = (DownloadManager) parent.getContext().getSystemService(Context.DOWNLOAD_SERVICE);

        return new DownloadAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadAdapter.DownloadAdapterHolder holder, int position) {
        Download download= allDownload.get(position);
        downloadUtils=new DownloadUtils(holder.itemView.getContext(),download.getIds());
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), downloadUtils.queryName());
        textView.setText(downloadUtils.queryName());
        url.setText(downloadUtils.queryUrl());
        size.setText(downloadUtils.querySize()/1024/1024+"MB");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (file.exists())
                    open(holder.itemView.getContext(),file);
                else Toast.makeText(holder.itemView.getContext(), "文件不存在", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return allDownload.size();
    }
    public void setAllDownload(List<Download> allDownload) {
        this.allDownload = allDownload;
    }
    class DownloadAdapterHolder extends RecyclerView.ViewHolder{
        public DownloadAdapterHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.downloadName);
            url=itemView.findViewById(R.id.downloadUrl);
            size=itemView.findViewById(R.id.downloadSize);
            progressBar=itemView.findViewById(R.id.downloadProgress);
            delete=itemView.findViewById(R.id.downloadDelete);

        }
    }

    public void open(Context context,File file){
        ContentResolver resolver = context.getContentResolver();
        intent = new Intent(Intent.ACTION_VIEW);
        //如果设置，这个活动将成为这个历史堆栈上的新任务的开始
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri apkUri = FileProvider.getUriForFile(context, "com.thallo.stage.fileprovider", file);
        //判读版本是否在7.0以上
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(apkUri, resolver.getType(apkUri));
        context.startActivity(intent);
    }


}
