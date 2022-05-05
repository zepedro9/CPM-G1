package com.cpm.g1.theacmeelectronicsshop.httpService

import android.app.Activity
import com.cpm.g1.theacmeelectronicsshop.LoginActivity
import com.cpm.g1.theacmeelectronicsshop.readStream
import org.json.JSONObject

import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class Auth {
    lateinit var uuid: String

    fun sendPostRequest(
        act: Activity,
        uri: String,
        body: String,
        onSuccess: (Activity, JSONObject) -> Unit
    ) {
        val url: URL
        var urlConnection: HttpURLConnection? = null
        try {
            url = URL(uri)

            // Configure POST request
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "POST"
            urlConnection.setRequestProperty("Content-Type", "application/json")
            urlConnection.doOutput = true
            urlConnection.doInput = true
            urlConnection.useCaches = false

            val outputStream = DataOutputStream(urlConnection.outputStream)

            // Send request
            outputStream.writeBytes(body)
            outputStream.flush()
            outputStream.close()

            val responseCode = urlConnection.responseCode

            if (responseCode == 200) {
                val response = readStream(urlConnection.inputStream)
                val jsonResponse = JSONObject(response)
                onSuccess(act, jsonResponse)
            }
        } catch (err: Exception) {
            println(err);
        } finally {
            urlConnection?.disconnect()
        }
    }

    /**
     * Makes a signup request to the server.
     */
    inner class SignUp(private val act: LoginActivity?, private val uri: String, val body: String) :
        Runnable {
        override fun run() {
            sendPostRequest(act as Activity, uri, body, act::changeToRegisterFragment)
        }
    }

    inner class Login(val act: LoginActivity?, val uri: String, val body: String) : Runnable {
        override fun run() {
            sendPostRequest(act as Activity, uri, body, act::toMainActivity)
        }
    }


}
