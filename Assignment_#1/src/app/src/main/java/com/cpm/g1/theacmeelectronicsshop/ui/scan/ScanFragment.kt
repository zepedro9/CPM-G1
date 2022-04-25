package com.cpm.g1.theacmeelectronicsshop.ui.scan

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.ui.BasketHelper
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.CaptureActivity
import org.json.JSONArray
import java.net.SocketException
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanFragment : Fragment() {
    private val dbHelper by lazy { BasketHelper(context) }

    class ScannedProduct(val name: String, val brand: String, val desc: String, val price: Float)

    private var currentProduct: ScannedProduct? = null

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
            openScan.launch(intent)
        }

        val addToBasketButton = view.findViewById<Button>(R.id.btnAddToBasket)
        addToBasketButton.setOnClickListener {
            if(currentProduct != null) {
                addToBasket(currentProduct!!.name, currentProduct!!.brand, currentProduct!!.desc, currentProduct!!.price)
            } else {
                Toast.makeText(this.context, getText(R.string.scan_no_scanned), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        if(currentProduct != null) {
            state.putString("scannedName", currentProduct!!.name)
            state.putString("scannedBrand", currentProduct!!.brand)
            state.putString("scannedDesc", currentProduct!!.desc)
            state.putFloat("scannedPrice", currentProduct!!.price)
        }
    }

    override fun onViewStateRestored(state: Bundle?) {
        super.onViewStateRestored(state)
        if(state?.getString("scannedName") != null &&
            state.getString("scannedBrand") != null &&
            state.getString("scannedDesc") != null
        ) {
            currentProduct = ScannedProduct(state.getString("scannedName")!!, state.getString("scannedBrand")!!, state.getString("scannedDesc")!!, state.getFloat("scannedPrice"))
            view?.findViewById<TextView>(R.id.nameContent)!!.text = state.getString("scannedName")!!
            view?.findViewById<TextView>(R.id.brandContent)!!.text = state.getString("scannedBrand")!!
            view?.findViewById<TextView>(R.id.descContent)!!.text = state.getString("scannedDesc")!!
            view?.findViewById<TextView>(R.id.priceContent)!!.text = state.getFloat("scannedPrice").toString()
            view?.findViewById<ImageView>(R.id.imageContent)!!.visibility = View.VISIBLE
        }
    }

    private val openScan =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    val prodID: String? = it.data?.getStringExtra(Intents.Scan.RESULT)
                    makeGetRequest(prodID)
                }
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(this.context, getText(R.string.scan_cancelled), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this.context, getText(R.string.scan_failed), Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun addToBasket(name: String, brand: String, desc: String, price: Float) {
        val c: Cursor = dbHelper.getAll()
        c.moveToFirst()
        while (!c.isAfterLast) {
            if(dbHelper.getName(c) == name) {
                dbHelper.updateQuantity(dbHelper.getId(c), dbHelper.getQuantity(c).toInt() + 1)
                return
            }
            c.moveToNext()
        }
        dbHelper.insert(
            name,
            brand,
            desc,
            price,
            1,
        )
    }

    private fun makeGetRequest(prodID: String?) {
        val networkService: ExecutorService = Executors.newFixedThreadPool(4)
        val ct = context

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
                    currentProduct = ScannedProduct(prodName, prodBrand, prodDesc, prodPrice.toFloat())
                    view?.findViewById<TextView>(R.id.nameContent)!!.text = prodName
                    view?.findViewById<TextView>(R.id.brandContent)!!.text = prodBrand
                    view?.findViewById<TextView>(R.id.descContent)!!.text = prodDesc
                    view?.findViewById<TextView>(R.id.priceContent)!!.text = prodPrice
                    view?.findViewById<ImageView>(R.id.imageContent)!!.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e("ScanGetRequest", e.toString())
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    Toast.makeText(ct, ct?.getText(R.string.scan_failed) ?: "The scan has failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}