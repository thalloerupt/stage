package com.thallo.stage.components.dialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thallo.stage.R;

import org.mozilla.geckoview.GeckoSession;

public class myDialog extends AlertDialog {

    public myDialog(@NonNull Context context) {
        super(context);
        getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.bg_dialog));
    }
}
