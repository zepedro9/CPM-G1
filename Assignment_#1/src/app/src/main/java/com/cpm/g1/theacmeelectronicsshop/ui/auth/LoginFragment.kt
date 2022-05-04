package com.cpm.g1.theacmeelectronicsshop.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.cpm.g1.theacmeelectronicsshop.ConfigHTTP
import com.cpm.g1.theacmeelectronicsshop.LoginActivity
import com.cpm.g1.theacmeelectronicsshop.R
import com.cpm.g1.theacmeelectronicsshop.dataClasses.UserLogin
import com.cpm.g1.theacmeelectronicsshop.httpService.Auth
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class LoginFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.login_btn).setOnClickListener { onClickLogin(view) }
        view.findViewById<Button>(R.id.signup_btn).setOnClickListener { onClickSignUp(view) }
    }

    fun onClickLogin(view: View) {
        val email = view.findViewById<TextInputEditText>(R.id.login_email_ed).text.toString()
        val password = view.findViewById<TextInputEditText>(R.id.login_password_ed).text.toString()

        val userJson = Gson().toJson(UserLogin(email, password))
        val address = "http://" + ConfigHTTP.BASE_ADDRESS + ":3000/api/auth/signin"
        Thread(Auth.Login(activity as LoginActivity?, address , userJson)).start()
    }

    /**
     * When the sign up button is pressed, the loginFragment is replaced by the registerFragment.
     */
    private fun onClickSignUp(view: View) {
        val registerFragment = RegisterFragment()
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.main_fragment_container, registerFragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit();
    }
}