package com.cpm.g1.theacmeelectronicsshop.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.cpm.g1.theacmeelectronicsshop.Product
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.ShopApp

class DashboardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.add_product_btn).setOnClickListener { onClickButton() }
    }

    private fun onClickButton(){
        val p = Product("Muni", 2.0F, "Nothing", "Eyes of different colors")
        val app = activity?.application as ShopApp
        app.adapter?.add(p)
    }

}