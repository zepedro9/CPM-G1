package com.cpm.g1.theacmeelectronicsshop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide action bar
        supportActionBar?.hide()

        // Visualize View
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check for old user
        if (getUserUUID(applicationContext) != "")
            startActivity(Intent(applicationContext, MainActivity::class.java))

        // Add fragment
        supportFragmentManager.commit {
            add<LoginFragment>(R.id.main_fragment_container)
        }
    }

    /**
     * Changes the LoginFragment to the RegisterFragment
     */
    fun changeToRegisterFragment(success: Boolean, response: String) {
        if (!success) {
            runOnUiThread {
                Toast.makeText(this, "This email is already registered", Toast.LENGTH_LONG).show()
            }
        } else {
            val loginFragment = LoginFragment()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_fragment_container, loginFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit();
        }
    }

    /**
     * Changes the LoginActivity to MainActivity
     */
    fun toMainActivity(success: Boolean, response: String) {
        val jsonResponse = JSONObject(response)
        saveUuid(jsonResponse.getString("uuid"))
        startActivity(Intent(this, MainActivity::class.java))
    }

    /**
     * Saves the uuid in the EncryptedSharedPreferences.
     */
    // TODO - maybe save encrypted or something
    private fun saveUuid(uuid: String) {
        try {
            val sharedPreferences = getEncryptedSharedPreferences(applicationContext)
            with(sharedPreferences.edit()) {
                putString("uuid", uuid)
                apply()
            }
        } catch (err: Exception) {
            println(err);
        }
    }
}