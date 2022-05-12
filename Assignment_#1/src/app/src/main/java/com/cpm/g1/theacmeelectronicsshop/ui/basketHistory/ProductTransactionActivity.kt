package com.cpm.g1.theacmeelectronicsshop.ui.basketHistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.Basket
import org.json.JSONObject

class ProductTransactionActivity : AppCompatActivity() {

    private val historyProducts: ArrayList<JSONObject> = ArrayList()
    val adapterProducts by lazy { ProductHistoryAdapter(this, historyProducts) }
    lateinit var basket: Basket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeToBasketHistoryProducts()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    /**
     * Changes the view to a fragment that shows the items inside the history.
     */
    private fun changeToBasketHistoryProducts(){
        val basketHistoryProducts = BasketHistoryProducts.newInstance(intent.extras!!)
        val fragmentManager = this.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(android.R.id.content, basketHistoryProducts)
        fragmentTransaction.commit()

    }


    fun buildBasketProducts(success: Boolean, response: String) {
        val jsonResponse = JSONObject(response)
        val products = jsonResponse.getJSONArray("products")
        historyProducts.clear()
        for (i in 0 until products.length()) {
            val product = products.getJSONObject(i)
            historyProducts.add(product)
        }
        runOnUiThread {
            adapterProducts.notifyDataSetChanged()
        }
    }

}