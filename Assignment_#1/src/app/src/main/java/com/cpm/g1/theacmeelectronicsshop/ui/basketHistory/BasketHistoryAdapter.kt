package com.cpm.g1.theacmeelectronicsshop.ui.basketHistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.cpm.g1.theacmeelectronicsshop.MainActivity
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.Basket


class BasketHistoryAdapter(val activity: MainActivity, productsList: ArrayList<Basket>) :
    ArrayAdapter<Basket>(activity, R.layout.basket_history_row, productsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = convertView?: activity.layoutInflater.inflate(R.layout.basket_history_row, parent, false)
        val history = activity.historyBasket[position]

        row.findViewById<TextView>(R.id.date_value_tv).text = history.date
        row.findViewById<TextView>(R.id.hour_value_tv).text = history.hour
        row.findViewById<TextView>(R.id.total_value_tv).text = history.total
        row.findViewById<TextView>(R.id.num_items_value_tv).text = history.products.size.toString()
        return row
    }
}