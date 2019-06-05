package com.francislainy.buffl.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.NewCardFragment
import com.francislainy.buffl.utils.addFragment
import com.francislainy.buffl.utils.toolbarSetUP
import kotlinx.android.synthetic.main.activity_main.toolbar

class NewCardActivity : AppCompatActivity() {

    private var edit: String? = null
    private var cardString: String? = null

    override fun onResume() {
        super.onResume()

        if (edit == null) {
            toolbarSetUP(this, "Create Card")
        } else {
            toolbarSetUP(this, "Edit Card")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_card)

        invalidateOptionsMenu()
        toolbarActionBarSetUP()

        val setString = intent.getStringExtra("setString")

        if (intent?.getStringExtra("edit") != null) {
            edit = intent?.getStringExtra("edit")
        }
        if (intent?.getStringExtra("cardString") != null) {
            cardString = intent?.getStringExtra("cardString")
        }

        addFragment(NewCardFragment.newInstance(setString, edit, cardString), R.id.container_body_new_card)
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