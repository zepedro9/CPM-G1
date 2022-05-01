package com.cpm.g1.theacmeelectronicsshop.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.cpm.g1.theacmeelectronicsshop.R

class RegisterFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.login_btn).setOnClickListener { onClickLogin() }
    }

    /**
     * When login button is pressed, the registerFragment is replaced by the loginFragment.
     */
    private fun onClickLogin() {
        val loginFragment = LoginFragment()
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.main_fragment_container, loginFragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }


}