package com.toolkit.sit.fragments.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.toolkit.sit.R
import com.toolkit.sit.util.Util

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {
    private var TAG = "SIGNUP_FRAGMENT"

    private lateinit var auth: FirebaseAuth

    private lateinit var signUpButton: Button
    private lateinit var backButton: Button
    private lateinit var emailField: EditText
    private lateinit var passField: EditText
    private lateinit var passValidateField: EditText
    private lateinit var applicationContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_sign_up, container, false)

        signUpButton = view.findViewById(R.id.button_sign_up)
        backButton = view.findViewById(R.id.button_back)
        emailField = view.findViewById(R.id.email_field)
        passField = view.findViewById(R.id.pass_field)
        passValidateField = view.findViewById(R.id.validate_field)
        applicationContext = view.context.applicationContext

        return view
    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance();

        backButton.setOnClickListener {
            goBack()
        }
        signUpButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passField.text.toString()
            val validatePass = passValidateField.text.toString()

            signUpUser(email, password, validatePass)

        }
    }

    private fun goBack() {
        Util.mainFragment(activity, LoginFragment())
    }

    private fun signUpUser(email: String, password: String, validatePass: String)  {
        Log.d(TAG, "Login Data: ${email}:${password}:${validatePass}")

        if(Util.checkFieldsIfEmpty(email, password, validatePass)) {
            Util.popUp(applicationContext, "Please enter both username and password!!", Toast.LENGTH_LONG)

            return
        }
        else if (password != validatePass) {
            Util.popUp(applicationContext, "Password and Confirm Password do not match", Toast.LENGTH_LONG)

            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        Util.popUp(applicationContext, "SignUp Sucessful!", Toast.LENGTH_LONG)
                        goBack()
                    } else {
                        Util.popUp(applicationContext, "SignUp Failed!", Toast.LENGTH_LONG)
                    }
                })
    }

}