package com.toolkit.sit.util

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.toolkit.sit.MainActivity

object ChangeActivity {
    @JvmStatic
    // swap to main Fragment
    fun mainFragment(activity: FragmentActivity?, fragment: Fragment) {
        (activity as MainActivity?)?.setFragment(fragment)
    }
}