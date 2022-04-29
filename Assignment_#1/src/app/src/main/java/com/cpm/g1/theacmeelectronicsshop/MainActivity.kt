package com.cpm.g1.theacmeelectronicsshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.cpm.g1.theacmeelectronicsshop.databinding.ActivityAppBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    private lateinit var binding:ActivityAppBinding

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
            setOf(R.id.navigation_scan, R.id.navigation_basket)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}