package com.toolkit.sit.fragments.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.toolkit.sit.R
import com.toolkit.sit.SITActivity
import com.toolkit.sit.util.Util


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // https://stackoverflow.com/questions/13216916/how-to-replace-the-activitys-fragment-from-the-fragment-itself
    private var TAG = "LOGIN_FRAGMENT"
    private lateinit var auth: FirebaseAuth
    private lateinit var loginButton: Button
    private lateinit var forgotPasswordButton: Button
    private lateinit var signUpButton: Button

    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText

    private lateinit var applicationContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.login_fragment, container, false)

        // get all the required buttons and fields
        loginButton = view.findViewById(R.id.buttonLogin)
        forgotPasswordButton = view.findViewById(R.id.buttonForgotPassword)
        signUpButton = view.findViewById(R.id.buttonSignUp)
        usernameField = view.findViewById(R.id.usernameField)
        passwordField = view.findViewById(R.id.passwordField)
        applicationContext = view.context.applicationContext
        return view
    }

    override fun onStart() {
        // sets fragment based on option chose.
        auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener {
            loginUser()
        }
        forgotPasswordButton.setOnClickListener {
            Log.d(TAG, "Forgot Button")
            setNewFrag(ResetPasswordFragment())
        }
        signUpButton.setOnClickListener {
            Log.d(TAG, "Sign up button.")

            setNewFrag(SignUpFragment())
        }

        super.onStart()
    }

    private fun setNewFrag(frag: Fragment) {

        Util.mainFragment(activity, frag)
    }

    private fun loginUser() {
        val email = usernameField.text.toString()
        val password = passwordField.text.toString()
        Log.d(TAG, "Login Data: ${email}:${password}")

        // check if email and password is empty if it is toast notification
        if(Util.checkFieldsIfEmpty(email, password)) {
            Util.popUp(applicationContext, "Please enter both username and password!!", Toast.LENGTH_LONG)
            return
        }

        // signin functonality
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        Util.popUp(applicationContext, "Login successful!!", Toast.LENGTH_LONG)

                        // if sign-in is successful
                        // intent to home activity
                        val intent = Intent(
                            activity,
                            SITActivity::class.java
                        )
                        startActivity(intent)
                    } else {
                        Util.popUp(applicationContext, "Login Failed!!", Toast.LENGTH_LONG)
                    }
                })
    }

}