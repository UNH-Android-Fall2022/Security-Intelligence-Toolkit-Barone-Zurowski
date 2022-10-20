package com.toolkit.sit.fragments.login

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
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
import com.toolkit.sit.MainActivity
import com.toolkit.sit.R

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
        (activity as MainActivity?)?.setFragment(LoginFragment())
    }

    private fun signUpUser(email: String, password: String, validatePass: String) {
        Log.d(TAG, "Login Data: ${email}:${password}:${validatePass}")
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(validatePass)) {
            Toast.makeText(applicationContext,
                "Please enter both username and password!!",
                Toast.LENGTH_LONG)
                .show()
            return
        }

        if (password != validatePass) {
            Toast.makeText(applicationContext, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Signup successful!!",
                            Toast.LENGTH_LONG
                        ).show()

                        // if sign-in is successful
                        // intent to home activity
                        goBack()
                    } else {
                        // sign-in failed
                        Toast.makeText(
                            applicationContext,
                            "Signup failed!!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })


    }

}