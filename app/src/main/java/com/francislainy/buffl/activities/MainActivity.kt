package com.francislainy.buffl.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.CoursesListFragment
import com.francislainy.buffl.utils.addFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addFragment(CoursesListFragment(), R.id.container_body_main)
    }
}