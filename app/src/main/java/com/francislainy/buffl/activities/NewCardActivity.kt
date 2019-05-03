package com.francislainy.buffl.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.CoursePagerControllerFragment
import com.francislainy.buffl.fragments.NewCardFragment
import com.francislainy.buffl.utils.ToolbarAndNavController
import com.francislainy.buffl.utils.addFragment
import kotlinx.android.synthetic.main.activity_main.*

class NewCardActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()

        displayToolbar(param = "Create Card") //todo: have dynamic position - 21/04/19
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_card)

        invalidateOptionsMenu()
        toolbarActionBarSetUP()

        addFragment(NewCardFragment(), R.id.container_body_new_card)
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
        menu.findItem(R.id.menu_plus).isVisible = false
        menu.findItem(R.id.menu_settings).isVisible = false
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