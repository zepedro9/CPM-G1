package com.cpm.g1.theacmeelectronicsshop.ui.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import coil.imageLoader
import coil.request.ImageRequest
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.BasketItem
import com.cpm.g1.theacmeelectronicsshop.ui.BasketHelper

class ProductDetailsFragment : Fragment() {
    private var item: BasketItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = arguments?.getSerializable("item") as BasketItem?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate (R.layout.fragment_product_details, container, false)
        val priceText = getString(R.string.product_price, item?.price)

        view.findViewById<TextView>(R.id.name).text = item?.name
        view.findViewById<TextView>(R.id.brandContent).text = item?.brand
        view.findViewById<TextView>(R.id.priceContent).text = priceText
        view.findViewById<TextView>(R.id.descriptionContent).text = item?.description
        val image = view.findViewById<ImageView>(R.id.image)

        val request = ImageRequest.Builder(requireContext())
            .data(item?.image_url)
            .target(image)
            .build()

        context?.imageLoader?.enqueue(request)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(item: BasketItem) =
            ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", item)
                }
            }

        fun newInstance(bundle: Bundle): ProductDetailsFragment {
            val item = bundle.getSerializable("item") as BasketItem
            return newInstance(item)
        }
    }
}