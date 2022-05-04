package com.cpm.g1.theacmeelectronicsshop.dataClasses

import java.io.Serializable

data class UserLogin  (
    val email: String,
    val password: String
) : Serializable {

}