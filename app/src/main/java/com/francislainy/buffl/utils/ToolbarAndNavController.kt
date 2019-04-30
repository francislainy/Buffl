package com.francislainy.buffl.utils

import androidx.appcompat.app.AppCompatActivity
import com.francislainy.buffl.R
import kotlinx.android.synthetic.main.toolbar_widget.*

class ToolbarAndNavController(private val mainActivity: AppCompatActivity) {

    fun toolbarSetUP(pos: Int = 0, param: String = "") {

        with(mainActivity) {

            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

            tvToolBarTitle.text = param
        }
    }

    private fun toolbarWithHamburger() {
        mainActivity.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_action_menu)
    }

    private fun toolbarWithBackArrow() {
        mainActivity.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_action_back)
    }

}