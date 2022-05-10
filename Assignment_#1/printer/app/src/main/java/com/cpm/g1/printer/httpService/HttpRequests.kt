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
        //callback(false, response)
        Log.e("ERR",  err.toString())

        // TODO: Remove fake json and test for real

        // FAKE JSON RESPONSE
        val rootObject = JSONObject()
        rootObject.put("message","Authorized")
        rootObject.put("user","Carlos")
        rootObject.put("nif","123456789")

        val basket = JSONArray()

        val basketItem1 = JSONObject()
        basketItem1.put("_id","627a8f91223863688597b2b0")

        val productsSmall = JSONArray()

        val smallProductItem1 = JSONObject()
        smallProductItem1.put("id","61234567890")
        smallProductItem1.put("quantity","2")
        smallProductItem1.put("_id","627a8f91223863688597b2b1")

        productsSmall.put(smallProductItem1)

        val smallProductItem2 = JSONObject()
        smallProductItem2.put("id","61234567891")
        smallProductItem2.put("quantity","3")
        smallProductItem2.put("_id","627a8f91223863688597b2b1")

        productsSmall.put(smallProductItem2)

        basketItem1.put("products", productsSmall)
        basketItem1.put("total","965.0")
        basketItem1.put("date","2022-05-10")
        basketItem1.put("hour","16:15")
        basketItem1.put("token","5f3ba7f0-d07c-11ec-9b21-17c2458e7544")
        basketItem1.put("__v","0")

        basket.put(basketItem1)

        rootObject.put("basket", basket)

        val products = JSONArray()

        val productItem1 = JSONObject()
        productItem1.put("id","61234567890")
        productItem1.put("name","Keyboard")
        productItem1.put("brand","Apple")
        productItem1.put("price","109.0")
        productItem1.put("description","description")
        productItem1.put("image_url","url")

        products.put(productItem1)

        val productItem2 = JSONObject()
        productItem2.put("id","61234567891")
        productItem2.put("name","Monitor")
        productItem2.put("brand","Apple")
        productItem2.put("price","249.0")
        productItem2.put("description","description")
        productItem2.put("image_url","url")

        products.put(productItem2)

        rootObject.put("products", products)
        // FAKE JSON RESPONSE

        callback(true, rootObject.toString())
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

