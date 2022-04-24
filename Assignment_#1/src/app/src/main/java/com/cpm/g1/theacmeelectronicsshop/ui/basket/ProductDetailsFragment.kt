package com.cpm.g1.theacmeelectronicsshop.ui.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cpm.g1.theacmeelectronicsshop.R

class ProductDetailsFragment : Fragment() {
    private var pos: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pos = arguments?.getString("pos")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(pos: String) =
            ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString("pos", pos)
                }
            }

        fun newInstance(bundle: Bundle): ProductDetailsFragment {
            val pos = bundle.getString("pos", null)
            return newInstance(pos)
        }
    }
}