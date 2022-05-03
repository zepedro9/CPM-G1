package com.cpm.g1.theacmeelectronicsshop

import android.security.KeyPairGeneratorSpec
import java.io.Serializable
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*
import javax.security.auth.x500.X500Principal

data class PubKey(var modulus: ByteArray, var exponent: ByteArray): Serializable

class Cryptography {

    /**
     * Checks if the RSA keys has already been created previously and thus stored at the
     * android keystore.
     * @return True when the keys are already stored.
     */
    fun checkKeys(): Boolean {
        val entry = KeyStore.getInstance(KeyProperties.ANDROID_KEYSTORE).run {
            load(null);
            getEntry(KeyProperties.KEY_ALIAS, null)
        }

        return (entry != null)
    }

    /**
     * Generates the security key pair and store it in the keystore.
     */
    fun generateKey(act: LoginActivity): Boolean {
        if (!checkKeys()) {
            try {
                // Generate expire date.
                val startDate = GregorianCalendar()
                val endDate = GregorianCalendar()
                endDate.add(Calendar.YEAR, 1)

                // Create RSA key pair and store it in the Android Keystore.
                val keyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM,
                    KeyProperties.ANDROID_KEYSTORE
                )

                // Create the key pair
                val spec = KeyPairGeneratorSpec.Builder(act)
                    .setKeySize(KeyProperties.KEY_SIZE)
                    .setAlias(KeyProperties.KEY_ALIAS)
                    .setSubject(X500Principal("CN=" + KeyProperties.KEY_ALIAS))
                    .setSerialNumber(BigInteger.valueOf(KeyProperties.serialNr))
                    .setStartDate(startDate.time)
                    .setEndDate(endDate.time)
                    .build()
                keyPairGenerator.initialize(spec)
                keyPairGenerator.generateKeyPair()
            } catch (ex: Exception) {
                println(ex.message)
                return false
            }
        }
        return true
    }


    fun getPubKey(): PubKey {

        val publicKey = PubKey(ByteArray(0), ByteArray(0))
        try {
            // Retrieve the entry in the keystore
            val entry = KeyStore.getInstance(KeyProperties.ANDROID_KEYSTORE).run {
                load(null)
                getEntry(KeyProperties.KEY_ALIAS, null)
            }

            val pub = (entry as KeyStore.PrivateKeyEntry).certificate.publicKey
            publicKey.modulus = (pub as RSAPublicKey).modulus.toByteArray()
            publicKey.exponent = pub.publicExponent.toByteArray()
        } catch (ex: Exception) {
            println(ex)
        }
        return publicKey
    }

    fun getPrivKeyExponent(): ByteArray {
        var exponent = ByteArray(0)
        try {
            // Retrieve the entry in the keystore
            val entry = KeyStore.getInstance(KeyProperties.ANDROID_KEYSTORE).run {
                load(null)
                getEntry(KeyProperties.KEY_ALIAS, null)
            }

            val privateKey = (entry as KeyStore.PrivateKeyEntry).privateKey
            exponent = (privateKey as RSAPrivateKey).privateExponent.toByteArray()
        } catch (ex: Exception) {
            println(ex)

        }
        return exponent
    }


}
