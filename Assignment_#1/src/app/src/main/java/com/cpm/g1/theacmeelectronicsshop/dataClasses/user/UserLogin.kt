package com.cpm.g1.theacmeelectronicsshop.dataClasses.user

import java.io.Serializable

data class UserLogin  (
    val email: String,
    val password: String
) : Serializable {

}