package com.cpm.g1.printer.httpService

import android.util.Log
import com.cpm.g1.printer.MainActivity
import com.cpm.g1.printer.readStream
import org.json.JSONArray
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URL

fun sendRequest(
    uri: String,
    body: String = "",
    callback: (Boolean, String) -> Unit,
    type: String = "POST"
) {
    val url: URL
    var urlConnection: HttpURLConnection? = null
    try {
        url = URL(uri)

        urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.doInput = true
        urlConnection.setRequestProperty("Content-Type", "application/json")
        urlConnection.useCaches = false

        if (type != "GET") {
            urlConnection.doOutput = true
            urlConnection.requestMethod = type
            val outputStream = DataOutputStream(urlConnection.outputStream)
            // Send body
            outputStream.writeBytes(body)
            outputStream.flush()
            outputStream.close()
        }
        val responseCode = urlConnection.responseCode

        val success = urlConnection.responseCode == 200
        val response = readStream(urlConnection.inputStream)
        callback(success, response)

        println("[RESPONSE CODE] $responseCode")
    } catch(err: FileNotFoundException){
        if(urlConnection?.errorStream != null){
            val response = readStream(urlConnection.errorStream)
            callback(false, response)
        }
        Log.e("ERR",  err.toString());
    } catch (err: Exception) {
        val response = "{\"message\": \"Unexpected error occurred\"}"
        Log.e("ERR",  err.toString());
        callback(false, response)
    } finally {
        urlConnection?.disconnect()
    }
}

/**
 * Request the receipt of the purchase.
 */
class GetReceipt(
    private val act: MainActivity,
    private val uri: String,
    private val body: String
) : Runnable {
    override fun run() {
        sendRequest(uri, body, act::changeToReceiptFragment)
    }
}

