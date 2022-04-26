package com.cpm.g1.theacmeelectronicsshop.ui.basket

import android.annotation.SuppressLint
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
import com.cpm.g1.theacmeelectronicsshop.MainActivity
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.ui.BasketHelper

class BasketFragment : Fragment() {
    private val dbHelper by lazy { BasketHelper(context) }

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
        productList.setOnItemClickListener { _, _, _, l -> onProductClick(l) }
    }

    private fun tempAddProducts() {
        dbHelper.insert(
            "Item 1",
            "Apple",
            "The Apple iPad Pro is a 12.9-inch touch screen tablet PC that is larger and offers higher resolution than Apple's other iPad models. The iPad Pro was scheduled to debut in November 2015, running the iOS 9 operating system. Apple unveiled the device at a September 2015 event in San Francisco.",
            909.99F,
            1,
        )
        dbHelper.insert(
            "Item 2",
            "Apple",
            "The Apple iPad Pro is a 12.9-inch touch screen tablet PC that is larger and offers higher resolution than Apple's other iPad models. The iPad Pro was scheduled to debut in November 2015, running the iOS 9 operating system. Apple unveiled the device at a September 2015 event in San Francisco.",
            909.99F,
            3,
        )
        dbHelper.insert(
            "Item 3",
            "Apple",
            "The Apple iPad Pro is a 12.9-inch touch screen tablet PC that is larger and offers higher resolution than Apple's other iPad models. The iPad Pro was scheduled to debut in November 2015, running the iOS 9 operating system. Apple unveiled the device at a September 2015 event in San Francisco.",
            909.99F,
            2,
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

        override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
            val priceText = getString(R.string.product_price, cursor?.let { dbHelper.getPrice(it) })
            val plusButton = view?.findViewById<ImageButton>(R.id.plus_button)
            val minusButton = view?.findViewById<ImageButton>(R.id.minus_button)
            val deleteButton = view?.findViewById<ImageButton>(R.id.product_delete)

            if(cursor == null) return
            val id = dbHelper.getId(cursor)

            view?.findViewById<TextView>(R.id.name_text)?.text = dbHelper.getName(cursor)
            view?.findViewById<TextView>(R.id.price_text)?.text = priceText
            view?.findViewById<TextView>(R.id.brand_text)?.text = dbHelper.getBrand(cursor)
            view?.findViewById<TextView>(R.id.product_quantity)?.text = dbHelper.getQuantity(cursor)
            //val image = view.findViewById<ImageView>(R.id.product_image)
            //image.setImageResource(R.drawable.ic_test)

            deleteButton?.setOnClickListener { onDeleteClickListener(id) }
            plusButton?.setOnClickListener{ onPlusClickListener(view, id) }
            minusButton?.setOnClickListener{ onMinusClickListener(view, id) }
        }


        @Suppress("DEPRECATION")
        private fun onDeleteClickListener(id: String){
            dbHelper.deleteById(id)
            cursor.requery()
            notifyDataSetChanged()
        }

        private fun onPlusClickListener(view: View, id: String) {
            val quantityText = view.findViewById<TextView>(R.id.product_quantity)
            val newQuantity = quantityText.text.toString().toInt() + 1
            quantityText.text = newQuantity.toString()
            dbHelper.updateQuantity(id, newQuantity)
        }

        private fun onMinusClickListener(view: View, id: String){
            val quantityText = view.findViewById<TextView>(R.id.product_quantity)
            val newQuantity = quantityText.text.toString().toInt() - 1

            if(newQuantity == 0) {
                onDeleteClickListener(id)
            }
            else {
                quantityText.text = newQuantity.toString()
                dbHelper.updateQuantity(id, newQuantity)
            }

        }
    }

}