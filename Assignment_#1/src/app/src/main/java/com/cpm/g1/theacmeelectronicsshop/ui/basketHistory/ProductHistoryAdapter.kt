package com.cpm.g1.theacmeelectronicsshop.ui.basketHistory

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import coil.imageLoader
import coil.request.ImageRequest
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.Basket
import org.json.JSONObject


class ProductHistoryAdapter(val activity: ProductTransactionActivity, val productsList: ArrayList<JSONObject>) :
    ArrayAdapter<JSONObject>(activity, R.layout.product_history_row, productsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = convertView ?: activity.layoutInflater.inflate(
            R.layout.product_history_row,
            parent,
            false
        )
        try {
            val product = productsList[position]
            val productId = product.get("id")

            val basket = (activity as ProductTransactionActivity).basket
            val itemQuantity = getQuantity(basket, productId as Long).toString()
            println(itemQuantity)
            row.findViewById<TextView>(R.id.name_text).text = product.getString("name")
            row.findViewById<TextView>(R.id.brand_text).text = product.getString("brand")
            row.findViewById<TextView>(R.id.price_text).text = product.getString("price") + " €"
            row.findViewById<TextView>(R.id.quantity_value).text = itemQuantity

            val image = row.findViewById<ImageView>(R.id.image)

            image!!.visibility = View.VISIBLE
            val request = ImageRequest.Builder(activity)
                .data(product.getString("image_url"))
                .target(image).build()

            context.imageLoader?.enqueue(request)

        }catch(err: Exception){
            Log.e("ERR!!", err.toString())

        }
        return row
    }

    /**
     * Get's the quantity of a product.
     */
    fun getQuantity(basket: Basket, productId: Long): Int {
        for (itemQuantity in basket.products) {
            if (itemQuantity.id == productId)
                return itemQuantity.quantity
        }
        return 0
    }
}