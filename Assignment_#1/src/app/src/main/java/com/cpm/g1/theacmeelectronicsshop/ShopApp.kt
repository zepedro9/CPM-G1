package com.cpm.g1.theacmeelectronicsshop

import android.app.Application

class ShopApp: Application() {
    val products: ArrayList<Product> = ArrayList()
    var adapter: ProductAdapter? = null
}