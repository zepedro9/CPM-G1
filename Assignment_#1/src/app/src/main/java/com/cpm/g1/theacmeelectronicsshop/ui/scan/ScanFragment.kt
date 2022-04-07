package com.cpm.g1.theacmeelectronicsshop.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.cpm.g1.theacmeelectronicsshop.Product
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.ShopApp
import com.cpm.g1.theacmeelectronicsshop.ui.BasketHelper

class ScanFragment : Fragment() {
    private val dbHelper by lazy { BasketHelper(context) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.add_product_btn).setOnClickListener { onClickButton() }
    }

    private fun onClickButton(){
        dbHelper.insert("Muni", "Nothing", "Eyes of different colors", 2.0F)
    }

}