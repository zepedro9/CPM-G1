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
import com.google.gson.Gson

class RegisterFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

    fun setupCardType(view: View){
        val textField = view.findViewById<AutoCompleteTextView>(R.id.reg_card_type_ed)
        val items = listOf("Credit", "Debit")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        textField?.setAdapter(adapter)
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

    // TODO: fix bug in validation
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onClickSignup(view: View) {
        val isValidFields = validateFields(view)
        if (isValidFields) {
            val user = createUserObj(view)
            val userJson = Gson().toJson(user)
            val address = "http://" + ConfigHTTP.BASE_ADDRESS + ":3000/api/auth/signup"
            Thread(SignUp(activity as LoginActivity, address , userJson)).start()
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
        var containsError = false
        val fields = arrayOf(
            R.id.reg_name_ed,
            R.id.reg_address_ed,
            R.id.reg_nif_ed,
            R.id.reg_email_ed,
            R.id.reg_password_ed,
            R.id.reg_card_type_ed,
            R.id.reg_card_number_ed,
            R.id.reg_card_expiration_ed
        )

        val fieldSizes = arrayOf(
            -1, 10, 9, 5, 7, -1, 15, -1
        )

        if (!checkFieldsFilled(view, fields)) containsError = true
        if (!checkSizes(view, fieldSizes, fields)) containsError = true

        // Check email pattern.
        val emailField = view.findViewById<EditText>(R.id.reg_email_ed)
        if (!Patterns.EMAIL_ADDRESS.matcher(emailField.text.toString()).matches()) {
            addErrorMessage(emailField, "Invalid email format")
            containsError = true
        }

        return !containsError

    }

    /**
     * Check the sizes of each.
     * @param fieldSize Size of the fields. Fields with negative values are considered of not
     * having minimum size.
     */
    private fun checkSizes(view: View, fieldSize: Array<Int>, fields: Array<Int>): Boolean {
        var hasMinSize = true
        for (i in 0..fieldSize.size-1) {
            val field: EditText = view.findViewById<EditText>(fields[i])
            if (fieldSize[i] != -1 && (field.text == null || field.text.length < fieldSize[i])) {
                addErrorMessage(field, "Minimum size is " + fieldSize[i])
                hasMinSize = false
            }
        }
        return hasMinSize
    }

    private fun addErrorMessage(field: EditText, errorMessage: String){
        // The field might be null. Initialize on that case.
        if (field.error.isNullOrBlank())
            field.error = "";

        var splitChar = " "
        if (field.error.toString().isNotEmpty())
            splitChar = "\n"
        field.error = field.error.toString() + splitChar + errorMessage
    }

    /**
     * Check if all fields are filled.
     * All the fields must be checked before returning.
     */
    private fun checkFieldsFilled(view: View, fields: Array<Int>): Boolean {
        var isFilled = true
        for (field in fields) {
            if (!checkMandatoryField(view.findViewById(field)))
                isFilled = false
        }

        return isFilled;
    }

    /**
     * Checks if a mandatory field is filled.
     */
    private fun checkMandatoryField(field: EditText): Boolean {
        if (field.text.toString().isEmpty()) {
            field.error = "Field is required";
            return false
        }
        return true;
    }
}

