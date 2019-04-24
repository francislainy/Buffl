package com.francislainy.buffl.activities

import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.CoursesListFragment
import com.francislainy.buffl.model.Course
import com.francislainy.buffl.utils.ToolbarAndNavController
import com.francislainy.buffl.utils.addFragment
import com.francislainy.buffl.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.custom_add_dialog.*

class MainActivity : NavActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpDrawer()
        toolbarActionBarSetUP()

        addFragment(CoursesListFragment(), R.id.container_body_main)
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
        val myRef = database.reference.child(userId).child("courses")

        if (courseTitle.isEmpty()) {
            return false
        }

        val course = Course(courseTitle)

        myRef.push().setValue(course).addOnSuccessListener {

            toast("course saved")
        }
        return true
    }

    fun displayView(pos: Int) {
        ToolbarAndNavController(this@MainActivity).replaceFragment(pos)
    }

    fun displayToolbar(pos: Int) {
        ToolbarAndNavController(this@MainActivity).toolbarSetUP(pos)
    }

}