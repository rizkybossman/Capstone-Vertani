package com.dicoding.vertani.ui.Tips

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.vertani.R
import com.dicoding.vertani.model.FarmingTip
import com.dicoding.vertani.ui.TipsDetailActivity
import com.google.firebase.firestore.FirebaseFirestore

class TipsFragment : Fragment(R.layout.fragment_tips) {

    private lateinit var db: FirebaseFirestore
    private lateinit var farmingTipAdapter: TipsAdapter
    private lateinit var recyclerView: RecyclerView
    private val farmingTipsList = mutableListOf<FarmingTip>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        db = FirebaseFirestore.getInstance()

        recyclerView = view.findViewById(R.id.recyclerViewTips)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        farmingTipAdapter = TipsAdapter { farmingTip ->
            val intent = Intent(requireContext(), TipsDetailActivity::class.java)
            intent.putExtra("FARMING_TIP", farmingTip)
            startActivity(intent)
        }

        recyclerView.adapter = farmingTipAdapter

        fetchFarmingTips()
    }

    private fun fetchFarmingTips() {
        db.collection("farming_tips")
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


                farmingTipAdapter.submitList(farmingTipsList.toList())
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Gagal mengambil data tips: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

