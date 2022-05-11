package com.cpm.g1.theacmeelectronicsshop

import android.os.Build
import android.security.KeyPairGeneratorSpec
import androidx.annotation.RequiresApi
import okio.Utf8
import java.io.Serializable
import java.math.BigInteger
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.Signature
import java.security.interfaces.RSAPrivateKey
import java.util.*
import javax.crypto.Cipher
import javax.security.auth.x500.X500Principal

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


    @RequiresApi(Build.VERSION_CODES.O)
    fun getPubKey(): String {

        try {
            // Retrieve the entry in the keystore
            val entry = KeyStore.getInstance(KeyProperties.ANDROID_KEYSTORE).run {
                load(null)
                getEntry(KeyProperties.KEY_ALIAS, null)
            }
            val pub = (entry as KeyStore.PrivateKeyEntry).certificate.publicKey
            return addHeaders(Base64.getEncoder().encodeToString(pub.encoded))
        } catch (ex: Exception) {
            println(ex)
        }
        return ""
    }

    fun signContent(content: String): String {
        return try {
            val entry = KeyStore.getInstance(KeyProperties.ANDROID_KEYSTORE).run {
                load(null)
                getEntry(KeyProperties.KEY_ALIAS, null)
            }
            val prKey = (entry as KeyStore.PrivateKeyEntry).privateKey
            val sg = Signature.getInstance(KeyProperties.SIGN_ALGO)
            sg.initSign(prKey)
            sg.update(content.toByteArray())
            val result = sg.sign()
            byteArrayToHex(result)
        } catch  (e: Exception) {
            ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decrypt(content: String) : String {
        return try {
            val entry = KeyStore.getInstance(KeyProperties.ANDROID_KEYSTORE).run {
                load(null)
                getEntry(KeyProperties.KEY_ALIAS, null)
            }
            val privateKey = (entry as KeyStore.PrivateKeyEntry).privateKey
            val cipher: Cipher = Cipher.getInstance(KeyProperties.ENC_ALGO)
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            val clear: ByteArray = cipher.doFinal(Base64.getDecoder().decode(content))
            clear.toString(StandardCharsets.UTF_8)
        } catch (e: Exception) {
            println(e)
            ""
        }
    }

    fun byteArrayToHex(ba: ByteArray): String {
        val sb = StringBuilder(ba.size * 2)
        for (b in ba) sb.append(String.format("%02x", b))
        return sb.toString()
    }

    private fun addHeaders(key: String): String {
        val headline = "-----BEGIN PUBLIC KEY-----\n"
        val footline = "\n-----END PUBLIC KEY-----\n"
        return headline + key + footline
    }
}
