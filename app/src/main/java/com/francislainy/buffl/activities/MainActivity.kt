package com.francislainy.buffl.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.francislainy.buffl.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvLogout.setOnClickListener {

            // FirebaseAuth.getInstance().signOut()

            AuthUI.getInstance().signOut(this)
                .addOnCompleteListener {
                    // user is now signed out
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

        }
    }

}