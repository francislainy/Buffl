package com.francislainy.buffl.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.SetPagerControllerFragment
import com.francislainy.buffl.model.MySet
import com.francislainy.buffl.utils.*
import kotlinx.android.synthetic.main.activity_main.*

class LearnExploreActivity : AppCompatActivity() {

    private var objectTitle: String? = null
    private var setString: String? = null

    override fun onResume() {
        super.onResume()

        toolbarSetUP(this, objectTitle!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_explore)

        invalidateOptionsMenu()
        toolbarActionBarSetUP()

        setString = intent.getStringExtra("setString")
        objectTitle = objectFromJsonString(setString, MySet::class.java).setTitle

        addFragment(SetPagerControllerFragment.newInstance(setString!!), R.id.container_body_learn)
    }

    private fun toolbarActionBarSetUP() {
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(false)
            setDisplayHomeAsUpEnabled(true)
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
            R.id.menu_settings -> { //todo: navigate to settings - 17/05/19
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun displayNewCardActivity() {
        val intent = Intent(this, NewCardActivity::class.java)
        intent.putExtra("setString", setString)
        startActivity(intent)
    }

}
