package com.cpm.g1.theacmeelectronicsshop.ui.basket

import android.app.Activity
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import coil.imageLoader
import coil.request.ImageRequest
import com.cpm.g1.theacmeelectronicsshop.*
import com.cpm.g1.theacmeelectronicsshop.R.layout.product_row
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.BasketItem
import com.cpm.g1.theacmeelectronicsshop.dataClasses.basket.ItemQuantity
import com.cpm.g1.theacmeelectronicsshop.httpService.BasketDetails
import com.cpm.g1.theacmeelectronicsshop.ui.BasketHelper
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

const val PRODUCTS_ADDRESS: String = "http://" + ConfigHTTP.BASE_ADDRESS + ":3000/api/basket/products?ids="

@RequiresApi(Build.VERSION_CODES.N)
class BasketFragment : Fragment() {
    private val dbHelper by lazy { BasketHelper(context) }
/*    private var totalView: TextView? = null
    private val nf: NumberFormat = NumberFormat.getNumberInstance()*/
    private val uuid by lazy { context?.let { getUserUUID(it) } }
    private var itemQuantities: HashMap<Long,Int> = HashMap()
    private val basket: ArrayList<BasketItem> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get basket products
        val mainActivity = activity as MainActivity
        if(uuid == null || uuid.equals("")){
            throw Exception("UUID is null")
        }

        val basketCursor = dbHelper.getBasket(uuid!!)
        mainActivity.startManagingCursor(basketCursor)
        mapItemToQuantity(basketCursor)
        val basketIdsList = itemQuantities.keys.joinToString()

        // Build Basket Adapter
        val productList = view.findViewById<ListView>(R.id.basket_sv)
        productList.emptyView = view.findViewById(R.id.empty_list)
        Thread(BasketDetails(mainActivity, PRODUCTS_ADDRESS+basketIdsList, this::loadProducts)).start()
        productList.adapter = BasketAdapter()
/*
        // Product click
        productList.setOnItemClickListener { _, _, _, l -> onProductClick(l) }

        // Set Basket Total
        totalView = view.findViewById(R.id.total)
        setTotalPrice(dbHelper.getBasketTotal())

        // Checkout
        view.findViewById<Button>(R.id.checkout_btn).setOnClickListener { onCheckoutButtonClick() }*/
    }

    private fun mapItemToQuantity(cursor: Cursor): HashMap<Long,Int> {
        cursor.moveToFirst()
        while(!cursor.isAfterLast) {
            itemQuantities[cursor.getLong(0)] = cursor.getInt(1)
            cursor.moveToNext();
        }
        return itemQuantities
    }

    private fun loadProducts(act: Activity, jsonResponse: JSONObject){
        val jsonProducts = jsonResponse.getJSONArray("products")
        for(i in 0 until jsonProducts.length()){
            val jsonProduct = jsonProducts.getJSONObject(i)
            val prodId = jsonProduct.getLong("id")
            val prodName = jsonProduct.getString("name")
            val prodBrand = jsonProduct.getString("brand")
            val prodPrice = jsonProduct.getString("price").toFloat()
            val prodDesc = jsonProduct.getString("description")
            val prodImage = jsonProduct.getString("image_url")
            val quantity = itemQuantities.getValue(prodId);
            basket.add(BasketItem(prodId,prodName, prodBrand, prodPrice, prodDesc, prodImage, quantity))
        }
    }

