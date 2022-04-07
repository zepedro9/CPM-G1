package com.cpm.g1.theacmeelectronicsshop.ui.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.cpm.g1.theacmeelectronicsshop.ProductAdapter
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.ShopApp

class CardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_card, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Card Product Adapter
        val cardList = view.findViewById<ListView>(R.id.card_sv)
        cardList.emptyView = view.findViewById(R.id.empty_list)
        cardList.adapter = (activity?.application as ShopApp).adapter
    }
}