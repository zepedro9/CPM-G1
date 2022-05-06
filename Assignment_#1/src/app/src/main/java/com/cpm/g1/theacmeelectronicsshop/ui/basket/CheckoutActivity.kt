package com.cpm.g1.theacmeelectronicsshop.ui.basket

import android.app.Activity
import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cpm.g1.theacmeelectronicsshop.ConfigHTTP
import com.cpm.g1.theacmeelectronicsshop.Cryptography
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.Basket
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.ItemQuantity
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.SignedBasket
import com.cpm.g1.theacmeelectronicsshop.getUserUUID
import com.cpm.g1.theacmeelectronicsshop.httpService.Checkout
import com.cpm.g1.theacmeelectronicsshop.ui.BasketHelper
import com.google.gson.Gson
import org.json.JSONObject

const val CHECKOUT_ADDRESS: String = "http://" + ConfigHTTP.BASE_ADDRESS + ":3000/api/basket/checkout"

class CheckoutActivity: AppCompatActivity() {
    private val dbHelper by lazy { BasketHelper(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Send basket to server
        sendCheckoutRequest()
    }

    private fun sendCheckoutRequest(){
        /*try{
            val basket = getBasket()
            Thread(Checkout(this, CHECKOUT_ADDRESS , basket)).start()
        } catch(err: Exception){
            println(err.toString())
        }*/
    }

    /*private fun getBasket() : String {
        val uuid = getUserUUID(applicationContext) ?: throw Exception("Missing uuid")
        val cursor: Cursor = dbHelper.getBasketProducts()
        val total = dbHelper.getBasketTotal()
        val products = mutableListOf<ItemQuantity>()

        cursor.moveToFirst()
        while(!cursor.isAfterLast) {
            products.add(ItemQuantity(cursor.getInt(0), cursor.getInt(1)))
            cursor.moveToNext()
        }
        cursor.close()

        val basket = Basket(uuid, products.toList(), total.toString())
        val basketJson = Gson().toJson(basket)
        println(basketJson)
        val signature = Cryptography().signContent(basketJson)
        return Gson().toJson(SignedBasket(basket, signature))
    }*/

    fun generateQrCode(act: Activity, jsonResponse: JSONObject) {
        // TODO: fragment with QRCode maybe?
        println("TODO: GENERATE QR CODE")
    }
}