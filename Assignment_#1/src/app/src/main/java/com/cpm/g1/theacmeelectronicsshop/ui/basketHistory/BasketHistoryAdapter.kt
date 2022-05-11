package com.cpm.g1.theacmeelectronicsshop.ui.basketHistory

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.cpm.g1.theacmeelectronicsshop.ui.MainActivity
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.Basket


class BasketHistoryAdapter(val activity: MainActivity, basketList: ArrayList<Basket>) :
    ArrayAdapter<Basket>(activity, R.layout.basket_history_row, basketList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = convertView?: activity.layoutInflater.inflate(R.layout.basket_history_row, parent, false)
        val basket = activity.historyBasket[position]

        row.findViewById<TextView>(R.id.date_value_tv).text = basket.date
        row.findViewById<TextView>(R.id.hour_value_tv).text = basket.hour
        row.findViewById<TextView>(R.id.total_value_tv).text = basket.total
        row.findViewById<TextView>(R.id.num_items_value_tv).text = basket.products.size.toString()
        return row
    }

}