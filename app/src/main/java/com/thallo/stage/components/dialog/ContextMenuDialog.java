package com.thallo.stage.components.dialog;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thallo.stage.BaseActivity;
import com.thallo.stage.R;
import com.thallo.stage.databinding.DiaContextmenuBinding;
import com.thallo.stage.download.DownloadUtils;

import org.mozilla.geckoview.GeckoSession;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

public class ContextMenuDialog extends AlertDialog {
    Context context;
    DiaContextmenuBinding binding;
    String type;
    public ContextMenuDialog(@NonNull Context context, GeckoSession.ContentDelegate.ContextElement element) {
        super(context);
        this.context=context;
        binding=DiaContextmenuBinding.inflate(LayoutInflater.from(context));
        Glide.with(context).load(element.srcUri).into(binding.imageView19);
        binding.iconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadUtils downloadUtils=new DownloadUtils(context);
                try {
                    downloadUtils.startTask(element.srcUri);
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        binding.button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyToClipboard(context,element.srcUri);
                dismiss();
            }
        });
        if(element.srcUri==null) binding.button7.setVisibility(View.GONE);
        binding.button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.setAction(Intent.ACTION_VIEW);

                if(element.type==element.TYPE_IMAGE) type = "image/*";
                else if (element.type==element.TYPE_VIDEO) type = "video/*";

                Uri uri = Uri.parse(element.srcUri);

                intent.setDataAndType(uri,type);

                context.startActivity(intent);
            }
        });
        setView(binding.getRoot());

    }
    public static void copyToClipboard(Context context, String content) {
        // ??? API11 ?????? android ???????????? android.content.ClipboardManager
        // ???????????????????????????????????????????????? android.text.ClipboardManager??????????????? deprecated????????????????????????
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // ??????????????????????????????????????????
        cm.setText(content);
        Toast.makeText(context, "?????????????????????", Toast.LENGTH_SHORT).show();
    }
    public void open(){
        getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.bg_dialog));
        show();
    }
}
