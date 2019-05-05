package com.instant.message_app.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

class DisplayUtil {
    companion object {
        fun getWidth(context: Context): Int {
            return getDisplayMetrics(context)!!.widthPixels
        }

        fun getHeight(context: Context): Int {
            return getDisplayMetrics(context)!!.heightPixels
        }

        private fun getDisplayMetrics(context: Context): DisplayMetrics? {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager ?: return null
            val outMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getRealMetrics(outMetrics)
            return outMetrics
        }
    }
}