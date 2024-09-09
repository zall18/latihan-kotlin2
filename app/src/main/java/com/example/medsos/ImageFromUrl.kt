package com.example.medsos

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

suspend fun getImageFromUrl(connectionString: String): Bitmap? {
    return withContext(Dispatchers.IO){
        Log.d("image", "getImageFromUrl: testimage")
        try {
            var url = URL(connectionString)
            var urlConnection = url.openConnection() as HttpURLConnection
            Log.d("url image", "getImageFromUrl: " + urlConnection.url)
            urlConnection.doInput = true
            urlConnection.connect()
            var inputStream: InputStream = urlConnection.inputStream
            BitmapFactory.decodeStream(inputStream)
        }catch (e: Exception){
            Log.d("image", "getImageFromUrl: testimageerror")

            e.printStackTrace()
            null
        }
    }
}