package com.thallo.stage.components.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thallo.stage.R;
import com.thallo.stage.interfaces.confirm;

import org.mozilla.geckoview.GeckoResult;

public class ContentPermissionDialog  {
    androidx.appcompat.app.AlertDialog alertDialog;
    private confirm confirm ;

    public void showDialog(int per, String url,Context context){




        View view = LayoutInflater.from(context).inflate(R.layout.dia_permission,null);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context).setView(view);
        TextView textView=view.findViewById(R.id.perText);
        textView.setText("允许"+url+"获取您的"+"吗？");
        dialog.create();
        dialog.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confirm.getConfirm(GeckoResult.deny());
            }
        });
        dialog.setPositiveButton("允许", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confirm.getConfirm(GeckoResult.allow());
            }
        });
        alertDialog = dialog.create();
        alertDialog.show();

    }



    public void setConfirm(com.thallo.stage.interfaces.confirm confirm) {
        this.confirm = confirm;
    }
}
