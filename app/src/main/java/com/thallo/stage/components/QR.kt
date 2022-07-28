package com.thallo.stage.components

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.FragmentManager
import mozilla.components.feature.qr.QrFeature
import mozilla.components.feature.qr.QrFragment

class QR(activity:Activity,context:Context,fragmentManager:FragmentManager){
    var result1: String? = null
    var qrFeature=QrFeature(
        context,
        fragmentManager,
        onNeedToRequestPermissions = { permissions ->
            requestPermissions(activity, permissions,1
            )
        },
        onScanResult = { result ->
            // result is a String (e.g. a URL) returned by the QR scanner.
             result1 = result;


        }

    )
    fun QrScan(){
        qrFeature.scan()

    }



}

