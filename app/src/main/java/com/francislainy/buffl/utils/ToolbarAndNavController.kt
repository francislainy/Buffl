package com.francislainy.buffl.utils

import com.francislainy.buffl.R
import com.francislainy.buffl.activities.MainActivity
import kotlinx.android.synthetic.main.toolbar_widget.*

class ToolbarAndNavController(private val mainActivity: MainActivity) {

    fun toolbarSetUP(pos: Int) {

        with(mainActivity) {

            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

            tvToolBarTitle.text = "Library"
            toolbarWithHamburger()
        }
    }

    private fun toolbarWithHamburger() {
        mainActivity.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_action_menu)
    }

    private fun toolbarWithBackArrow() {
        mainActivity.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_action_back)
    }

}