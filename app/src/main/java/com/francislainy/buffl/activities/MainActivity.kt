package com.francislainy.buffl.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.CoursesListFragment
import com.francislainy.buffl.fragments.FragmentDrawer
import com.francislainy.buffl.utils.addFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FragmentDrawer.FragmentDrawerListener {

    private var drawerFragment: FragmentDrawer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpDrawer()

        addFragment(CoursesListFragment(), R.id.container_body_main)
    }

    override fun onDrawerItemSelected(view: View, position: Int) {
//        displayView(position)
    }

    override fun onSupportNavigateUp(): Boolean {

        drawerFragment?.openDrawer()
        return true
    }

    private fun setUpDrawer() {
        drawerFragment = supportFragmentManager.findFragmentById(R.id.fragment_drawer) as FragmentDrawer
        drawerFragment!!.setUp(
            R.id.fragment_drawer,
            findViewById(R.id.drawer_layout),
            toolbar as Toolbar
        )
        drawerFragment!!.setDrawerListener(this)
    }

}