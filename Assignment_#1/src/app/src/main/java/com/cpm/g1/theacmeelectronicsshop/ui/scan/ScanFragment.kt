package com.cpm.g1.theacmeelectronicsshop.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.cpm.g1.theacmeelectronicsshop.R
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
        dbHelper.insert(
            "iPad Pro (11'' - 128 GB - Wi-Fi - Gray)",
            "Apple",
            "The Apple iPad Pro is a 12.9-inch touch screen tablet PC that is larger and offers higher resolution than Apple's other iPad models. The iPad Pro was scheduled to debut in November 2015, running the iOS 9 operating system. Apple unveiled the device at a September 2015 event in San Francisco.",
            909.99F,
            1,
            )
    }

}