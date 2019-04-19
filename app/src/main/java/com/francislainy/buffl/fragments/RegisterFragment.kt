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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_register.*
import timber.log.Timber

class RegisterFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener {

            createAccount(etEmail.text.toString(), etPassword.text.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun createAccount(email: String?, password: String?) {

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity as LoginActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("createUserWithEmail:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w("createUserWithEmail:failure ${task.exception}")
                    updateUI(null)
                }

                // ...
            }
            .addOnFailureListener {

                Timber.w("Failure creating user ${it.message}")
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
