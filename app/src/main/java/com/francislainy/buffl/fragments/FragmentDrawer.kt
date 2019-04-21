package com.francislainy.buffl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.francislainy.buffl.R

class FragmentDrawer : Fragment() {

    private var drawerToggle: ActionBarDrawerToggle? = null
    private var drawerLayout: DrawerLayout? = null
    private var containerView: View? = null
    private var drawerListener: FragmentDrawerListener? = null

    fun openDrawer() {
        drawerLayout?.openDrawer(containerView!!)
    }

    fun setDrawerListener(listener: FragmentDrawerListener) {
        this.drawerListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_drawer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listeners
//        challenges!!.setOnClickListener { closeNavDrawer(it, MainActivity.MENU_CHALLENGES) }
    }

    private fun closeNavDrawer(v: View, viewID: Int) {
        drawerListener!!.onDrawerItemSelected(v, viewID)
        drawerLayout!!.closeDrawer(containerView!!)
    }

    fun setUp(fragmentId: Int, drawerLayout: DrawerLayout, toolbar: Toolbar) {
        containerView = activity!!.findViewById(fragmentId)
        this.drawerLayout = drawerLayout
        drawerToggle = object : ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                activity!!.invalidateOptionsMenu()

            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                activity!!.invalidateOptionsMenu()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                toolbar.alpha = 1 - slideOffset / 2
            }
        }

        this.drawerLayout!!.setDrawerListener(drawerToggle)
        this.drawerLayout!!.post { drawerToggle!!.syncState() }
    }

    interface FragmentDrawerListener {
        fun onDrawerItemSelected(view: View, position: Int)
    }

}