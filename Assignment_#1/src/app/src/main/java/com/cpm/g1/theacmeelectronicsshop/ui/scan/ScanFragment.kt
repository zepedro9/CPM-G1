package com.cpm.g1.theacmeelectronicsshop.ui.scan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.ui.BasketHelper
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.CaptureActivity
import org.json.JSONArray
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ScanFragment : Fragment() {
    private val dbHelper by lazy { BasketHelper(context) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scanButton = view.findViewById<Button>(R.id.btnScan)
        scanButton.setOnClickListener {
            val intent = Intent(this.context, CaptureActivity::class.java)
            intent.putExtra(
                "SCAN_FORMATS",
                "UPC_A"
            )
            intent.action = Intents.Scan.ACTION
            startActivityForResult(intent, 0)
        }
    }

    private fun onClickButton() {
        dbHelper.insert(
            "iPad Pro (11'' - 128 GB - Wi-Fi - Gray)",
            "Apple",
            "The Apple iPad Pro is a 12.9-inch touch screen tablet PC that is larger and offers higher resolution than Apple's other iPad models. The iPad Pro was scheduled to debut in November 2015, running the iOS 9 operating system. Apple unveiled the device at a September 2015 event in San Francisco.",
            909.99F,
            1,
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                val prodID: String? = data?.getStringExtra(Intents.Scan.RESULT)

                makeGetRequest(prodID)
            }
            Activity.RESULT_CANCELED -> {
                Toast.makeText(this.context, "Scan canceled", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this.context, "Scan failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makeGetRequest(prodID: String?) {
        val networkService: ExecutorService = Executors.newFixedThreadPool(4)

        networkService.execute {
            try {
                val jsonObject = JSONArray(URL("http://127.0.0.1:3000/api/products/${prodID?.dropLast(1)}").readText()).getJSONObject(0)

                val prodName = jsonObject.getString("name")
                Log.i("Name: ", prodName)

                val prodBrand = jsonObject.getString("brand")
                Log.i("Brand: ", prodBrand)

                val prodPrice = jsonObject.getString("price")
                Log.i("Price: ", prodPrice)

                val prodDesc = jsonObject.getString("description")
                Log.i("Description: ", prodDesc)

                activity?.runOnUiThread {
                    view?.findViewById<TextView>(R.id.nameContent)!!.text = prodName
                    view?.findViewById<TextView>(R.id.descContent)!!.text = prodDesc
                }
            } catch (e: Exception) {
                Log.e("ScanGetRequest", e.toString())
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    // TODO: Tornar os possíveis erros (ligação à internet, ao servidor, etc) user-friendly
                    Toast.makeText(this.context, e.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}