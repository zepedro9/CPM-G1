package com.cpm.g1.theacmeelectronicsshop

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    return EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

fun getUserUUID(context: Context): String {
    val sharedPreferences = getEncryptedSharedPreferences(context)
    return sharedPreferences.getString("uuid", "") ?: ""
}

fun clearUserUUID(context: Context) {
    val sharedPreferences = getEncryptedSharedPreferences(context)
    sharedPreferences.edit().remove("uuid").apply()
}