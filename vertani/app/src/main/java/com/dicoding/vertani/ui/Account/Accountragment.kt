package com.dicoding.vertani.ui.Account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.dicoding.vertani.R
import com.dicoding.vertani.ui.ForgotPasswordActivity
import com.dicoding.vertani.ui.LoginActivity
import com.dicoding.vertani.ui.Profile.ProfileActivity
import com.dicoding.vertani.ui.Profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Accountragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var tvFullName: TextView
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_accountragment, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        tvFullName = view.findViewById(R.id.tvFullName)


        profileViewModel.getUserData().observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                tvFullName.text = it.name
            }
        })


        profileViewModel.loadUserData()

        val profileLayout = view.findViewById<LinearLayout>(R.id.menuProfile)
        profileLayout.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        val forgotpasswordLayout = view.findViewById<LinearLayout>(R.id.menuChangePassword)
        forgotpasswordLayout.setOnClickListener {
            val intent = Intent(requireContext(), ForgotPasswordActivity::class.java)
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
            .setPositiveButton("Ya") { dialog, which -> performLogout() }
            .setNegativeButton("Tidak") { dialog, which -> dialog.dismiss() }
            .create()

        alertDialog.show()
    }
    override fun onResume() {
        super.onResume()
        profileViewModel.loadUserData()
    }
    private fun performLogout() {
        auth.signOut()
        Toast.makeText(context, "Anda telah logout", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}

