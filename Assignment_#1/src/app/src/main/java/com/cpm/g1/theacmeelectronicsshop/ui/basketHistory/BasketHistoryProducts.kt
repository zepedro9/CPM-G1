package com.cpm.g1.theacmeelectronicsshop.ui.basketHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cpm.g1.theacmeelectronicsshop.ConfigHTTP
import com.cpm.g1.theacmeelectronicsshop.MainActivity
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.Basket
import com.cpm.g1.theacmeelectronicsshop.httpService.GetProductsList


class BasketHistoryProducts(val basket: Basket) : Fragment() {
    val LIST_ADDRESS: String = "http://${ConfigHTTP.BASE_ADDRESS}:3000/api/products/list?"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_basket_history_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val productsList = view.findViewById<ListView>(R.id.basket_sv)
        productsList.adapter = (activity as MainActivity).adapterProducts

        val requestURI = buildProductListURI()
        val mainActivity = activity as MainActivity
        Thread(GetProductsList(mainActivity, requestURI)).start()
    }


    /**
     * Function that adds the products as queries to the request of a list of
     * products.
     */
    fun buildProductListURI(): String{
        var uri = LIST_ADDRESS
        for (product in basket.products){
            uri += "prod=" + product.id + "&"
        }
        return uri
    }

}
