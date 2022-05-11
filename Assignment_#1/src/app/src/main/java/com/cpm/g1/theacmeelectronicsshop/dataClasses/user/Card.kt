package com.cpm.g1.theacmeelectronicsshop.dataClasses.user

import java.io.Serializable

data class Card(
    var cardType: String,
    var number: String,
    var expirationDate: String,
) : Serializable {

}
