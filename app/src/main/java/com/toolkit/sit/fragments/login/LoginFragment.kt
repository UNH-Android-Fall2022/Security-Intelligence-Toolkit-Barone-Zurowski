package com.toolkit.sit.fragments.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.toolkit.sit.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // https://stackoverflow.com/questions/13216916/how-to-replace-the-activitys-fragment-from-the-fragment-itself
    private var TAG = "LOGIN_FRAGMENT"
//    private lateinit var auth: FirebaseAuth
    private lateinit var loginButton: Button
    private lateinit var forgotPasswordButton: Button
    private lateinit var signUpButton: Button

    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.login_fragment, container, false)

        loginButton = view.findViewById(R.id.buttonLogin)
        forgotPasswordButton = view.findViewById(R.id.buttonForgotPassword)
        signUpButton = view.findViewById(R.id.buttonSignUp)
        usernameField = view.findViewById(R.id.usernameField)
        passwordField = view.findViewById(R.id.passwordField)
        return view
    }

    override fun onStart() {
        loginButton.setOnClickListener {
            val user = usernameField.text
            val password = passwordField.text
            Log.d(TAG, "Login Data: ${user}:${password}")
        }
        forgotPasswordButton.setOnClickListener {
            Log.d(TAG, "Forgot Button")
        }
        signUpButton.setOnClickListener {
            Log.d(TAG, "Sign up button.")
        }

        super.onStart()
    }

//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//    }


}