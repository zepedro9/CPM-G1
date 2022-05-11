package com.cpm.g1.printer.ui

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import com.cpm.g1.printer.R
import org.json.JSONArray

class ReceiptFragment : Fragment() {

    private var user: String? = null
    private var nif: String? = null
    private var basket: String? = null
    private var products: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        user = arguments?.getString("user")
        nif = arguments?.getString("nif")
        basket = arguments?.getString("basket")
        products = arguments?.getString("products")
        return inflater.inflate(R.layout.fragment_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val thank = view.findViewById<TextView>(R.id.thankyou)
        thank.text = getString(R.string.thank, user)

        val nifText = view.findViewById<TextView>(R.id.nif)
        nifText.text = nif

        val productsListView = view.findViewById<LinearLayout>(R.id.productsList)

        if(basket != null && products != null) {
            val finalTotal = JSONArray(basket!!).getJSONObject(0).getString("total")
            val date = JSONArray(basket!!).getJSONObject(0).getString("date")
            val hour = JSONArray(basket!!).getJSONObject(0).getString("hour")

            val dateText = view.findViewById<TextView>(R.id.date)
            dateText.text = date
            val timeText = view.findViewById<TextView>(R.id.time)
            timeText.text = hour

            val basketProductsArray = JSONArray(basket!!).getJSONObject(0).getJSONArray("products")
            val listProductsArray = JSONArray(products!!)
            for (i in 0 until basketProductsArray.length()) {
                val product = basketProductsArray.getJSONObject(i)
                val id = product.getString("id")
                val quantity = product.getString("quantity")
                val name: String
                val price: String

                for (j in 0 until listProductsArray.length()) {
                    val listProd = listProductsArray.getJSONObject(j)
                    if(listProd.getString("id").equals(id)) {
                        name = listProd.getString("name")
                        price = listProd.getString("price")

                        productsListView.addView(createProductLine(name, quantity.toInt(), price.toFloat()))
                        break
                    }
                }
            }

            productsListView.addView(createProductLine("Total", -1, finalTotal.toFloat()))
        } else {
            Toast.makeText(context, R.string.receipt_error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun createProductLine(name: String, quantity: Int, price: Float): LinearLayout {
        val productLineLayout = LinearLayout(requireActivity())
        val isTotal = quantity == -1
        productLineLayout.id = View.generateViewId()

        productLineLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )
        if(isTotal) {
            val params = productLineLayout.layoutParams as LinearLayout.LayoutParams
            params.setMargins(0, resources.getDimension(R.dimen.padding_default).toInt(), 0, 0)
            productLineLayout.layoutParams = params
        }
        productLineLayout.orientation = LinearLayout.HORIZONTAL

        val nameView = TextView(activity)
        if(isTotal) {
            nameView.text = getString(R.string.total)
            nameView.setTypeface(nameView.typeface, Typeface.BOLD);
        } else nameView.text = getString(R.string.product_name_format, quantity, name)
        nameView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1F
        )
        nameView.textAlignment = TextView.TEXT_ALIGNMENT_TEXT_START

        val priceView = TextView(activity)
        priceView.text = getString(R.string.product_price_format, price)
        if(isTotal) priceView.setTypeface(priceView.typeface, Typeface.BOLD);
        priceView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1F
        )
        priceView.textAlignment = TextView.TEXT_ALIGNMENT_TEXT_END

        productLineLayout.addView(nameView, 0)
        productLineLayout.addView(priceView, 1)

        return productLineLayout
    }
}