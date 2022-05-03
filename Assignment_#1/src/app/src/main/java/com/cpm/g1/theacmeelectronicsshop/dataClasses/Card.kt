package com.cpm.g1.theacmeelectronicsshop.dataClasses

import java.io.Serializable

data class Card(
    var type: String,
    var number: String,
    var expirationDate: String,
) : Serializable {

}
