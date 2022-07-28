package com.thallo.stage.dialog;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thallo.stage.R;
import com.thallo.stage.databinding.DiaInstallBinding;

import org.mozilla.geckoview.WebExtension;

import java.lang.reflect.Array;
import java.util.Arrays;

public class PermissionDialog extends MaterialAlertDialogBuilder
{
    int dialogResult;
    Handler mHandler ;
    DiaInstallBinding binding;


    public PermissionDialog(Activity context, WebExtension webExtension)
    {

        super(context);
        onCreate(webExtension);

    }
    public int getDialogResult()
    {
        return dialogResult;
    }
    public void setDialogResult(int dialogResult)
    {
        this.dialogResult = dialogResult;
    }
    /** Called when the activity is first created. */

    public void onCreate(WebExtension webExtension) {
        binding=DiaInstallBinding.inflate(LayoutInflater.from(getContext()));
        setTitle("要添加"+webExtension.metaData.name+"吗？");
        setMessage("需要以下权限：");
        setIcon(R.drawable.ic_addons);
        binding.diaPer.setText(Arrays.toString(webExtension.metaData.permissions)
                .replaceAll("\\[", "• ")
                .replaceAll(",","\n•")
                .replaceAll("\\]","")
                .replaceAll(getContext().getString(R.string.per_tabs), getContext().getString(R.string.per_tabs_cn))
                .replaceAll(getContext().getString(R.string.per_bookmarks), getContext().getString(R.string.per_bookmarks_cn))
                .replaceAll(getContext().getString(R.string.per_clipboardRead), getContext().getString(R.string.per_clipboardRead_cn))
                .replaceAll(getContext().getString(R.string.per_browserSettings), getContext().getString(R.string.per_browserSettings_cn))
                .replaceAll(getContext().getString(R.string.per_browsingData), getContext().getString(R.string.per_browsingData_cn))
                .replaceAll(getContext().getString(R.string.per_downloads), getContext().getString(R.string.per_downloads_cn))
                .replaceAll(getContext().getString(R.string.per_geolocation), getContext().getString(R.string.per_geolocation_cn))
                .replaceAll(getContext().getString(R.string.per_notifications), getContext().getString(R.string.per_notifications_cn))
        );


        setView(binding.getRoot());
        setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                endDialog(0);
            }
        });
        setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                endDialog(1);
            }
        });


    }

    public void endDialog(int result)
    {
        setDialogResult(result);
        Message m = mHandler.obtainMessage();
        mHandler.sendMessage(m);
    }

    @SuppressLint("HandlerLeak")
    public int showDialog()
    {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                // process incoming messages here
                //super.handleMessage(msg);
                throw new RuntimeException();
            }
        };
        super.show();
        try {
            Looper.getMainLooper().loop();
        }
        catch(RuntimeException e2)
        {
        }
        return dialogResult;
    }

}