/*    private fun onCheckoutButtonClick(){
        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val intent = Intent(context, CheckoutActivity::class.java)
        startActivity(intent)
    }

    private fun onProductClick(id: Long){
        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val intent = Intent(context, ProductDetailsActivity::class.java).also{
            it.putExtra("pos", id.toString())
        }
        startActivity(intent)
    }*/

    inner class BasketAdapter(): ArrayAdapter<BasketItem>(this.requireContext(), product_row, basket) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val row = convertView ?: (requireContext() as AppCompatActivity).layoutInflater.inflate(product_row, parent, false)
            val product = basket[position]
            println("HEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEE")
            println(product)

            // Product Buttons
            val plusButton = row.findViewById<ImageButton>(R.id.plus_button)
            val minusButton = row.findViewById<ImageButton>(R.id.minus_button)
            val deleteButton = row.findViewById<ImageButton>(R.id.product_delete)

            // Set product details
            val id = product.id
            val quantity = product.quantity
            val price = product.price
            val priceText = getString(R.string.product_price, price*quantity)

            row.findViewById<TextView>(R.id.price_text)?.text = priceText
            row.findViewById<TextView>(R.id.product_quantity)?.text = quantity.toString()
            row.findViewById<TextView>(R.id.name_text)?.text = product.name
            row.findViewById<TextView>(R.id.brand_text)?.text = product.brand

            // Set product image from URL
            val image = row.findViewById<ImageView>(R.id.product_image)
            val request = ImageRequest.Builder(context)
                .data(product.image_url)
                .target(image).build()
            context.imageLoader.enqueue(request)

            // Buttons click listeners
/*            deleteButton.setOnClickListener { onDeleteClickListener(row, id) }
            plusButton.setOnClickListener{ onPlusClickListener(row, id) }
            minusButton.setOnClickListener{ onMinusClickListener(row, id) }*/
            return row
        }

/*        @Suppress("DEPRECATION")
        private fun onDeleteClickListener(productView: View, id: Int){
            // Update total price
            val price = priceViewToFloat(productView.findViewById(R.id.price_text))
            val newTotal = priceViewToFloat(totalView!!) - price
            setTotalPrice(newTotal)

            // Delete Item
            dbHelper.deleteById(id)
            cursor.requery()
            notifyDataSetChanged()
        }

        private fun onPlusClickListener(productView: View, id: Int) {
            val priceView = productView.findViewById<TextView>(R.id.price_text)
            val quantityText = productView.findViewById<TextView>(R.id.product_quantity)
            val quantity = quantityText.text.toString().toInt()
            val price = priceViewToFloat(priceView)
            val unitPrice = price / quantity
            val newQuantity = quantity + 1
            val newTotal = priceViewToFloat(totalView!!) + unitPrice

            // Update quantity
            quantityText.text = newQuantity.toString()
            dbHelper.updateQuantity(id, newQuantity)

            // Update total price
            setTotalPrice(newTotal)

            // Update item price
            val newPrice = price + unitPrice
            val priceText = getString(R.string.product_price, newPrice)
            priceView.text = priceText
        }

        private fun onMinusClickListener(productView: View, id: Int){
            val quantityText = productView.findViewById<TextView>(R.id.product_quantity)
            val quantity = quantityText.text.toString().toInt()
            val newQuantity = quantity - 1

            if(newQuantity == 0) {
                // Delete item from basket
                onDeleteClickListener(productView, id)
                return
            }

            // Delete one unit
            val priceView = productView.findViewById<TextView>(R.id.price_text)
            val price = priceViewToFloat(priceView)
            val unitPrice = price / quantity

            // Update quantity
            quantityText.text = newQuantity.toString()
            dbHelper.updateQuantity(id, newQuantity)

            // Update total
            val newTotal =  priceViewToFloat(totalView!!) - unitPrice
            setTotalPrice(newTotal)

            // Update item price
            val newPrice = price - unitPrice
            val priceText = getString(R.string.product_price, newPrice)
            priceView.text = priceText

        }*/
    }
/*
    private fun setTotalPrice(total: Float){
        val priceText = getString(R.string.product_price, total)
        totalView!!.text = priceText
    }

    // Utils
    private fun priceViewToFloat(priceView: TextView): Float {
        return priceStringToFloat(priceView.text.toString())
    }

    private fun priceStringToFloat(strPrice: String): Float {
        return nf.parse(strPrice.dropLast(1)).toFloat()
    }*/
}