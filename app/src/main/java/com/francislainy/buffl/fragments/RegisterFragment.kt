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
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_register.*
import timber.log.Timber

class RegisterFragment : Fragment(), GoogleApiClient.OnConnectionFailedListener {

    private val RC_SIGN_IN = 9000

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

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

        tvBackToLogin.setOnClickListener {

            (activity as LoginActivity).replaceFragment(LoginFragment(), R.id.container_body)
        }

        btnSignInGoogle.setOnClickListener {

            signIn()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun signIn() {

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id)) // It comes from https://console.cloud.google.com/apis/credentials?project=buffl-e110f
            .build()

        val googleApiClient = GoogleApiClient.Builder(activity as LoginActivity)
            .enableAutoManage(activity as LoginActivity, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Timber.w("Google sign in failed: $e")
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Timber.d("firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity as LoginActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithCredential:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w("signInWithCredential:failure ${task.exception}")
//                    Snackbar.make(main_layout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // ...
            }
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