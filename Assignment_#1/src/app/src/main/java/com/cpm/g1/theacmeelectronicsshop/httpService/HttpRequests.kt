package com.cpm.g1.theacmeelectronicsshop.httpService

import android.os.Build
import androidx.annotation.RequiresApi
import android.util.Log
import com.cpm.g1.theacmeelectronicsshop.ui.auth.LoginActivity
import com.cpm.g1.theacmeelectronicsshop.ui.MainActivity
import com.cpm.g1.theacmeelectronicsshop.readStream
import com.cpm.g1.theacmeelectronicsshop.ui.basket.CheckoutActivity
import com.cpm.g1.theacmeelectronicsshop.ui.basketHistory.ProductTransactionActivity

import java.io.*
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
        callback(false, response)
        Log.e("ERR",  err.toString());
    } finally {
        urlConnection?.disconnect()
    }
}

/**
 * Makes a signup request to the server.
 */
class SignUp(private val act: LoginActivity, private val uri: String, val body: String) :
    Runnable {
    override fun run() {
        sendRequest( uri, body, act::changeToRegisterFragment)
    }
}

/**
 * Makes a signin request to the server.
 */
class Login(private val act: LoginActivity, private val uri: String, private val body: String) :
    Runnable {
    override fun run() {
        sendRequest(uri, body, act::toMainActivity)
    }
}

/**
 * Makes a checkout request to the server.
 */
class Checkout(private val act: CheckoutActivity, private val uri: String, val basket: String) : Runnable {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun run() {
        sendRequest(uri, basket, act::generateQrCode)
    }
}

/**
 * Request details about basket products to the server.
 */
class InitBasket(private val uri: String, private val callback: (Boolean, String) -> Unit) : Runnable {
    override fun run() {
        sendRequest(uri, callback=callback, type="GET")
    }
}
/**
 * Request the history of purchases.
 */
class GetHistory(
    private val act: MainActivity,
    private val uri: String,
    private val body: String
) : Runnable {
    override fun run() {
        sendRequest(uri, body, act::updateHistoryAdapter)
    }
}

/**
 * Get's a list of products.
 */
class GetProductsList(
    private val act: ProductTransactionActivity,
    private val uri: String,
) : Runnable {
    override fun run() {
        sendRequest(uri,  callback= act::buildBasketProducts, type= "GET")
    }
}

/**
 * Verify if the user exists
 */
class UserExists(private val act: LoginActivity, private val uri: String, val userUUID: String) : Runnable {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun run() {
        sendRequest(uri, userUUID, act::loginOrLogout)
    }
}