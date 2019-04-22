package com.francislainy.buffl.activities

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.CoursesListFragment
import com.francislainy.buffl.fragments.FragmentDrawer
import com.francislainy.buffl.utils.ToolbarAndNavController
import com.francislainy.buffl.utils.addFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_add_dialog.*

class MainActivity : AppCompatActivity(), FragmentDrawer.FragmentDrawerListener {

    private var drawerFragment: FragmentDrawer? = null
    private var exit: Boolean? = false

    private lateinit var myRef: DatabaseReference
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpDrawer()
        toolbarActionBarSetUP()

        addFragment(CoursesListFragment(), R.id.container_body_main)

        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("courses")
    }

    private fun toolbarActionBarSetUP() {
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = null
            setHomeAsUpIndicator(R.drawable.ic_action_menu) // set Hamburger default back to the Actionbar ;)
        }
    }

    override fun onBackPressed() {

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_plus -> showDialog()
        }
        return true
    }

    private fun showDialog() {
        Dialog(this).apply {

            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.custom_add_dialog)
            show()

            btnSave.setOnClickListener {

                if (etNewCourseTitle.text.toString().isEmpty()) {
                    return@setOnClickListener
                }

                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid
                myRef.child(userId!!).child("courses").push().setValue(etNewCourseTitle.text.toString())

//                myRef.push().setValue(etNewCourseTitle.text.toString())

                dismiss()
            }
        }
    }


//    fun displayView(pos: Int) {
//        ToolbarAndNavController(this@MainActivity).replaceFragment(pos)
//    }

    fun displayToolbar(pos: Int) {
        ToolbarAndNavController(this@MainActivity).toolbarSetUP(pos)
    }

}