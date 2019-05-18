package com.francislainy.buffl.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.widget.Toolbar
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.NewCardFragment
import com.francislainy.buffl.fragments.NewSetFragment
import com.francislainy.buffl.model.Course
import com.francislainy.buffl.model.MySet
import com.francislainy.buffl.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_add_dialog.*

class NewSetActivity : AppCompatActivity() {

    private var courseString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_set)

        invalidateOptionsMenu()
        toolbarActionBarSetUP()

        courseString = intent.getStringExtra("courseString")
        addFragment(NewSetFragment.newInstance(courseString!!), R.id.container_body_new_set)
    }

    override fun onResume() {
        super.onResume()

        displayToolbar(param = "New Set") //todo: have dynamic position - 21/04/19
    }

    private fun displayToolbar(pos: Int = 0, param: String) {
        ToolbarAndNavController(this).toolbarSetUP(pos, param)
    }

    private fun toolbarActionBarSetUP() {
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(false)
            setDisplayHomeAsUpEnabled(false)
            title = null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        menu.findItem(R.id.menu_check).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
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

            tvNewCourseHeader.text = "New Set"
            tvNewCourseDescInfo.text = "Just give your cardset a name \nand you are done."

            btnSave.setOnClickListener {

                if (!saveToFirebase(etNewCourseTitle.text.toString())) {
                    return@setOnClickListener
                }

                dismiss()
            }
        }
    }

    private fun saveToFirebase(setTitle: String): Boolean {

        val user = FirebaseAuth.getInstance().currentUser!!
        val userId = user.uid

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(userId).child(DATA_SETS)

        if (setTitle.isEmpty()) {
            return false
        }

        val course = objectFromJsonString(courseString, Course::class.java)

        val mySet = MySet(
            course.courseId,
            "setId",//todo: not sure if needed
            setTitle
        )

        myRef.push().setValue(mySet)
            .addOnSuccessListener {

                this.toast("set saved")
                this.finish()
            }
            .addOnFailureListener {
                this.toast("failure")
            }

        return true

    }
}
