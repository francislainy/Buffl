package com.francislainy.buffl.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.francislainy.buffl.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)

        btnLogin.setOnClickListener {

            createAccount(etEmail.text.toString(), etPassword.text.toString())
        }
    }

    private fun createAccount(email: String?, password: String?) {

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
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

            Toast.makeText(this, "Not signed in", Toast.LENGTH_SHORT).show()

        } else {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            Toast.makeText(this, "Signed in", Toast.LENGTH_SHORT).show()
        }
    }
}