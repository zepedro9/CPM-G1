package com.cpm.g1.theacmeelectronicsshop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.Basket
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.Product
import com.cpm.g1.theacmeelectronicsshop.databinding.ActivityAppBinding
import com.cpm.g1.theacmeelectronicsshop.ui.basketHistory.BasketHistoryAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    // History fragment variables
    val historyBasket : ArrayList<Basket> = ArrayList();
    val adapter by lazy {BasketHistoryAdapter(this, historyBasket)}

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


    /**
     * Updates the history fragment content in the BasketHistory fragment.
     */
    fun updateHistoryAdapter(activity: Activity, response: String) {
        val jsonResponse = JSONArray(response)
        historyBasket.clear()
        for (jsonPos in (0 until jsonResponse.length())){
            val jsonObject = jsonResponse.get(jsonPos) as JSONObject
            val products = jsonObject.get("products") as JSONArray
            val productsList: List<Product> = buildProducts(products)

            val total = jsonObject.get("total")
            val date = jsonObject.get("date")
            val hour = jsonObject.get("hour")

            val basket = Basket(products = productsList, total= total as String, date= date as String?, hour = hour as String?)
            historyBasket.add(basket)
        }
        adapter.notifyDataSetChanged()
    }

    /**
     * Build the products list.
     */
    fun buildProducts(products: JSONArray): List<Product> {
        var productList = mutableListOf<Product>()
        for (prodPos in 0 until products.length()){
            val jsonObject = products.get(prodPos) as JSONObject
            val id = jsonObject.get("id")
            val quantity = jsonObject.get("quantity")
            var product = Product(id as Int, quantity as Int)
            productList.add(product)
        }

        return productList.toList()
    }
}
