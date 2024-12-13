package com.dicoding.vertani.ui.Home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.vertani.R
import com.dicoding.vertani.model.FarmingTip
import com.dicoding.vertani.ui.TipsDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var headerTitle: TextView
    private lateinit var farmingTipAdapterHome: FarmingTipAdapterHome
    private lateinit var recyclerView: RecyclerView
    private val farmingTipsList = mutableListOf<FarmingTip>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        headerTitle = view.findViewById(R.id.headerTitle)
        recyclerView = view.findViewById(R.id.recyclerViewTips)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)


        farmingTipAdapterHome = FarmingTipAdapterHome { farmingTip ->
            val intent = Intent(requireContext(), TipsDetailActivity::class.java)
            intent.putExtra("FARMING_TIP", farmingTip)
            startActivity(intent)
        }

        recyclerView.adapter = farmingTipAdapterHome

        fetchUserName()
        fetchFarmingTips()
    }

    private fun fetchUserName() {
        val userId = auth.currentUser?.uid
        userId?.let {
            db.collection("users").document(it)
                .get()
                .addOnSuccessListener { document ->
                    val name = document.getString("name")
                    if (!name.isNullOrEmpty()) {
                        headerTitle.text = "Halo, $name!"
                    } else {
                        headerTitle.text = "Halo, Pengguna!"
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Gagal mengambil data pengguna: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun fetchFarmingTips() {
        db.collection("farming_tips")
            .limit(5)
            .get()
            .addOnSuccessListener { documents ->
                farmingTipsList.clear()
                for (document in documents) {
                    val title = document.getString("title") ?: ""
                    val description = document.getString("description") ?: ""

                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        farmingTipsList.add(FarmingTip(document.id, title, description))
                    }
                }

                farmingTipAdapterHome.submitList(farmingTipsList.toList())
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Gagal mengambil data tips: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
