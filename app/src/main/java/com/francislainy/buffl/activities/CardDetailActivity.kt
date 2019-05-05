package com.francislainy.buffl.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.CardDetailFragment
import com.francislainy.buffl.fragments.CoursesListFragment
import com.francislainy.buffl.utils.addFragment

class CardDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)

        addFragment(CardDetailFragment(), R.id.container_body)
    }

}
