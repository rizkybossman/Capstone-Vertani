package com.dicoding.vertani.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.vertani.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.etEmail.setEmailInput(true)
        auth = FirebaseAuth.getInstance()

        binding.btnsend.setOnClickListener {
            forgotPassword()
        }
    }

    private fun forgotPassword() {
        val email = binding.etEmail.text.toString()
        if (email.isEmpty()) {
            Toast.makeText(this, "Masukkan alamat email", Toast.LENGTH_SHORT).show()
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email reset sandi telah dikirim", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal mengirim email reset sandi", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Gagal mengirim email reset sandi: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}