package com.cpm.g1.theacmeelectronicsshop

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.cpm.g1.theacmeelectronicsshop.databinding.ActivityMainBinding
import com.cpm.g1.theacmeelectronicsshop.ui.LoginFragment
import com.cpm.g1.theacmeelectronicsshop.ui.RegisterFragment


/**
 * This is the initial fragment of the program.
 * The activity remains active until the user logs in.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide action bar
        supportActionBar?.hide()

        // Visualize View
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add fragment
        supportFragmentManager.commit {
            add<LoginFragment>(R.id.main_fragment_container)
        }
    }

}