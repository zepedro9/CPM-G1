package com.cpm.g1.theacmeelectronicsshop.dataClasses

import com.cpm.g1.theacmeelectronicsshop.PubKey
import java.io.Serializable

data class User (
    val pk: PubKey,
    val name: String,
    val address: String,
    val NIF: Int,
    val email: String,
    val password: String,   // encrypted.
    val card: Card,
) : Serializable {

}
