package com.cpm.g1.theacmeelectronicsshop.httpService

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import android.util.Log
import com.cpm.g1.theacmeelectronicsshop.LoginActivity
import com.cpm.g1.theacmeelectronicsshop.MainActivity
import com.cpm.g1.theacmeelectronicsshop.readStream
import com.cpm.g1.theacmeelectronicsshop.ui.basket.CheckoutActivity
import com.google.gson.JsonObject
import org.json.JSONObject
import com.cpm.g1.theacmeelectronicsshop.ui.basketHistory.ProductTransactionActivity

import java.io.*
import java.net.HttpURLConnection
import java.net.URL

fun sendRequest(
// TODO: combine functions and show toast when errors occur or something
// If response code is not 200 read the error message from the server(must add message to all server requests)
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

fun sendGetRequest(
    uri: String,
    callback: (Boolean, JSONObject) -> Unit
) {
    val url: URL
    var urlConnection: HttpURLConnection? = null
    try {
        url = URL(uri)

        // Configure POST request
        urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "GET"
        urlConnection.setRequestProperty("Content-Type", "application/json")
        urlConnection.doInput = true
        urlConnection.useCaches = false

        // Send request
        val success = urlConnection.responseCode == 200
        val response = readStream(urlConnection.inputStream)
        val jsonResponse = JSONObject(response)
        callback(success, jsonResponse)

    } catch (err: Exception) {
        callback(false, JSONObject(hashMapOf("message" to "Error connecting to server: ${err.message}").toString()))
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
class Checkout(private val act: CheckoutActivity?, private val uri: String, val basket: String) : Runnable {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun run() {
        sendRequest(act as Activity, uri, basket, act::generateQrCode)
    }
}

/**
 * Request details about basket products to the server.
 */
class InitBasket(private val uri: String, private val callback: (Boolean, JSONObject) -> Unit) : Runnable {
    override fun run() {
        sendGetRequest(uri, callback)
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
    private val act: ProductTransactionActivity?,
    private val uri: String,
) : Runnable {
    override fun run() {
        sendRequest(act as Activity, uri, "", act::buildBasketProducts, "GET")
    }
}

