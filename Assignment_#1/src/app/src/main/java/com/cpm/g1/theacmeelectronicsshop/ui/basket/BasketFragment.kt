package com.cpm.g1.theacmeelectronicsshop.ui.basket

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.icu.text.NumberFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import coil.imageLoader
import coil.request.ImageRequest
import com.cpm.g1.theacmeelectronicsshop.MainActivity
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.ui.BasketHelper

@RequiresApi(Build.VERSION_CODES.N)
class BasketFragment : Fragment() {
    private val dbHelper by lazy { BasketHelper(context) }
    private var totalView: TextView? = null
    private val nf: NumberFormat = NumberFormat.getNumberInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get basket products
        val mainActivity = activity as MainActivity
        val basketCursor = dbHelper.getAll()
        mainActivity.startManagingCursor(basketCursor)

        // Basket Adapter
        val productList = view.findViewById<ListView>(R.id.basket_sv)
        productList.adapter = ProductAdapter(basketCursor)
        productList.emptyView = view.findViewById(R.id.empty_list)

        // Product click
        productList.setOnItemClickListener { _, _, _, l -> onProductClick(l) }

        // Set Basket Total
        totalView = view.findViewById(R.id.total)
        setTotalPrice(dbHelper.getBasketTotal())
    }

    private fun onProductClick(id: Long){
        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val intent = Intent(context, ProductDetailsActivity::class.java).also{
            it.putExtra("pos", id.toString())
        }
        startActivity(intent)
    }

    inner class ProductAdapter(cursor: Cursor) : CursorAdapter(activity, cursor,true) {

        override fun newView(ctx: Context, cursor: Cursor, parent: ViewGroup): View {
            return layoutInflater.inflate(R.layout.product_row, parent, false)
        }

        override fun bindView(view: View, context: Context, cursor: Cursor) {
            // Product Buttons
            val plusButton = view.findViewById<ImageButton>(R.id.plus_button)
            val minusButton = view.findViewById<ImageButton>(R.id.minus_button)
            val deleteButton = view.findViewById<ImageButton>(R.id.product_delete)

            // Set product details
            val id = dbHelper.getId(cursor)
            val quantity = dbHelper.getQuantity(cursor)
            val price = dbHelper.getPrice(cursor)
            val priceText = getString(R.string.product_price, price*quantity)

            view.findViewById<TextView>(R.id.price_text)?.text = priceText
            view.findViewById<TextView>(R.id.product_quantity)?.text = quantity.toString()
            view.findViewById<TextView>(R.id.name_text)?.text = dbHelper.getName(cursor)
            view.findViewById<TextView>(R.id.brand_text)?.text = dbHelper.getBrand(cursor)

            // Set product image from URL
            val image = view.findViewById<ImageView>(R.id.product_image)
            val request = ImageRequest.Builder(context)
                .data(dbHelper.getImageUrl(cursor))
                .target(image).build()
            context.imageLoader.enqueue(request)

            // Buttons click listeners
            deleteButton.setOnClickListener { onDeleteClickListener(view, id) }
            plusButton.setOnClickListener{ onPlusClickListener(view, id) }
            minusButton.setOnClickListener{ onMinusClickListener(view, id) }
        }


        @Suppress("DEPRECATION")
        private fun onDeleteClickListener(productView: View, id: String){
            // Update total price
            val price = priceViewToFloat(productView.findViewById(R.id.price_text))
            val newTotal = priceViewToFloat(totalView!!) - price
            setTotalPrice(newTotal)

            // Delete Item
            dbHelper.deleteById(id)
            cursor.requery()
            notifyDataSetChanged()
        }

        private fun onPlusClickListener(productView: View, id: String) {
            val priceView = productView.findViewById<TextView>(R.id.price_text)
            val quantityText = productView.findViewById<TextView>(R.id.product_quantity)
            val quantity = quantityText.text.toString().toInt()
            val price = priceViewToFloat(priceView)
            val unitPrice = price / quantity
            val newQuantity = quantity + 1
            val newTotal = priceViewToFloat(totalView!!) + unitPrice

            // Update quantity
            quantityText.text = newQuantity.toString()
            dbHelper.updateQuantity(id, newQuantity)

            // Update total price
            setTotalPrice(newTotal)

            // Update item price
            val newPrice = price + unitPrice
            val priceText = getString(R.string.product_price, newPrice)
            priceView.text = priceText
        }

        private fun onMinusClickListener(productView: View, id: String){
            val quantityText = productView.findViewById<TextView>(R.id.product_quantity)
            val quantity = quantityText.text.toString().toInt()
            val newQuantity = quantity - 1

            if(newQuantity == 0) {
                // Delete item from basket
                onDeleteClickListener(productView, id)
                return
            }

            // Delete one unit
            val priceView = productView.findViewById<TextView>(R.id.price_text)
            val price = priceViewToFloat(priceView)
            val unitPrice = price / quantity

            // Update quantity
            quantityText.text = newQuantity.toString()
            dbHelper.updateQuantity(id, newQuantity)

            // Update total
            val newTotal =  priceViewToFloat(totalView!!) - unitPrice
            setTotalPrice(newTotal)

            // Update item price
            val newPrice = price - unitPrice
            val priceText = getString(R.string.product_price, newPrice)
            priceView.text = priceText

        }
    }

    private fun setTotalPrice(total: Float){
        val priceText = getString(R.string.product_price, total)
        totalView!!.text = priceText
    }


    // Utils
    private fun priceViewToFloat(priceView: TextView): Float {
        return priceStringToFloat(priceView.text.toString())
    }

    private fun priceStringToFloat(strPrice: String): Float {
        return nf.parse(strPrice.dropLast(1)).toFloat()
    }
}