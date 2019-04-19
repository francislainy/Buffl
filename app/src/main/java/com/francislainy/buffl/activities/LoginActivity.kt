package com.francislainy.buffl.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.LoginFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction().add(R.id.container_body, LoginFragment()).commit()
    }
}