package com.toolkit.sit.util

import android.text.TextUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import android.content.Context
import com.toolkit.sit.MainActivity

object Util {
    @JvmStatic

    // helper function to swap to main Fragment
    fun mainFragment(activity: FragmentActivity?, fragment: Fragment) {
        (activity as MainActivity?)?.setFragment(fragment)
    }

    // Function for validating if fields are entered in.
    fun checkFieldsIfEmpty(vararg fieldStr: String): Boolean {
        for (field in fieldStr) {
            if(TextUtils.isEmpty(field)) {
                return true
            }
        }
        return false
    }

    // Function to easy set fragment using the fragmentManager
    fun setFragment(manager: FragmentManager, containerId: Int, fragment: Fragment) {
        // Create new fragment and transaction
        manager.beginTransaction().replace(containerId, fragment).commit()
    }


    // simple function to popup toast
    fun popUp(ctx: Context, text: String, toast: Int) {
        Toast.makeText(ctx, text, toast).show()
    }
    fun String.isCIDR(): Boolean =
        this.matches(Regex("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\/(\\d{1,2})"))


}