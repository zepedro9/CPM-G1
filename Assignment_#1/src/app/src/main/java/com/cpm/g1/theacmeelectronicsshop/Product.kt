package com.cpm.g1.theacmeelectronicsshop

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class Product (val name: String, val price: Float, val brand: String, val description: String) {
}

class ProductAdapter(val ctx: Context, val prods: ArrayList<Product>):
    ArrayAdapter<Product>(ctx, R.layout.product_row, prods) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = convertView ?: (ctx as AppCompatActivity).layoutInflater.inflate(R.layout.product_row, parent, false)
        val p = prods[position]
        row.findViewById<TextView>(R.id.name_text).text = p.name
        row.findViewById<TextView>(R.id.price_text).text = p.price.toString()
        row.findViewById<TextView>(R.id.brand_text).text = p.brand
        val image = row.findViewById<ImageView>(R.id.product_image)
        /*image.setImageResource(R.drawable.ic_test)*/
        return row
    }
}