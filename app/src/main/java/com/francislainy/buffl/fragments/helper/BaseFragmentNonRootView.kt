package com.francislainy.buffl.fragments.helper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.MenuItem

open class BaseFragmentNonRootView : Fragment() {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {

                activity!!.onBackPressed() // call the Parent OnBackPress will handled there ;)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true) //required for the Actionbar
    }
}
