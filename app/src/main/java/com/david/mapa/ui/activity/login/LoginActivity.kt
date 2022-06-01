package com.david.mapa.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.david.mapa.R
import com.david.mapa.ui.activity.MapsActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btn = findViewById<Button>(R.id.login_btn)
        btn.setOnClickListener {
            startActivity(Intent(this,MapsActivity::class.java))
        }
    }
}