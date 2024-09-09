package com.example.medsos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

suspend fun postRequest(connectionString: String, jsonObject: JSONObject, token: String?): Result<String>{
    return withContext(Dispatchers.IO){
        try {

            var url = URL(connectionString)
            var redirect = false
            var response = StringBuilder()

            do {
                var urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.instanceFollowRedirects = false
                urlConnection.requestMethod = "POST"
                urlConnection.setRequestProperty("Content-Type", "application/json")
                urlConnection.setRequestProperty("Accept", "application/json")
                if (token != null){
                    urlConnection.setRequestProperty("Authorization", "Bearer $token")
                }
                urlConnection.doOutput = true

                var outputStreamWriter = OutputStreamWriter(urlConnection.outputStream)
                outputStreamWriter.write(jsonObject.toString())
                outputStreamWriter.flush()
                outputStreamWriter.close()

                var responseCode = urlConnection.responseCode

                if(responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP){
                    var newurl = urlConnection.getHeaderField("Location")
                    url = URL(newurl)
                    redirect = true
                }else{
                    redirect = false

                    var inputStream = if(responseCode in 200 .. 299){
                        urlConnection.inputStream
                    }else{
                        urlConnection.errorStream
                    }

                    var reader = BufferedReader(InputStreamReader(inputStream))
                    var line : String?
                    while (reader.readLine().also { line = it } != null){
                        response.append(line)
                    }
                    reader.close()

                    if(responseCode !in 200 .. 299){
                        return@withContext Result.failure(Exception("Failed with response code: $responseCode \n $response"))
                    }
                }
                urlConnection.disconnect()
            }while (redirect)
            Result.success(response.toString())
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}