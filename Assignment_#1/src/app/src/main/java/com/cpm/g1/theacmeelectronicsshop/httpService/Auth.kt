package com.cpm.g1.theacmeelectronicsshop.httpService

import android.util.JsonWriter
import com.cpm.g1.theacmeelectronicsshop.LoginActivity
import com.cpm.g1.theacmeelectronicsshop.dataClasses.User
import com.google.gson.Gson
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class Auth {

    private fun readStream(input: InputStream): String {
        var reader: BufferedReader? = null
        var line: String?
        val response = StringBuilder()
        try {
            reader = BufferedReader(InputStreamReader(input))
            while (reader.readLine().also { line = it } != null)
                response.append(line)
        } catch (e: IOException) {
            return "readStream: " + e.message
        } finally {
            reader?.close()
        }
        return response.toString()
    }

    /**
     * Register a user in the platform.
     * https://documenter.getpostman.com/view/15267940/UyrHetKC#776e1031-4d70-4576-9749-ecce8b39248e
     */
    class SignUp(val act: LoginActivity?, val baseAddress: String, val user: User) : Runnable {
        override fun run() {
            val url: URL
            var urlConnection: HttpURLConnection? = null
            try {
                url = URL("http://$baseAddress:3000/api/auth/signup")

                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.setRequestProperty("Content-Type", "application/json")
                urlConnection.doOutput = true
                urlConnection.doInput = true
                urlConnection.useCaches = false

                val outputStream = DataOutputStream(urlConnection.outputStream)

                val body: String = Gson().toJson(user);
                outputStream.writeBytes(body)
                outputStream.flush()
                outputStream.close()

                // get response
                val responseCode = urlConnection.responseCode

            } finally {
                urlConnection?.disconnect()
            }
        }
    }

}