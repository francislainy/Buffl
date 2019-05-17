package com.francislainy.buffl.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.NewCardFragment
import com.francislainy.buffl.fragments.NewSetFragment
import com.francislainy.buffl.utils.ToolbarAndNavController
import com.francislainy.buffl.utils.addFragment
import kotlinx.android.synthetic.main.activity_main.*

class NewSetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_set)

        invalidateOptionsMenu()
        toolbarActionBarSetUP()

        val objectString = intent.getStringExtra("objectString")
        addFragment(NewSetFragment.newInstance(objectString), R.id.container_body_new_set)
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
            R.id.menu_check -> {
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
