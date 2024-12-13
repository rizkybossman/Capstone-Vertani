package com.dicoding.vertani.ui.Profile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.vertani.R

class ProfileActivity : AppCompatActivity() {

    private lateinit var ivProfilePicture: ImageView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var saveButton: Button
    private lateinit var ivBack: ImageView

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.profile_activity)
        supportActionBar?.hide()
        ivProfilePicture = findViewById(R.id.ivProfilePicture)
        etName = findViewById(R.id.etname)
        etEmail = findViewById(R.id.etEmail)
        saveButton = findViewById(R.id.btnSave)
        ivBack = findViewById(R.id.ivBack)

        profileViewModel.getUserData().observe(this, Observer { user ->
            user?.let {
                etName.setText(it.name)
                etEmail.setText(it.email)
            }
        })


        profileViewModel.loadUserData()
        ivBack.setOnClickListener {
            onBackPressed()
        }
        saveButton.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi")
        builder.setMessage("Apakah Anda yakin ingin menyimpan perubahan profil?")

        builder.setPositiveButton("Ya") { dialog, _ ->
            saveProfileChanges()
            dialog.dismiss()
            finish()
        }

        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun saveProfileChanges() {
        val name = etName.text.toString()
        val email = etEmail.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty()) {

            profileViewModel.updateUserData(name, email)

            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
        }
    }
}
