package com.cpm.g1.theacmeelectronicsshop.dataClasses.basket

import java.io.Serializable

data class SignedBasket(
    var basket: Basket,
    var signature: String
): Serializable {
}