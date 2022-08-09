package com.david.mapa.ui.activity.login


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.david.mapa.R
import com.google.firebase.auth.FirebaseAuth


class SignUpActivity : AppCompatActivity() {
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val signInBtn = findViewById<Button>(R.id.sign_in_btn)

        user = FirebaseAuth.getInstance()

        signInBtn.setOnClickListener {
            registerUser()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, LoginOrRegisterActivity::class.java))
    }

    private fun registerUser() {
        val email = findViewById<EditText>(R.id.username).text.toString()
        val password = findViewById<EditText>(R.id.password1).text.toString()
        val password2 = findViewById<EditText>(R.id.password2).text.toString()
        
        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (password == password2) {
                user.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity()){ task ->
                        if (task.isSuccessful){
//                            Toast.makeText(this, getString(R.string.user_added), Toast.LENGTH_SHORT).show()

                            user.addAuthStateListener {
                                if (user.currentUser != null) {
//                                    sendVerificationEmail()
                                    finish()
                                    startActivity(Intent(this, SignInActivity::class.java))
                                }
                            }
                        } else {
                            Toast.makeText(this, "aqui", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, getString(R.string.password_match), Toast.LENGTH_SHORT).show()
            }
            
        } else {
            Toast.makeText(this, getString(R.string.email_password_empty), Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVerificationEmail() {
        user.currentUser!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // email sent
                    // after email is sent just logout the user and finish this activity
                    Toast.makeText(this, getString(R.string.verification_email_sent), Toast.LENGTH_SHORT).show()
                    user.signOut()
                    finish()
                    startActivity(Intent(this, SignInActivity::class.java))
                } else {
                    // email not sent, so display message and restart the activity or do whatever you wish to do
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()

                    //restart this activity
//                    finish()
//                    startActivity(intent)
                }
            }
    }
}