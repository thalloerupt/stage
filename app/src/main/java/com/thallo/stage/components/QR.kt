package com.thallo.stage.components

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Context
import android.graphics.Camera
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.FragmentManager
import mozilla.components.feature.qr.QrFeature
import mozilla.components.feature.qr.QrFragment

class QR(){

    fun QrScan(context:Activity,fragmentManager:FragmentManager,id:Int){
        requestPermissions(context, arrayOf(CAMERA),1)
        var scanCompleteListener:QrFragment.OnScanCompleteListener=object:QrFragment.OnScanCompleteListener{
            override fun onScanComplete(result: String) {
                TODO("Not yet implemented")
            }
        }
        fragmentManager.beginTransaction()
            .add(id, QrFragment.newInstance(scanCompleteListener, 1),
                "MOZAC_QR_FRAGMENT")
            .commit()

    }



}

