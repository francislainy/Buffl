package com.francislainy.buffl.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.CoursePagerControllerFragment
import com.francislainy.buffl.model.Course
import com.francislainy.buffl.utils.ToolbarAndNavController
import com.francislainy.buffl.utils.addFragment
import com.francislainy.buffl.utils.objectFromJsonString
import kotlinx.android.synthetic.main.activity_main.*

class LearnExploreActivity : AppCompatActivity() {

    private var objectTitle: String? = null

    override fun onResume() {
        super.onResume()

        displayToolbar(2, objectTitle!!) //todo: have dynamic position - 21/04/19
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_explore)

        invalidateOptionsMenu()
        toolbarActionBarSetUP()

        val objectString = intent.getStringExtra("objectString")
        objectTitle = objectFromJsonString(objectString, Course::class.java).courseTitle

        addFragment(CoursePagerControllerFragment.newInstance(objectString!!), R.id.container_body_learn)
    }

    private fun displayToolbar(pos: Int, param: String) {
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
                displayNewCardActivity()
                return true
            }
            R.id.menu_settings -> {
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun displayNewCardActivity() {
        val intent = Intent(this, NewCardActivity::class.java)
        startActivity(intent)
    }

}
