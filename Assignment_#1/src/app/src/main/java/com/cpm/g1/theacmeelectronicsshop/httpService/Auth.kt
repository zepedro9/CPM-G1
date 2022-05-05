package com.cpm.g1.theacmeelectronicsshop.httpService

import com.cpm.g1.theacmeelectronicsshop.LoginActivity
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.readStream
import com.cpm.g1.theacmeelectronicsshop.ui.auth.LoginFragment
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class Auth {
    /**
     * Make a request to the server
     * https://documenter.getpostman.com/view/15267940/UyrHetKC#776e1031-4d70-4576-9749-ecce8b39248e
     */
    class SignUp(val act: LoginActivity?, val address: String, val body: String) : Runnable {
        override fun run() {
            val url: URL
            var urlConnection: HttpURLConnection? = null
            try {
                url = URL(address)
                println(address)
                println(body)
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.setRequestProperty("Content-Type", "application/json")
                urlConnection.doOutput = true
                urlConnection.doInput = true
                urlConnection.useCaches = false

                val outputStream = DataOutputStream(urlConnection.outputStream)

                outputStream.writeBytes(body)
                outputStream.flush()
                outputStream.close()

                // get response
                val responseCode = urlConnection.responseCode

                if (responseCode == 200) {
                    changeToSignInFragment(act)
                }
            } catch (err: Exception) {
                println(err);
            } finally {
                urlConnection?.disconnect()
            }
        }

        fun changeToSignInFragment(activity: LoginActivity?){
            val loginFragment= LoginFragment()
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.main_fragment_container, loginFragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit();
        }
    }

    class Login(val act: LoginActivity?, val address: String, val body: String) : Runnable {
        override fun run() {
            val url: URL
            var urlConnection: HttpURLConnection? = null
            try {
                url = URL(address)
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.setRequestProperty("Content-Type", "application/json")
                urlConnection.doOutput = true
                urlConnection.doInput = true
                urlConnection.useCaches = false

                val outputStream = DataOutputStream(urlConnection.outputStream)

                outputStream.writeBytes(body)
                outputStream.flush()
                outputStream.close()

                // get response
                val responseCode = urlConnection.responseCode

                if(responseCode == 200){
                    val response = readStream(urlConnection.inputStream)
                    val jsonResponse = JSONObject(response)
                    val uuid = jsonResponse.getString("uuid")
                }
            } catch (err: Exception) {
                println(err);
            } finally {
                urlConnection?.disconnect()
            }
        }


    }


}