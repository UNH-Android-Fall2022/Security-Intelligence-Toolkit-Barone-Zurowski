package com.toolkit.sit.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toolkit.sit.R


/**
 * A simple [Fragment] subclass.
 * Use the [RestPassword.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResetPassword : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_reset_password, container, false)
        // Inflate the layout for this fragment
        return  view
    }

}