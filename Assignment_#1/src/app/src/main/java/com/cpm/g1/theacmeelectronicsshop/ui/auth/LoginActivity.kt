package com.cpm.g1.theacmeelectronicsshop.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.cpm.g1.theacmeelectronicsshop.ui.MainActivity
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.databinding.ActivityMainBinding
import com.cpm.g1.theacmeelectronicsshop.httpService.UserExists
import com.cpm.g1.theacmeelectronicsshop.security.clearUserUUID
import com.cpm.g1.theacmeelectronicsshop.security.getEncryptedSharedPreferences
import com.cpm.g1.theacmeelectronicsshop.security.getUserUUID
import com.cpm.g1.theacmeelectronicsshop.utils.ConfigHTTP
import com.google.gson.Gson
import org.json.JSONObject

const val USER_EXISTS_ADDRESS: String = "http://" + ConfigHTTP.BASE_ADDRESS + ":3000/api/auth/user"

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
        val uuid = getUserUUID(applicationContext)
        if (uuid != ""){
            val uuidJson = Gson().toJson(hashMapOf("userUUID" to uuid))
            Thread(UserExists(this, USER_EXISTS_ADDRESS, uuidJson)).start()
        } else {
            // Add login fragment
            supportFragmentManager.commit {
                add<LoginFragment>(R.id.main_fragment_container)
            }
        }
    }

    /**
     * Logs in the user or logs out, depending on the answer of the server
     */
    fun loginOrLogout(success: Boolean, response: String){
        runOnUiThread {
            if (!success) {
                // Clear user UUID and show login fragment
                clearUserUUID(applicationContext)
                supportFragmentManager.commit {
                    add<LoginFragment>(R.id.main_fragment_container)
                }
            } else {
                // Proceed to main
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }
    }

    /**
     * Changes the LoginFragment to the RegisterFragment
     */
    fun changeToRegisterFragment(success: Boolean, response: String) {
        val jsonResponse = JSONObject(response)
        if (!success) {
            runOnUiThread {
                Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_LONG).show()
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
        if(!success){
            runOnUiThread {
                Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_LONG).show()
            }
        } else {
            saveUuid(jsonResponse.getString("uuid"))
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    /**
     * Saves the uuid in the EncryptedSharedPreferences.
     */
    private fun saveUuid(uuid: String){
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