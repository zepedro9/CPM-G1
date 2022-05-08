package com.cpm.g1.theacmeelectronicsshop.httpService

import android.app.Activity
import android.util.Log
import com.cpm.g1.theacmeelectronicsshop.LoginActivity
import com.cpm.g1.theacmeelectronicsshop.MainActivity
import com.cpm.g1.theacmeelectronicsshop.readStream
import com.cpm.g1.theacmeelectronicsshop.ui.basket.CheckoutActivity

import java.io.*
import java.net.HttpURLConnection
import java.net.URL

fun sendRequest(
    act: Activity,
    uri: String,
    body: String = "",
    onSuccess: (Activity, String) -> Unit,
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
            println("here")
            urlConnection.doOutput = true
            urlConnection.requestMethod = type
            val outputStream = DataOutputStream(urlConnection.outputStream)
            // Send body
            outputStream.writeBytes(body)
            outputStream.flush()
            outputStream.close()
        }
        val responseCode = urlConnection.responseCode

        if (responseCode == 200) {
            val response = readStream(urlConnection.inputStream)
            onSuccess(act, response)
            println("[SUC] message sent!")
        }
        println("[RESPONSE CODE] " + responseCode)
    } catch (err: Exception) {
        Log.e("ERR",  err.toString());
    } finally {
        urlConnection?.disconnect()
    }
}

/**
 * Makes a signup request to the server.
 */
class SignUp(private val act: LoginActivity?, private val uri: String, val body: String) :
    Runnable {
    override fun run() {
        sendRequest(act as Activity, uri, body, act::changeToRegisterFragment)
    }
}

/**
 * Makes a signin request to the server.
 */
class Login(private val act: LoginActivity?, private val uri: String, private val body: String) :
    Runnable {
    override fun run() {
        sendRequest(act as Activity, uri, body, act::toMainActivity)
    }
}

/**
 * Makes a checkout request to the server.
 */
class Checkout(
    private val act: CheckoutActivity?,
    private val uri: String,
    private val body: String
) : Runnable {
    override fun run() {
        sendRequest(act as Activity, uri, body, act::generateQrCode)
    }
}

/**
 * Request the history of purchases.
 */
class GetHistory(
    private val act: MainActivity?,
    private val uri: String,
    private val body: String
) : Runnable {
    override fun run() {
        sendRequest(act as Activity, uri, body, act::updateHistoryAdapter)
    }
}

/**
 * Get's a list of products.
 */
class GetProductsList(
    private val act: MainActivity?,
    private val uri: String,
) : Runnable {
    override fun run() {
        sendRequest(act as Activity, uri, "", act::buildBasketProducts, "GET")
    }
}

