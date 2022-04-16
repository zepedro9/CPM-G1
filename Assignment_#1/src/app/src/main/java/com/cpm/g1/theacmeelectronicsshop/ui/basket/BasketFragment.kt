package com.cpm.g1.theacmeelectronicsshop.ui.basket

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
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

        // Card Product Adapter
        val productList = view.findViewById<ListView>(R.id.basket_sv)
        val basketCursor = dbHelper.getAll()
        val mainActivity = activity as MainActivity
        mainActivity.startManagingCursor(basketCursor)

        productList.adapter = ProductAdapter(basketCursor)
        productList.emptyView = view.findViewById(R.id.empty_list)
    }

    inner class ProductAdapter(c: Cursor) : CursorAdapter(activity, c,true) {
        override fun newView(ctx: Context, c: Cursor, parent: ViewGroup): View {
            val row: View = layoutInflater.inflate(R.layout.product_row, parent, false)
            val priceText = getString(R.string.product_price, dbHelper.getPrice(c))
            val quantityEditField = row.findViewById<EditText>(R.id.quantity_ef)
            val deleteButton = row.findViewById<ImageButton>(R.id.product_delete)

            row.findViewById<TextView>(R.id.name_text).text = dbHelper.getName(c)
            row.findViewById<TextView>(R.id.price_text).text = priceText
            row.findViewById<TextView>(R.id.brand_text).text = dbHelper.getBrand(c)
            quantityEditField .findViewById<EditText>(R.id.quantity_ef).setText(dbHelper.getQuantity(c))
            //val image = row.findViewById<ImageView>(R.id.product_image)
            //image.setImageResource(R.drawable.ic_test)

            deleteButton.setOnClickListener{
                dbHelper.delete(dbHelper.getId(c))
                c.requery()
                notifyDataSetChanged()
            }

            return row
        }

        override fun bindView(p0: View?, p1: Context?, p2: Cursor?) {
        }
    }

}