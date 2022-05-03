package com.cpm.g1.theacmeelectronicsshop.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.cpm.g1.theacmeelectronicsshop.*
import com.cpm.g1.theacmeelectronicsshop.dataClasses.Card
import com.cpm.g1.theacmeelectronicsshop.dataClasses.User
import com.cpm.g1.theacmeelectronicsshop.httpService.Auth
import com.cpm.g1.theacmeelectronicsshop.ui.auth.LoginFragment
import com.google.gson.Gson

class RegisterFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_register, container, false)
        view.findViewById<Button>(R.id.login_btn).setOnClickListener { onClickLogin() }
        view.findViewById<Button>(R.id.reg_signup_btn).setOnClickListener { onClickSignup(view) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun onClickSignup(view: View){
        val user = createUserObj(view)
        Thread(Auth.SignUp(activity as LoginActivity?, ConfigHTTP.BASE_ADDRESS, user)).start()
    }


    private fun createUserObj(view: View): User {
        var name = view.findViewById<EditText>(R.id.reg_name_ed).text.toString()
        var address = view.findViewById<EditText>(R.id.reg_address_ed).text.toString()
        var nif = view.findViewById<EditText>(R.id.reg_nif_ed).text.toString().toInt()
        var email = view.findViewById<EditText>(R.id.reg_email_ed).text.toString()
        var password = view.findViewById<EditText>(R.id.reg_password_ed).text.toString()
        var card = createCardObj(view)
        Cryptography().generateKey(activity as LoginActivity);
        val pk = Cryptography().getPubKey();
        return User(pk, name, address, nif, email, password, card)
    }

    private fun createCardObj(view: View): Card {
        var type = view.findViewById<EditText>(R.id.reg_card_type_ed).text.toString()
        var number = view.findViewById<EditText>(R.id.reg_card_number_ed).text.toString()
        var expirationDate = view.findViewById<EditText>(R.id.reg_card_expiration_ed).text.toString()
        return Card(type, number, expirationDate)
    }
}

