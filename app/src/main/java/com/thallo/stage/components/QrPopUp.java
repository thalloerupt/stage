package com.thallo.stage.components;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thallo.stage.R;

public class QrPopUp {
    public void show(Context context,Activity activity, FragmentManager fragmentManager,int id){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogStyle);
        View popView= LayoutInflater.from(context).inflate(R.layout.popup_qr,null );
        bottomSheetDialog.setContentView(popView);
        View layout=popView.findViewById(R.id.qrView);
        QR qr=new QR();
        qr.QrScan(activity,fragmentManager,id);
       // bottomSheetDialog.show();
    }
}
