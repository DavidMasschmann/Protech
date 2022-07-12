package com.david.mapa.ui.activity.login

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.david.mapa.R
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var user: FirebaseAuth
    private lateinit var emailEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val resetPasswordBtn = findViewById<Button>(R.id.sign_in_btn)

        user = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.email)
        resetPasswordBtn.setOnClickListener {
            resetPassword(emailEditText.text.toString().trim())
        }
    }

    private fun resetPassword(email: String) {
        emailEditText = findViewById(R.id.email)

        if (email.isEmpty()) {
            emailEditText.error = "Email is required"
            emailEditText.requestFocus()
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Please provide a valid email"
            emailEditText.requestFocus()
        }

        val loading = findViewById<ProgressBar>(R.id.loading)
        loading.visibility = View.VISIBLE
        user.sendPasswordResetEmail(email)
            .addOnCompleteListener(ResetPasswordActivity()) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Check your email to reset your password", Toast.LENGTH_SHORT).show()
                loading.visibility = View.INVISIBLE
                finish()
            } else {
                Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                loading.visibility = View.INVISIBLE
            }
        }
    }
}