package com.cpm.g1.theacmeelectronicsshop.ui.basket

import android.app.Activity
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.cpm.g1.theacmeelectronicsshop.ConfigHTTP
import com.cpm.g1.theacmeelectronicsshop.Cryptography
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.Basket
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.BasketItem
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
        val total = intent.getFloatExtra("total", 0F)
        println(total)
        // Send basket to server
        sendCheckoutRequest(total)
    }

    private fun sendCheckoutRequest(total: Float){
        try{
            val basket = getBasket(total)
            println("BASKET " + basket);
            Thread(Checkout(this, CHECKOUT_ADDRESS , basket)).start()
        } catch(err: Exception){
            Toast.makeText(applicationContext, err.message, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun getBasket(total: Float) : String {
        val uuid = getUserUUID(applicationContext)
        val cursor: Cursor = dbHelper.getBasket(getUserUUID(this))
        val products = mutableListOf<ItemQuantity>()

        cursor.moveToFirst()
        while(!cursor.isAfterLast) {
            products.add(ItemQuantity(cursor.getLong(0), cursor.getInt(1)))
            cursor.moveToNext()
        }
        cursor.close()

        if(products.isEmpty())
            throw Exception("Empty basket")
        val basket = Basket(uuid, products.toList(), total.toString())
        val basketJson = Gson().toJson(basket)
        val signature = Cryptography().signContent(basketJson)
        return Gson().toJson(SignedBasket(basket, signature))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateQrCode(success: Boolean, response: String) {
        val jsonResponse = JSONObject(response)
        val basketUUID = Cryptography().decrypt(jsonResponse.getString("message"))
        println("BASKET UUID = " + basketUUID)

        val details = QRCodeFragment.newInstance(basketUUID)
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, details)
            .commit()
    }
}