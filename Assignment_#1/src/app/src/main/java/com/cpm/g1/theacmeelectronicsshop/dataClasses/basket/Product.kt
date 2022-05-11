package com.cpm.g1.theacmeelectronicsshop.dataClasses.basket

import java.io.Serializable

data class Product(
    var id: Long,
    var quantity: Int
): Serializable {}
