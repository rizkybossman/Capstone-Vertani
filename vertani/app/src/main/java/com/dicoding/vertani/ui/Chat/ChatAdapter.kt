package com.dicoding.vertani.ui.Chat


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.vertani.R
import com.dicoding.vertani.data.entity.UploadHistory

class ChatAdapter(var uploadHistory: List<UploadHistory>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uploadHistoryItem = uploadHistory[position]
        holder.bind(uploadHistoryItem)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailRiwayatActivity::class.java)
            intent.putExtra("uploadHistory", uploadHistoryItem)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return uploadHistory.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPrediction: TextView = itemView.findViewById(R.id.tvPrediction)
        private val tvSoilPercentage: TextView = itemView.findViewById(R.id.tvSoilPercentage)
        private val tvElevation: TextView = itemView.findViewById(R.id.tvElevation)


        fun bind(uploadHistoryItem: UploadHistory) {
            tvPrediction.text = uploadHistoryItem.prediction
            tvSoilPercentage.text = uploadHistoryItem.soilPercentage
            tvElevation.text = uploadHistoryItem.elevation
        }
    }
}