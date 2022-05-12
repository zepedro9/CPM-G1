package com.cpm.g1.theacmeelectronicsshop.ui.auth

import android.os.Build
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.cpm.g1.theacmeelectronicsshop.*
import com.cpm.g1.theacmeelectronicsshop.dataClasses.user.Card
import com.cpm.g1.theacmeelectronicsshop.dataClasses.user.User
import com.cpm.g1.theacmeelectronicsshop.httpService.SignUp
import com.cpm.g1.theacmeelectronicsshop.security.Cryptography
import com.cpm.g1.theacmeelectronicsshop.utils.ConfigHTTP
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_register, container, false)
        setupCardType(view)
        view.findViewById<Button>(R.id.login_btn).setOnClickListener { onClickLogin() }
        view.findViewById<Button>(R.id.reg_signup_btn).setOnClickListener { onClickSignup(view) }
        return view
    }

    fun setupCardType(view: View) {
        val textField = view.findViewById<AutoCompleteTextView>(R.id.reg_card_type_ed)
        val items = listOf("Credit", "Debit")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        textField.setText(getString(R.string.credit))
        textField?.setAdapter(adapter)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onClickSignup(view: View) {
        val isValidFields = validateFields(view)
        if (isValidFields) {
            val user = createUserObj(view)
            val userJson = Gson().toJson(user)
            val address = "http://" + ConfigHTTP.BASE_ADDRESS + ":3000/api/auth/signup"
            Thread(SignUp(activity as LoginActivity, address, userJson)).start()
        } else {
            Toast.makeText(requireContext(), "Invalid fields", Toast.LENGTH_LONG).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createUserObj(view: View): User {
        val name = view.findViewById<EditText>(R.id.reg_name_ed).text.toString()
        val address = view.findViewById<EditText>(R.id.reg_address_ed).text.toString()
        val nif = view.findViewById<EditText>(R.id.reg_nif_ed).text.toString().toInt()
        val email = view.findViewById<EditText>(R.id.reg_email_ed).text.toString()
        val password = view.findViewById<EditText>(R.id.reg_password_ed).text.toString()
        val card = createCardObj(view)
        Cryptography().generateKey(activity as LoginActivity);
        val pk = Cryptography().getPubKey();
        return User(pk, name, address, nif, email, password, card)
    }


    private fun createCardObj(view: View): Card {
        val type = view.findViewById<EditText>(R.id.reg_card_type_ed).text.toString()
        val number = view.findViewById<EditText>(R.id.reg_card_number_ed).text.toString()
        val expirationDate =
            view.findViewById<EditText>(R.id.reg_card_expiration_ed).text.toString()
        return Card(type, number, expirationDate)
    }

    // FIELDS VALIDATION ==============================

    private fun validateFields(view: View): Boolean {
        var isValid = true

        isValid = isValid.and(validateField( view, R.id.reg_name_ed, 5, 200));
        isValid = isValid.and(validateField( view, R.id.reg_address_ed, 7, 200));
        isValid = isValid.and(validateField( view, R.id.reg_nif_ed, 9, 9));
        isValid = isValid.and(validateField( view, R.id.reg_password_ed, 7, 200));
        isValid = isValid.and(validateField( view, R.id.reg_card_number_ed, 15, 200));
        isValid = isValid.and(validateField( view, R.id.reg_email_ed, 7, 200 ));

        // Check email pattern.
        val emailField = view.findViewById<TextInputEditText>(R.id.reg_email_ed)
        if (!Patterns.EMAIL_ADDRESS.matcher(emailField.text.toString()).matches()) {
            val errorMessage = "Invalid email format"
            if (emailField.error.isNullOrEmpty()) {
                emailField.error = errorMessage
            } else {
                emailField.error = "${emailField.error} \n$errorMessage"
            }
            isValid = false;
        }

        // Check email pattern
        val dateField = view.findViewById<TextInputEditText>(R.id.reg_card_expiration_ed)
        if (!checkDateFormat(dateField)) {
            dateField.error = "Expecting format MM-yyyy"
            isValid = false
        }

        return isValid

    }


    private fun validateField(view: View, id: Int, min: Int, max: Int): Boolean{
        var isValid = true
        val editView = view.findViewById<TextInputEditText>(id)
        editView.error = ""
        isValid = isValid.and(setErrorOnEmpty(editView))
        isValid = isValid.and(setMinMax(editView, min, max))

        if (editView.error.isNullOrEmpty()) {
            editView.error = null
        }
        return isValid;
    }

    private fun setErrorOnEmpty(editView: TextInputEditText): Boolean{
        if (editView.text.isNullOrEmpty()){
            editView.error = "${editView.error}This field must be filled"
            return false;
        }
        return true
    }
    private fun setMinMax(editView: TextInputEditText, min: Int, max: Int): Boolean{
        var isValid = true
        var separator = "\n";
        if (editView.error.isNullOrEmpty())
            separator = "";
        if (max != -1 && editView.length() > max) {
            editView.error = "${editView.error} ${separator}Max size is $max"
            isValid = false
        } else if (min != -1 && editView.length() < min){
            editView.error = "${editView.error} ${separator}Min size is $min"
            isValid = false
        }
        return isValid
    }



    private fun checkDateFormat(field: EditText): Boolean {
        val regex = """(1[0-2]|0[1-9]|[1-9])-(20\d{2}|19\d{2})""".toRegex()
        return regex.matches(field.text.toString());

    }
}
