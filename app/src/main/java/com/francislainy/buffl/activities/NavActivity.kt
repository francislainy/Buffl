package com.francislainy.buffl.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.drawer.FragmentDrawer
import com.francislainy.buffl.utils.ToolbarAndNavController
import kotlinx.android.synthetic.main.activity_main.*

open class NavActivity : AppCompatActivity(),  FragmentDrawer.FragmentDrawerListener {

    private var exit: Boolean? = false
    private var drawerFragment: FragmentDrawer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
    }

    override fun onDrawerItemSelected(view: View, position: Int) {
//        displayView(position)
    }

    override fun onBackPressed() {

        if(drawerFragment!!.isNavDrawerOpen()) {
            drawerFragment!!.closeNavDrawer()
        }

        val count = supportFragmentManager.backStackEntryCount

        when (count) {
            0 -> pressAgainToExit()
            else -> supportFragmentManager.popBackStack()
        }
    }

    private fun pressAgainToExit() {

        if (exit!!) {
            finish() // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show()

            exit = true
            Handler().postDelayed({ exit = false }, (3 * 1000).toLong())
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        drawerFragment?.openDrawer()
        return true
    }

    fun toolbarActionBarSetUP() {
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = null
            setHomeAsUpIndicator(R.drawable.ic_action_menu) // set Hamburger default back to the Actionbar ;)
        }
    }

    fun setUpDrawer() {
        drawerFragment = supportFragmentManager.findFragmentById(R.id.fragment_drawer) as FragmentDrawer
        drawerFragment!!.setUp(
            R.id.fragment_drawer,
            findViewById(R.id.drawer_layout),
            toolbar as Toolbar
        )
        drawerFragment!!.setDrawerListener(this)
    }

    fun displayView(pos: Int, param: String = "") {
        ToolbarAndNavController(this@NavActivity).replaceFragment(pos, param)
    }

    fun displayToolbar(pos: Int) {
        ToolbarAndNavController(this@NavActivity).toolbarSetUP(pos)
    }

}
