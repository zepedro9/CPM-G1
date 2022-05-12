package com.cpm.g1.theacmeelectronicsshop.ui

import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.Basket
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.ItemQuantity
import com.cpm.g1.theacmeelectronicsshop.databinding.ActivityAppBinding
import com.cpm.g1.theacmeelectronicsshop.ui.auth.AuthenticatedUserActivity
import com.cpm.g1.theacmeelectronicsshop.ui.basketHistory.BasketHistoryAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AuthenticatedUserActivity() {
    // History fragment variables
    val historyBasket: ArrayList<Basket> = ArrayList()
    val adapterBasket by lazy { BasketHistoryAdapter(this, historyBasket) }



    // Other variables
    private lateinit var binding: ActivityAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Visualize View
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bottom Navigation View
        val navView: BottomNavigationView = binding.navView

        // Controls the fragments change on click
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_scan, R.id.navigation_basket, R.id.navigation_history_basket)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0){
            fragmentManager.popBackStack()

        } else {
            super.onBackPressed()
        }
    }
    /**
     * Updates the history fragment content in the BasketHistory fragment.
     */
    fun updateHistoryAdapter(success: Boolean, response: String) {
        val jsonResponse = JSONObject(response)
        if(!success){
            runOnUiThread {
                Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_LONG).show()
            }
        } else {
            val jsonHistory = jsonResponse.getJSONArray("history")
            historyBasket.clear()
            for (jsonPos in (0 until jsonHistory.length())) {
                val jsonObject = jsonHistory.get(jsonPos) as JSONObject
                val products = jsonObject.get("products") as JSONArray
                val productsList: List<ItemQuantity> = buildProducts(products)

                val total = jsonObject.get("total")
                val date = jsonObject.get("date")
                val hour = jsonObject.get("hour")

                val basket = Basket(
                    products = productsList,
                    total = total as String,
                    date = date as String?,
                    hour = hour as String?
                )
                historyBasket.add(basket)
            }

            runOnUiThread {
                adapterBasket.notifyDataSetChanged()
            }
        }
    }

    /**
     * Build the products list for basketHistory.
     */
    fun buildProducts(products: JSONArray): List<ItemQuantity> {
        var productList = mutableListOf<ItemQuantity>()
        for (prodPos in 0 until products.length()) {
            val jsonObject = products.get(prodPos) as JSONObject
            val id = jsonObject.get("id")
            val quantity = jsonObject.get("quantity")
            var product = ItemQuantity((id as Number).toLong(), quantity as Int)
            productList.add(product)
        }

        return productList.toList()
    }

}
