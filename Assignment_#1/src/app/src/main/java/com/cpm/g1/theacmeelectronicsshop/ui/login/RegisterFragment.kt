package com.cpm.g1.theacmeelectronicsshop.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.cpm.g1.theacmeelectronicsshop.R

class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }



    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //
        super.onViewCreated(view, savedInstanceState);

    }

    fun onSubmit(view: View){
        val name = view.findViewById<EditText>(R.id.name_etxt);
        val address = view.findViewById<EditText>(R.id.address_etxt);
        val nif = view.findViewById<EditText>(R.id.nif_etxt);
        val cardType = view.findViewById<EditText>(R.id.card_type_etxt);
        val cardNumber = view.findViewById<EditText>(R.id.card_number_etxt);
        val cardExpirationDate = view.findViewById<EditText>(R.id.card_expiration_date);

        // TODO: make the validation.
    }


}