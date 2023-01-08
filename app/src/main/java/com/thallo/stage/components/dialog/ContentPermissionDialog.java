package com.thallo.stage.components.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thallo.stage.R;
import com.thallo.stage.interfaces.confirm;

import org.mozilla.geckoview.GeckoResult;

public class ContentPermissionDialog extends MaterialAlertDialogBuilder {
    public ContentPermissionDialog(@NonNull Context context) {
        super(context);
    }
}
