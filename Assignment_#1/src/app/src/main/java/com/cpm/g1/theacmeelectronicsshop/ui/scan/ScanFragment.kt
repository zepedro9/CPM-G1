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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import coil.imageLoader
import coil.request.ImageRequest
import com.cpm.g1.theacmeelectronicsshop.MainActivity
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.getUserUUID
import com.cpm.g1.theacmeelectronicsshop.readStream
import com.cpm.g1.theacmeelectronicsshop.ui.BasketHelper
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.CaptureActivity
import org.json.JSONArray
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL

class ScanFragment : Fragment() {
    private val dbHelper by lazy { BasketHelper(context) }

    class ScannedProduct(val id: String, val name: String, val brand: String, val desc: String, val price: Float, val imageUrl: String) :
        Serializable

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
                addToBasket(currentProduct!!.id)
                val mainActivity = activity as MainActivity
                val navController = findNavController(mainActivity, R.id.nav_host_fragment_activity_main)
                navController.navigate(R.id.navigation_basket)
            } else {
                Toast.makeText(this.context, getText(R.string.scan_no_scanned), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        if(currentProduct != null)
            state.putSerializable("currentProduct", currentProduct)
    }

    override fun onViewStateRestored(state: Bundle?) {
        super.onViewStateRestored(state)
        if(state?.getSerializable("currentProduct") != null) {
            currentProduct = state.getSerializable("currentProduct") as ScannedProduct?
            view?.findViewById<TextView>(R.id.nameContent)!!.text = currentProduct?.name
            view?.findViewById<TextView>(R.id.brandContent)!!.text = currentProduct?.brand
            view?.findViewById<TextView>(R.id.descContent)!!.text = currentProduct?.desc
            view?.findViewById<TextView>(R.id.priceContent)!!.text = getString(R.string.product_price, currentProduct?.price)
            val image = view?.findViewById<ImageView>(R.id.imageContent)
            image!!.visibility = View.VISIBLE
            val request = ImageRequest.Builder(requireContext())
                .data(currentProduct?.imageUrl)
                .target(image).build()
            context?.imageLoader?.enqueue(request)
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

    private fun addToBasket(productId: String) {
        val uuid = getUserUUID(requireContext())
        val productCursor = dbHelper.getBasketItemById(uuid, productId)

        if(!productCursor.moveToFirst()){
            // New product
            dbHelper.insertBasketItem(uuid, productId)
        } else {
            // Update quantity
            val quantity = productCursor.getInt(1) + 1
            dbHelper.updateItemQuantity(uuid, productId, quantity)
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

        activity?.runOnUiThread {
            currentProduct =
                ScannedProduct(prodId, prodName, prodBrand, prodDesc, prodPrice, prodImage)
            view?.findViewById<TextView>(R.id.nameContent)!!.text = prodName
            view?.findViewById<TextView>(R.id.brandContent)!!.text = prodBrand
            view?.findViewById<TextView>(R.id.descContent)!!.text = prodDesc
            val priceText = getString(R.string.product_price, prodPrice)
            view?.findViewById<TextView>(R.id.priceContent)!!.text = priceText

            // Set product image from URL
            val image = view?.findViewById<ImageView>(R.id.imageContent)
            image!!.visibility = View.VISIBLE
            val request = ImageRequest.Builder(requireContext())
                .data(prodImage)
                .target(image).build()
            context?.imageLoader?.enqueue(request)
        }
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