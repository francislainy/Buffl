package com.francislainy.buffl.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.CardDetailFragment
import com.francislainy.buffl.utils.addFragment

class CardDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)

        val setString = intent.getStringExtra("setString")
        addFragment(CardDetailFragment.newInstance(setString), R.id.container_body)
    }

}
