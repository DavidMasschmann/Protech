package com.david.mapa.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.david.mapa.R
import com.david.mapa.ui.activity.MapsActivity
import com.google.firebase.auth.FirebaseAuth


class SignInActivity : AppCompatActivity() {
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signInBtn = findViewById<Button>(R.id.sign_in_btn)
        val resetPasswordBtn = findViewById<TextView>(R.id.forgot_password)

        user = FirebaseAuth.getInstance()

        signInBtn.setOnClickListener {
            loginUser()
        }

        resetPasswordBtn.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, LoginOrRegisterActivity::class.java))
    }

    private fun loginUser() {
        val email = findViewById<EditText>(R.id.username).text.toString()
        val password = findViewById<EditText>(R.id.password1).text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            user.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task ->
                    if (task.isSuccessful){
//                        Toast.makeText(this, "User logged in", Toast.LENGTH_SHORT).show()
                        user.currentUser?.reload()
                        finish()
                        startActivity(Intent(this, MapsActivity::class.java))
//                        checkIfEmailVerified()
                    } else {
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, getString(R.string.email_password_empty), Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfEmailVerified() {
        if (user.currentUser?.isEmailVerified == true) {
            // user is verified
            Toast.makeText(this, "Email verified", Toast.LENGTH_SHORT).show()

            finish()
            startActivity(Intent(this, MapsActivity::class.java))
        } else {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(this, "Your e-mail is not verified!", Toast.LENGTH_SHORT).show()
            user.signOut()
        }
    }


}