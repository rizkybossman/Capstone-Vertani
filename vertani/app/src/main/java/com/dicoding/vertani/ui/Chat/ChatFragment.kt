package com.dicoding.vertani.ui.Chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.vertani.R
import com.dicoding.vertani.data.entity.UploadHistory
import com.dicoding.vertani.data.room.UploadHistoryDao
import com.dicoding.vertani.data.room.UploadHistoryDatabase
import com.dicoding.vertani.ui.ChatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var recyclerViewChats: RecyclerView
    private var adapter: ChatAdapter? = null
    private lateinit var uploadHistoryDao: UploadHistoryDao
    private lateinit var uploadHistoryLiveData: LiveData<List<UploadHistory>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newChatButton: Button = view.findViewById(R.id.btnNewChat)


        newChatButton.setOnClickListener {
            val intent = Intent(requireContext(), ChatActivity::class.java)
            startActivity(intent)
        }


        recyclerViewChats = view.findViewById(R.id.recyclerViewChats)

        recyclerViewChats.layoutManager = LinearLayoutManager(requireContext())

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = UploadHistoryDatabase.getInstance(requireContext(), userId!!)

        uploadHistoryDao = database.uploadHistoryDao()

        uploadHistoryLiveData = liveData {
            val uploadHistory = uploadHistoryDao.getUploadHistoryByUserId(userId)
            emit(uploadHistory)
        }

        uploadHistoryLiveData.observe(viewLifecycleOwner, Observer { uploadHistory ->
            if (adapter == null) {
                adapter = ChatAdapter(uploadHistory)
                recyclerViewChats.adapter = adapter
            } else {
                adapter?.uploadHistory = uploadHistory
                adapter?.notifyDataSetChanged()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        lifecycleScope.launch {
            val uploadHistory = uploadHistoryDao.getUploadHistoryByUserId(userId!!)
            adapter?.uploadHistory = uploadHistory
            adapter?.notifyDataSetChanged()
        }
    }
}