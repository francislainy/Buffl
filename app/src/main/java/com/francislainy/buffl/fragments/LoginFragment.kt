package com.francislainy.buffl.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.francislainy.buffl.R
import com.francislainy.buffl.activities.LoginActivity
import com.francislainy.buffl.activities.MainActivity
import com.francislainy.buffl.utils.replaceFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_login.*
import timber.log.Timber


class LoginFragment : Fragment() {

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

            signIn(etEmail.text.toString(), etPassword.text.toString())
        }

        btnSignInGoogle.setOnClickListener {

            //todo: copy code from registration
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun signIn(email: String?, password: String?) {

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

    private fun updateUI(user: FirebaseUser?) {

        if (user == null) { // Not logged in

            Toast.makeText(activity as LoginActivity, "Not signed in", Toast.LENGTH_SHORT).show()

        } else {

            val intent = Intent(activity as LoginActivity, MainActivity::class.java)
            startActivity(intent)

            Toast.makeText(activity as LoginActivity, "Signed in", Toast.LENGTH_SHORT).show()
        }
    }
}
