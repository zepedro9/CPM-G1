package com.cpm.g1.theacmeelectronicsshop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cpm.g1.theacmeelectronicsshop.databinding.ActivityMainBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Visualize View
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}