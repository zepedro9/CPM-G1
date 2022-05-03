package com.cpm.g1.theacmeelectronicsshop.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.cpm.g1.theacmeelectronicsshop.R

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
        view.findViewById<Button>(R.id.login_btn).setOnClickListener { onClickLogin() }
        view.findViewById<Button>(R.id.signup_btn).setOnClickListener { onClickSignUp(view) }
    }

    fun onClickLogin() {

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