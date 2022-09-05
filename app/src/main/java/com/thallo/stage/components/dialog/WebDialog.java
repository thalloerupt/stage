package com.thallo.stage.components.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thallo.stage.databinding.DiaWebBinding;

import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;

public class WebDialog extends AlertDialog {
    DiaWebBinding binding;
    GeckoSession session;
    public WebDialog(@NonNull Context context, Boolean a) {
        super(context);
        binding=DiaWebBinding.inflate(LayoutInflater.from(context));
        session=new GeckoSession();
        if(a==true) session.loadUri("https://static-6e7c68d2-83dd-40b0-9f09-0150b6b22138.bspapp.com/");
        else session.loadUri("https://static-6e7c68d2-83dd-40b0-9f09-0150b6b22138.bspapp.com/Stage%E9%9A%90%E7%A7%81%E6%94%BF%E7%AD%96.html");
        session.open(GeckoRuntime.getDefault(context));
        binding.DiaGeckoview.setSession(session);
        setView(binding.getRoot());
        setButton(BUTTON_POSITIVE, "同意", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        setButton(BUTTON_NEGATIVE, "不同意", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Activity context1 = (Activity) context;
                context1.finish();

            }
        });

    }
}
