package com.cpm.g1.theacmeelectronicsshop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.Basket
import com.cpm.g1.theacmeelectronicsshop.databinding.ActivityAppBinding
import com.cpm.g1.theacmeelectronicsshop.ui.basketHistory.BasketHistoryAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    // History fragment variables
    private val historyBasket = arrayListOf<Basket>();
    private val adapter by lazy { BasketHistoryAdapter(this, historyBasket) }

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

    }
}