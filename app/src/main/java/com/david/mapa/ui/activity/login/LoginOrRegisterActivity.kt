package com.david.mapa.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.david.mapa.R
import com.david.mapa.ui.activity.MapsActivity
import com.google.firebase.auth.FirebaseAuth

class LoginOrRegisterActivity : AppCompatActivity() {
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_or_register)

        user = FirebaseAuth.getInstance()

        checkIfUserIsLogged()

        val signInBtn = findViewById<Button>(R.id.sign_in_btn)
        val signUpBtn = findViewById<Button>(R.id.sign_up_btn)

        signInBtn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }


    }

    private fun checkIfUserIsLogged() {
        if (user.currentUser != null){
            finish()
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }


}