package com.francislainy.buffl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.francislainy.buffl.R
import com.francislainy.buffl.activities.LoginActivity
import com.francislainy.buffl.utils.replaceFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import timber.log.Timber


class LoginFragment : GoogleSignInParentFragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        tvGoToSignUp.setOnClickListener {

            (activity as LoginActivity).replaceFragment(RegisterFragment(), R.id.container_body)
        }

        btnLogin.setOnClickListener {

            signInFirebase(etEmail.text.toString(), etPassword.text.toString())
        }

        btnSignInGoogle.setOnClickListener {

            signIn()
        }
    }

    private fun signInFirebase(email: String?, password: String?) {

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            return
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity as LoginActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithEmail:success")

                    Toast.makeText(activity as LoginActivity, "Logged in!!", Toast.LENGTH_SHORT).show()

                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w("signInWithEmail:failure ${task.exception}")
                    Toast.makeText(activity as LoginActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // ...
            }
    }
}