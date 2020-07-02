package com.example.googlelogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


class MainActivity : AppCompatActivity() {
    private val GoogleSignin = 100
    private lateinit var botonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setup()
    }

    private fun setup() {

        botonLogin = findViewById(R.id.login)
        botonLogin.setOnClickListener {
            if (username.text.isNotEmpty() && password.text.isNotEmpty()) {

                //  FirebaseAuthentication()
                GoogleAuthentication()

            }
        }
    }

    private fun GoogleAuthentication() {


        val googleconf =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        val googleClient = GoogleSignIn.getClient(this, googleconf)
        googleClient.signOut()
        startActivityForResult(googleClient.signInIntent, GoogleSignin)
    }

    private fun FirebaseAuthentication() {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(username.text.toString(), password.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Usuario Autenticado", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        this,
                        "Usuario ya se ha Autenticado o no se pudo registrar",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GoogleSignin) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "Usuario Autenticado", Toast.LENGTH_LONG)
                                    .show()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Usuario ya se ha Autenticado o no se pudo registrar",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            } catch (e: ApiException) {
                Toast.makeText(
                    this,
                    "ha habido  un error " + e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}