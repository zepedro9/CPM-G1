package com.cpm.g1.printer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cpm.g1.printer.databinding.ActivityMainBinding
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.cpm.g1.printer.ui.ScanFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Visualize View
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add fragment
        supportFragmentManager.commit {
            add<ScanFragment>(R.id.main_fragment_container)
        }
    }
}