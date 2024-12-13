package com.dicoding.vertani.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.vertani.R

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)


        val backButton = findViewById<ImageView>(R.id.ivBack)
        backButton.setOnClickListener {
            finish()
        }


        val saveButton = findViewById<Button>(R.id.btnSave)
        saveButton.setOnClickListener {

            saveProfileChanges()
        }
    }

    private fun saveProfileChanges() {

        Toast.makeText(this, "Perubahan berhasil disimpan", Toast.LENGTH_SHORT).show()
    }
}
