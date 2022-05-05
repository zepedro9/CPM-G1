package com.cpm.g1.theacmeelectronicsshop

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.cpm.g1.theacmeelectronicsshop.databinding.ActivityMainBinding
import com.cpm.g1.theacmeelectronicsshop.ui.auth.LoginFragment
import org.json.JSONObject
import java.net.CacheResponse


/**
 * This is the initial fragment of the program.
 * The activity remains active until the user logs in.
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var keysGenerated = false       // tells if the security keys have been generated.


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

    /**
     * Changes the LoginFragment to the RegisterFragment
     */
    fun changeToRegisterFragment(activity: Activity?, jsonResponse: JSONObject) {
        val loginFragment = LoginFragment()
        val fragmentManager = (activity as LoginActivity).supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_fragment_container, loginFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit();
    }

    /**
     * Changes the LoginActivity to MainActivity
     */
    fun toMainActivity(act: Activity, jsonResponse: JSONObject) {
        act.startActivity(Intent(act, MainActivity::class.java))
        saveUuid(act, jsonResponse)
    }

    /**
     * Saves the uuid in the SharedPreferences.
     */
    private fun saveUuid(act: Activity, jsonResponse: JSONObject) {
        val editor: SharedPreferences.Editor =
            act.getSharedPreferences("credentials", MODE_PRIVATE).edit()
        editor.putString("uuid", jsonResponse.getString("uuid"))
        editor.apply()
        println(act.getSharedPreferences("credentials", MODE_PRIVATE).getString("uuid", "nothing"))
    }


}