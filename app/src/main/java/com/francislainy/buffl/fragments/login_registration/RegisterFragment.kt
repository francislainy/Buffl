package com.francislainy.buffl.fragments.login_registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.francislainy.buffl.R
import com.francislainy.buffl.activities.LoginActivity
import com.francislainy.buffl.fragments.login_registration.GoogleSignInParentFragment
import com.francislainy.buffl.fragments.login_registration.LoginFragment
import com.francislainy.buffl.utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : GoogleSignInParentFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRegister.setOnClickListener {

            createAccount(etEmail.text.toString(), etPassword.text.toString())
        }

        tvBackToLogin.setOnClickListener {

            (activity as LoginActivity).replaceFragment(LoginFragment(), R.id.container_body)
        }

        btnSignInGoogle.setOnClickListener {

            signInGoogle()
        }
    }

}