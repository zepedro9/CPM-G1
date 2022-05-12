package com.cpm.g1.printer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.cpm.g1.printer.databinding.ActivityMainBinding
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.cpm.g1.printer.ui.ReceiptFragment
import com.cpm.g1.printer.ui.ScanFragment
import org.json.JSONArray
import org.json.JSONObject

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

    /**
     * Changes the ScanFragment to the ReceiptFragment
     */
    fun changeToReceiptFragment(success: Boolean, response: String) {
        val jsonResponse = JSONObject(response)
        if (!success) {
            runOnUiThread {
                Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_LONG).show()
            }
        } else {
            val bundle = Bundle()
            bundle.putString("user", jsonResponse.getString("user"))
            bundle.putString("nif", jsonResponse.getString("nif"))
            bundle.putString("basket", jsonResponse.getString("basket"))
            bundle.putString("products", jsonResponse.getString("products"))

            val receiptFragment = ReceiptFragment()
            receiptFragment.arguments = bundle
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_fragment_container, receiptFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
}


















