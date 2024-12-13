package com.dicoding.vertani.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.vertani.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing_activity)
        supportActionBar?.hide()
        FirebaseApp.initializeApp(this)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }


        val btnMasuk: Button = findViewById(R.id.loginButton)
        val btnRegister: Button = findViewById(R.id.registerButton)


        btnMasuk.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        btnRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
