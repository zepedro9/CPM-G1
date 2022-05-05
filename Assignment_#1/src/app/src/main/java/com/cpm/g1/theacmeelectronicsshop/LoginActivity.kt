package com.cpm.g1.theacmeelectronicsshop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.cpm.g1.theacmeelectronicsshop.databinding.ActivityMainBinding
import com.cpm.g1.theacmeelectronicsshop.ui.auth.LoginFragment
import org.json.JSONObject

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
        saveUuid(act, jsonResponse.getString("uuid"))
        act.startActivity(Intent(act, MainActivity::class.java))
    }

    /**
     * Saves the uuid in the EncryptedSharedPreferences.
     */
    private fun saveUuid(act: Activity, uuid: String){
        try {
            val sharedPreferences = getEncryptedSharedPreferences(act.applicationContext)
            with (sharedPreferences.edit()) {
                putString("uuid", uuid)
                apply()
            }
        } catch(err: Exception){
            println(err);
        }
    }
}