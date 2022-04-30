package com.cpm.g1.theacmeelectronicsshop

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.cpm.g1.theacmeelectronicsshop.databinding.ActivityMainBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide action bar
        supportActionBar?.hide()

        // Visualize View
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<Button>(R.id.login_btn).setOnClickListener { onLoginPressed() }
    }

    private fun onLoginPressed(){
       val transaction = supportFragmentManager.beginTransaction()
        //transaction.replace()
        transaction.commit()
    }

   private fun onSignUpPressed(){

   }
}