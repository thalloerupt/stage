package com.thallo.stage.components;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thallo.stage.BaseActivity;
import com.thallo.stage.R;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class Qr {
    public void show(BaseActivity baseActivity){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(baseActivity, R.style.BottomSheetDialog);
        View popView= LayoutInflater.from(baseActivity).inflate(R.layout.qr_layout,null );

        ZXingView zXingView=popView.findViewById(R.id.zxingview);
        ImageView imageView=popView.findViewById(R.id.qrFlash);

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                zXingView.onDestroy();


            }
        });


        new Thread(){
            @Override
            public void run() {
                super.run();
                zXingView.startCamera();
                zXingView.startSpot();
            }
        }.start();

        imageView.setOnClickListener(new View.OnClickListener() {
            boolean a=false;
            @Override
            public void onClick(View view) {
                if (!a)
                {
                    imageView.setImageResource(R.drawable.ic_bulb);
                    a=true;
                    zXingView.openFlashlight();
                }
                else {imageView.setImageResource(R.drawable.ic_bulb_off);a=false;zXingView.closeFlashlight();}
            }
        });
        zXingView.setDelegate(new QRCodeView.Delegate() {
            @Override
            public void onScanQRCodeSuccess(String result) {
                baseActivity.getWebSessionViewModel().getSession().loadUri(result);
                bottomSheetDialog.dismiss();

            }

            @Override
            public void onCameraAmbientBrightnessChanged(boolean isDark) {

            }

            @Override
            public void onScanQRCodeOpenCameraError() {

            }
        });

        bottomSheetDialog.setContentView(popView);
        bottomSheetDialog.show();

    }
}
