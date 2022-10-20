package com.toolkit.sit.fragments.login

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
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
import com.toolkit.sit.util.ChangeActivity


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

        backButton.setOnClickListener {
            ChangeActivity.mainFragment(activity, LoginFragment())
        }

        forgotButton.setOnClickListener {
            val email = emailText.text.toString()
            if (!TextUtils.isEmpty(email)) {
                sendEmailReset(email)
            }
        }

    }

    private fun sendEmailReset(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                if (task.isSuccessful) {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(applicationContext, "Done sent", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_LONG)
                        .show()
                }
            }).addOnFailureListener(OnFailureListener {
                Toast.makeText(applicationContext, "Error Failed", Toast.LENGTH_LONG).show()
            })
    }

}