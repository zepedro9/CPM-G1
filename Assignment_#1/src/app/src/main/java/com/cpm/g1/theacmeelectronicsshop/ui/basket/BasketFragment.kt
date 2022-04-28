package com.cpm.g1.theacmeelectronicsshop.ui.basket

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import coil.imageLoader
import coil.request.ImageRequest
import com.cpm.g1.theacmeelectronicsshop.MainActivity
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.ui.BasketHelper

class BasketFragment : Fragment() {
    private val dbHelper by lazy { BasketHelper(context) }
    private var totalView: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //tempAddProducts()

        val productList = view.findViewById<ListView>(R.id.basket_sv)
        val basketCursor = dbHelper.getAll()
        val mainActivity = activity as MainActivity
        mainActivity.startManagingCursor(basketCursor)

        // Basket Adapter
        productList.adapter = ProductAdapter(basketCursor)
        productList.emptyView = view.findViewById(R.id.empty_list)

        // Product click
        productList.setOnItemClickListener { _, _, _, l -> onProductClick(l) }

        // Total
        totalView = view.findViewById(R.id.total)
        setTotalPrice(dbHelper.getBasketTotal())
    }

    private fun tempAddProducts() {
        dbHelper.insert(
            "Item 1",
            "Apple",
            "The Apple iPad Pro is a 12.9-inch touch screen tablet PC that is larger and offers higher resolution than Apple's other iPad models. The iPad Pro was scheduled to debut in November 2015, running the iOS 9 operating system. Apple unveiled the device at a September 2015 event in San Francisco.",
            909.99F,
            1,
            "http://127.0.0.1:3000/api/images/1048891954.jpg"
        )
        dbHelper.insert(
            "Item 2",
            "Apple",
            "The Apple iPad Pro is a 12.9-inch touch screen tablet PC that is larger and offers higher resolution than Apple's other iPad models. The iPad Pro was scheduled to debut in November 2015, running the iOS 9 operating system. Apple unveiled the device at a September 2015 event in San Francisco.",
            909.99F,
            3,
            "http://127.0.0.1:3000/api/images/26761059180.jpg"
        )
        dbHelper.insert(
            "Item 3",
            "Apple",
            "The Apple iPad Pro is a 12.9-inch touch screen tablet PC that is larger and offers higher resolution than Apple's other iPad models. The iPad Pro was scheduled to debut in November 2015, running the iOS 9 operating system. Apple unveiled the device at a September 2015 event in San Francisco.",
            909.99F,
            2,
            "http://127.0.0.1:3000/api/images/39555064294.jpg"
        )
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
            val quantity = dbHelper.getQuantity(cursor)
            val priceText = getString(R.string.product_price, dbHelper.getPrice(cursor)*quantity)
            val plusButton = view.findViewById<ImageButton>(R.id.plus_button)
            val minusButton = view.findViewById<ImageButton>(R.id.minus_button)
            val deleteButton = view.findViewById<ImageButton>(R.id.product_delete)

            val id = dbHelper.getId(cursor)

            view.findViewById<TextView>(R.id.name_text)?.text = dbHelper.getName(cursor)
            view.findViewById<TextView>(R.id.price_text)?.text = priceText
            view.findViewById<TextView>(R.id.brand_text)?.text = dbHelper.getBrand(cursor)
            view.findViewById<TextView>(R.id.product_quantity)?.text = quantity.toString()
            val image = view.findViewById<ImageView>(R.id.product_image)

            val request = ImageRequest.Builder(context)
                .data(dbHelper.getImageUrl(cursor))
                .target(image)
                .build()
            context.imageLoader.enqueue(request)

            deleteButton.setOnClickListener { onDeleteClickListener(id) }
            plusButton.setOnClickListener{ onPlusClickListener(view, id) }
            minusButton.setOnClickListener{ onMinusClickListener(view, id) }
        }


        @Suppress("DEPRECATION")
        private fun onDeleteClickListener(id: String){
            dbHelper.deleteById(id)
            cursor.requery()
            notifyDataSetChanged()

            // TODO: Update total price
        }

        private fun onPlusClickListener(productView: View, id: String) {
            val quantityText = productView.findViewById<TextView>(R.id.product_quantity)
            val newQuantity = quantityText.text.toString().toInt() + 1
            val price = productView.findViewById<TextView>(R.id.price_text).text.toString().dropLast(1).toFloat()
            quantityText.text = newQuantity.toString()
            dbHelper.updateQuantity(id, newQuantity)

            // Update total price
            val newTotal = totalView!!.text.toString().dropLast(1).toFloat() + price
            setTotalPrice(newTotal)

            // TODO: update price of items
        }

        private fun onMinusClickListener(productView: View, id: String){
            val quantityText = productView.findViewById<TextView>(R.id.product_quantity)
            val price = productView.findViewById<TextView>(R.id.price_text).text.toString().dropLast(1).toFloat()
            val newQuantity = quantityText.text.toString().toInt() - 1
            val newTotal = totalView!!.text.toString().dropLast(1).toFloat() - price

            if(newQuantity == 0) {
                onDeleteClickListener(id)
            }
            else {
                quantityText.text = newQuantity.toString()
                dbHelper.updateQuantity(id, newQuantity)
                setTotalPrice(newTotal)
                // TODO: update price of items
            }
        }
    }

    private fun setTotalPrice(total: Float){
        val priceText = getString(R.string.product_price, total)
        totalView!!.text = priceText
    }

}