package com.cpm.g1.printer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cpm.g1.printer.R

class ReceiptFragment : Fragment() {

    val basket = arguments?.getString("basket")
    val total = arguments?.getString("total")
    val date = arguments?.getString("date")
    val hour = arguments?.getString("hour")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}