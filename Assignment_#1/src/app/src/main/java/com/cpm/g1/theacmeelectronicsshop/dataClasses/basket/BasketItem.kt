package com.cpm.g1.theacmeelectronicsshop.dataClasses.basket

import java.io.Serializable

data class BasketItem(
    var id: Long,
    var name: String,
    var brand: String,
    var price: Float,
    var description: String,
    var image_url: String,
    var quantity: Int
): Serializable

