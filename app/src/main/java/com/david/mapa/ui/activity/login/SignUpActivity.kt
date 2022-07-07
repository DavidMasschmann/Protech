package com.david.mapa.ui.activity.login


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.david.mapa.R
import com.david.mapa.ui.activity.MapsActivity
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

    private fun registerUser() {
        val email = findViewById<EditText>(R.id.username).text.toString()
        val password = findViewById<EditText>(R.id.email).text.toString()
        val password2 = findViewById<EditText>(R.id.password2).text.toString()
        
        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (password == password2) {
                user.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity()){ task ->
                        if (task.isSuccessful){
                            Toast.makeText(this, getString(R.string.user_added), Toast.LENGTH_SHORT).show()

                            startActivity(Intent(this, MapsActivity::class.java))
                            finish()
                            LoginOrRegisterActivity().finish()
                        } else {
                            Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                        }
                    }

            } else {
                Toast.makeText(this, getString(R.string.password_match), Toast.LENGTH_SHORT).show()
            }
            
        } else {
            Toast.makeText(this, getString(R.string.email_password_empty), Toast.LENGTH_SHORT).show()
        }
    }
}