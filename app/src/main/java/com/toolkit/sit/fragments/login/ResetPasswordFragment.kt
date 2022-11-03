package com.toolkit.sit.fragments.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.toolkit.sit.R
import com.toolkit.sit.util.Util


/**
 * A simple [Fragment] subclass.
 * Use the [RestPassword.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResetPasswordFragment : Fragment() {

    private lateinit var backButton: Button
    private lateinit var forgotButton: Button
    private lateinit var emailText: EditText
    private lateinit var applicationContext: Context


    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_reset_password, container, false)
        // Inflate the layout for this fragment
        backButton = view.findViewById(R.id.buttonresetBack)
        forgotButton = view.findViewById(R.id.submit_forgot_password)
        emailText = view.findViewById(R.id.emailForgotPasswordField)
        applicationContext = view.context.applicationContext
        return  view
    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance();

        // allows user to go back to login portal
        backButton.setOnClickListener {
            Util.mainFragment(activity, LoginFragment())
        }

        // when the user clicks the forgot button with data
        forgotButton.setOnClickListener {
            val email = emailText.text.toString()
            if(!Util.checkFieldsIfEmpty(email)) {
                sendEmailReset(email)
            } else {
                Util.popUp(applicationContext, "Please Enter email", Toast.LENGTH_LONG)
            }
        }

    }

    // sends an email to the user to reset password
    private fun sendEmailReset(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                if (task.isSuccessful) {
                    Util.popUp(applicationContext, "Done sent", Toast.LENGTH_LONG)
                } else {
                    Util.popUp(applicationContext, "Error Occurred", Toast.LENGTH_LONG)
                }
            }).addOnFailureListener(OnFailureListener {
                Util.popUp(applicationContext, "Error Failed", Toast.LENGTH_LONG)
            })
    }

}