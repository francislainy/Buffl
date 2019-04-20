package com.francislainy.buffl.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.LoginFragment
import com.francislainy.buffl.utils.addFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        addFragment(LoginFragment(), R.id.container_body)
    }
}