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
import com.francislainy.buffl.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.custom_add_dialog.*
import timber.log.Timber

class MainActivity : AppCompatActivity(), FragmentDrawer.FragmentDrawerListener {

    private var exit: Boolean? = false
    private var drawerFragment: FragmentDrawer? = null
    private lateinit var newPostReference: DatabaseReference

    private var courseString: String? = null

    override fun onResume() {
        super.onResume()

        toolbarSetUP(this, "Library")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        invalidateOptionsMenu()
        setUpDrawer()
        toolbarActionBarSetUP()

        fetchCourses()
    }

    override fun onDrawerItemSelected(view: View, position: Int) {
//        displayView(position)
    }

    override fun onBackPressed() {

        if (drawerFragment!!.isNavDrawerOpen()) {
            drawerFragment!!.closeNavDrawer()
        }

        when (supportFragmentManager.backStackEntryCount) {
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

        val course = Course(newPostReference.key!!, courseTitle, true)

        newPostReference.setValue(course).addOnSuccessListener {

            toast("course saved")

//            fetchCoursesToMarkThemNotRecentAnymore() // When a new course is added all the new ones are set to no longer the most recent

            val intent = Intent(this@MainActivity, NewSetActivity::class.java)
            intent.putExtra("courseString", courseString)
            startActivity(intent)
        }
        return true
    }

    private fun fetchCourses() {
        val user = FirebaseAuth.getInstance().currentUser!!
        val userId = user.uid

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(userId).child(DATA_COURSES)
        myRef.orderByKey().limitToLast(1)

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.children.forEach {

                    val map = it.value
                    courseString = objectToStringJson(map!!)

                    val course = objectFromJsonString(courseString, Course::class.java) // todo: find why this is getting back to false - 18/05

//                    if (course.mostRecent) {
                        addFragment(SetListFragment.newInstance(courseString!!), R.id.container_body_main)
//                        return@forEach
//                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Timber.w("Failed to read value. + ${error.toException()}")
            }
        })
    }

    private fun fetchCoursesToMarkThemNotRecentAnymore() {
        val user = FirebaseAuth.getInstance().currentUser!!
        val userId = user.uid

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(userId).child(DATA_COURSES)
        myRef.orderByKey()

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.children.forEach {

                    val cardMap = it.value
                    courseString = Gson().toJson(cardMap)

                    val course = Gson().fromJson<Course>(courseString, Course::class.java)
                    course.mostRecent = false

                    newPostReference.setValue(course).addOnSuccessListener {

                        Timber.d("Course ${course.courseId} not recent anymore")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Timber.w("Failed to read value. + ${error.toException()}")
            }
        })
    }

}