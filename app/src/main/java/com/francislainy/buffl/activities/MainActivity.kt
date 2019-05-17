package com.francislainy.buffl.activities

import android.app.Dialog
import android.content.Intent
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
import com.francislainy.buffl.fragments.SetListFragment
import com.francislainy.buffl.fragments.drawer.FragmentDrawer
import com.francislainy.buffl.model.Course
import com.francislainy.buffl.utils.DATA_COURSES
import com.francislainy.buffl.utils.ToolbarAndNavController
import com.francislainy.buffl.utils.addFragment
import com.francislainy.buffl.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_add_dialog.*

class MainActivity : AppCompatActivity(), FragmentDrawer.FragmentDrawerListener {

    private var exit: Boolean? = false
    private var drawerFragment: FragmentDrawer? = null
    private lateinit var newPostReference: DatabaseReference

    override fun onResume() {
        super.onResume()

        displayToolbar(1, "Library") //todo: have dynamic position -21/04/19
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        invalidateOptionsMenu()
        setUpDrawer()
        toolbarActionBarSetUP()

        addFragment(SetListFragment(), R.id.container_body_main)
    }

    override fun onDrawerItemSelected(view: View, position: Int) {
//        displayView(position)
    }

    override fun onBackPressed() {

        if (drawerFragment!!.isNavDrawerOpen()) {
            drawerFragment!!.closeNavDrawer()
        }

        val count =
            supportFragmentManager.backStackEntryCount // todo: add press again to exit without checking frag back stack

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

    private fun setUpDrawer() {
        drawerFragment = supportFragmentManager.findFragmentById(R.id.fragment_drawer) as FragmentDrawer
        drawerFragment!!.setUp(
            R.id.fragment_drawer,
            findViewById(R.id.drawer_layout),
            toolbar as Toolbar
        )
        drawerFragment!!.setDrawerListener(this)
    }

    private fun toolbarActionBarSetUP() {
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = null
            setHomeAsUpIndicator(R.drawable.ic_action_menu)
        }
    }

    private fun displayToolbar(pos: Int, param: String) {
        ToolbarAndNavController(this).toolbarSetUP(pos, param)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        menu.findItem(R.id.menu_settings).isVisible = false
        menu.findItem(R.id.menu_check).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawerFragment?.openDrawer()
            R.id.menu_plus -> {
                showDialog()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showDialog() {

        Dialog(this).apply {

            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.custom_add_dialog)
            show()

            btnSave.setOnClickListener {

                if (!saveToFirebase(etNewCourseTitle.text.toString())) {
                    return@setOnClickListener
                }

                dismiss()
            }
        }
    }

    private fun saveToFirebase(courseTitle: String): Boolean {

        val user = FirebaseAuth.getInstance().currentUser!!
        val userId = user.uid

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(userId).child(DATA_COURSES)

        if (courseTitle.isEmpty()) {
            return false
        }

//       myRef.push().setValue(course).addOnSuccessListener {

        newPostReference = myRef.push()

        val course = Course(newPostReference.key!!, courseTitle)

        newPostReference.setValue(course).addOnSuccessListener {

            toast("course saved")

            val intent = Intent(this@MainActivity, NewSetActivity::class.java)
            intent.putExtra("objectString", "delete") //todo: proper object
            startActivity(intent)
        }
        return true
    }

}