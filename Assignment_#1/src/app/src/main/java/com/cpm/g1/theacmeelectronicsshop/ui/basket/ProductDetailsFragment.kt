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
import com.cpm.g1.theacmeelectronicsshop.ui.BasketHelper

class ProductDetailsFragment : Fragment() {
    private var pos: String? = null
    private val dbHelper by lazy { BasketHelper(context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pos = arguments?.getString("pos")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate (R.layout.fragment_product_details, container, false)
      /*  val productCursor = dbHelper.getById(pos!!)
        productCursor.moveToFirst()

        val priceText = getString(R.string.product_price, dbHelper.getPrice(productCursor))

        view.findViewById<TextView>(R.id.name).text = dbHelper.getName(productCursor)
        view.findViewById<TextView>(R.id.brandContent).text = dbHelper.getBrand(productCursor)
        view.findViewById<TextView>(R.id.priceContent).text = priceText
        view.findViewById<TextView>(R.id.descriptionContent).text =dbHelper.getDescription(productCursor)
        view.findViewById<TextView>(R.id.descriptionContent).text =dbHelper.getDescription(productCursor)
        val image = view.findViewById<ImageView>(R.id.image)

        val request = ImageRequest.Builder(requireContext())
            .data(dbHelper.getImageUrl(productCursor))
            .target(image)
            .build()

        context?.imageLoader?.enqueue(request)*/
        return view
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