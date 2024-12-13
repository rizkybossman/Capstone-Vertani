package com.dicoding.vertani.ui.Profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.vertani.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userLiveData = MutableLiveData<User?>()

    fun getUserData(): LiveData<User?> {
        return userLiveData
    }

    fun loadUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val user = document.toObject(User::class.java)
                        userLiveData.value = user
                    } else {
                        userLiveData.value = null
                    }
                }
        }
    }

    fun updateUserData(name: String, email: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userRef = db.collection("users").document(userId)
            userRef.update("name", name, "email", email)
                .addOnSuccessListener {
                    loadUserData()
                }
        }
    }
}