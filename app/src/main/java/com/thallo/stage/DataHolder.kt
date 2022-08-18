package com.thallo.stage

import android.app.Application
import android.widget.Toast

class DataHolder: Application() {
    var ids: Array<Long> = emptyArray<Long>()
    fun set(id:Long){
        ids.plus(id)
        Toast.makeText(this,"ok",Toast.LENGTH_SHORT).show()
    }
}