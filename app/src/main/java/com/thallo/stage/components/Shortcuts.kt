package com.thallo.stage.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View

class Shortcuts {
    /**
     * author:  stone
     * email:   aa86799@163.com
     *
     * 创建新的 Canvas + Bitmap对象，利用View的draw(canvas)
     */
    fun createBitmapFromView(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }

}