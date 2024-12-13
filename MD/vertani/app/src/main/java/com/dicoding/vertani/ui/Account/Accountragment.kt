package com.dicoding.vertani.ui.Account

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.dicoding.vertani.R
import com.dicoding.vertani.ui.ProfileActivity
import com.dicoding.vertani.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class Accountragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_accountragment, container, false)


        auth = FirebaseAuth.getInstance()


        val profileLayout = view.findViewById<LinearLayout>(R.id.menuProfile)
        profileLayout.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }


        val logoutLayout = view.findViewById<LinearLayout>(R.id.menuLogout)
        logoutLayout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        return view
    }


    private fun showLogoutConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin logout?")
            .setPositiveButton("Ya") { dialog, which ->
                performLogout()
            }
            .setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss() // Menutup dialog jika klik "Tidak"
            }
            .create()

        alertDialog.show()
    }


    private fun performLogout() {
        auth.signOut() // Logout dari Firebase
        Toast.makeText(context, "Anda telah logout", Toast.LENGTH_SHORT).show()


        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
