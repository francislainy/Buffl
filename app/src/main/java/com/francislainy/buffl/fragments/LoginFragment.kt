package com.francislainy.buffl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.francislainy.buffl.R
import com.francislainy.buffl.activities.LoginActivity
import com.francislainy.buffl.utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : GoogleSignInParentFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvGoToSignUp.setOnClickListener {

            (activity as LoginActivity).replaceFragment(RegisterFragment(), R.id.container_body)
        }

        btnLogin.setOnClickListener { signInFirebase(etEmail.text.toString(), etPassword.text.toString()) }

        btnSignInGoogle.setOnClickListener { signInGoogle() }
    }
}