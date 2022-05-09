package com.cpm.g1.printer.ui

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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.cpm.g1.printer.R
import com.cpm.g1.printer.readStream
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.CaptureActivity
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class ScanFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scanButton = view.findViewById<Button>(R.id.scan)
        scanButton.setOnClickListener {
            val intent = Intent(this.context, CaptureActivity::class.java)
            intent.putExtra(
                "SCAN_FORMATS",
                "UPC_A"
            )
            intent.action = Intents.Scan.ACTION
            openScan.launch(intent)
        }
    }

    private val openScan =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    val prodID: String? = it.data?.getStringExtra(Intents.Scan.RESULT)
                    //makeGetRequest(prodID)
                }
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(this.context, getText(R.string.scan_cancelled), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this.context, getText(R.string.scan_failed), Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun makeGetRequest(prodID: String?) {
        Thread(prodID?.let { GetProduct(it) }).start()
    }

    private fun showProduct(payload: String) {
        val jsonProduct = JSONArray(payload).getJSONObject(0)
        val prodId = jsonProduct.getLong("id").toString()
        val prodName = jsonProduct.getString("name")
        val prodBrand = jsonProduct.getString("brand")
        val prodPrice = jsonProduct.getString("price").toFloat()
        val prodDesc = jsonProduct.getString("description")
        val prodImage = jsonProduct.getString("image_url")

    }

    inner class GetProduct(val prodID: String) : Runnable {
        override fun run() {
            val url: URL
            var urlConnection: HttpURLConnection? = null
            try {
                url = URL("http://127.0.0.1:3000/api/products/${prodID.dropLast(1)}")

                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.doInput = true
                urlConnection.setRequestProperty("Content-Type", "application/json")
                urlConnection.useCaches = false
                val responseCode = urlConnection.responseCode

                if (responseCode == 200)
                    showProduct(readStream(urlConnection.inputStream))
                else
                    Toast.makeText(context, context?.getText(R.string.scan_failed) ?: "The scan has failed", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("ScanGetRequest", e.toString())
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    Toast.makeText(
                        context,
                        context?.getText(R.string.scan_failed) ?: "The scan has failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } finally {
                urlConnection?.disconnect()
            }
        }
    }

}