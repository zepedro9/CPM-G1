package com.cpm.g1.theacmeelectronicsshop.ui.basketHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.cpm.g1.theacmeelectronicsshop.*
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.Basket
import com.cpm.g1.theacmeelectronicsshop.httpService.GetHistory
import com.google.gson.Gson


class BasketHistoryList : Fragment() {

    val HISTORY_ADDRESS: String = "http://" + ConfigHTTP.BASE_ADDRESS + ":3000/api/basket/history"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_basket_history, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = getEncryptedSharedPreferences(requireActivity().applicationContext)
        val uuid = sharedPreferences.getString("uuid", "") ?: throw Exception("Missing uuid")
        var body = Gson().toJson(hashMapOf("userUUID" to uuid));
        val signature = Cryptography().signContent(body);
        val mainActivity = activity as MainActivity
        val signedContent = Gson().toJson(hashMapOf("userUUID" to uuid, "signature" to signature))
        Thread(GetHistory(mainActivity, HISTORY_ADDRESS, signedContent)).start()

        setupAdapter()
    }

    fun setupAdapter() {
        val listView = view?.findViewById<ListView>(R.id.basket_sv)
        listView?.adapter = (activity as MainActivity).adapterBasket

        listView?.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val basket = (activity as MainActivity).historyBasket.get(position)
                changeToBasketHistoryProducts(basket)
                println(basket)
            }
    }



    /**
     * Changes the view to a fragment that shows the items inside the history.
     */
    fun changeToBasketHistoryProducts(basket: Basket){
        val tag : String? = BasketHistoryList::class.simpleName
        println(tag)
        val basketHistoryProducts = BasketHistoryProducts(basket)
        val fragmentManager = (activity as MainActivity).supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, basketHistoryProducts)
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()

    }


}