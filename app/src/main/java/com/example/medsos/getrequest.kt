package com.example.medsos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

suspend fun getRequest(connectionString: String, token: String? ): Result<String>{
    return withContext(Dispatchers.IO){
        try {
            var url = URL(connectionString);
            var redirect = false
            var result = StringBuilder()

            do {
                var urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.instanceFollowRedirects = false
                urlConnection.requestMethod = "GET"
                urlConnection.setRequestProperty("Content-Type", "application/json")
                urlConnection.setRequestProperty("Accept", "application/json")
                if(token != null){
                    urlConnection.setRequestProperty("Authorization", "Bearer $token")
                }
                urlConnection.connect()

                var responseCode = urlConnection.responseCode

                if(responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP)
                {
                    redirect = true
                    var newUrl = urlConnection.getHeaderField("Location")
                    url = URL(newUrl)

                }else{
                    redirect = false

                    var inputStream = if(responseCode in 200 .. 299){
                        urlConnection.inputStream
                    }else{
                        urlConnection.errorStream
                    }

                    var reader = BufferedReader(InputStreamReader(inputStream))
                    var line : String?
                    while (reader.readLine().also { line = it } != null)
                    {
                        result.append(line)
                    }
                    reader.close()

                    if(responseCode !in 200 .. 299){
                        return@withContext Result.failure(Exception("Failed with response code : $responseCode \n $result"))
                    }

                }
                urlConnection.disconnect()
            }while (redirect)
            Result.success(result.toString())
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}