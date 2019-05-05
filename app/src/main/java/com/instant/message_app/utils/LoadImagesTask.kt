package com.instant.message_app.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class LoadImagesTask : AsyncTask<String, Void, Bitmap> {
    private val imageView: ImageView

    constructor(imageView: ImageView){
        this.imageView = imageView
    }

    override fun doInBackground(vararg params: String): Bitmap? {
        var imageUrl: URL? = null
        var bitmap: Bitmap? = null
        var inputStream: InputStream? = null
        try {
            imageUrl = URL(params[0])
            val conn = imageUrl.openConnection() as HttpURLConnection
            conn.doInput = true
            conn.connect()
            inputStream = conn.inputStream
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream!!.close()

        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bitmap
    }

    override fun onPostExecute(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }
}