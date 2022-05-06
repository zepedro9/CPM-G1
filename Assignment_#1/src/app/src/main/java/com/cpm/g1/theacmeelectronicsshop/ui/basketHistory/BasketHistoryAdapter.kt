package com.cpm.g1.theacmeelectronicsshop.ui.basketHistory

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.cpm.g1.theacmeelectronicsshop.MainActivity
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.Basket
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.Product

class BasketHistoryAdapter(activity: MainActivity, productsList: ArrayList<Basket>) :
    ArrayAdapter<Basket>(activity, R.layout.basket_history_row, productsList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent)
    }
}