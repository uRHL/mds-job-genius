package com.canonicalexamples.jobgenius.util

import android.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import java.net.URL


class LoadImageURL(): AsyncTask<String?, Void?, Bitmap?>() {

     override fun doInBackground(vararg params: String?): Bitmap? {
        val url = params[0]
        var bmp: Bitmap? = null
        try {
            val url = URL(url)
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: Exception) {
            Log.e("Error", e.message!!)
            e.printStackTrace()
        }
        return bmp
    }

    override fun onPostExecute(result: Bitmap?) {
//        if (result != null) imgURL!!.setImageBitmap(result)
//        pbbar!!.visibility = View.GONE
//        imgURL!!.visibility = View.VISIBLE
    }

}