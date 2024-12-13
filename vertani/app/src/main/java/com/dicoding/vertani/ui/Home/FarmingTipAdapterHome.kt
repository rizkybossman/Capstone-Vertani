package com.dicoding.vertani.ui.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.vertani.R
import com.dicoding.vertani.model.FarmingTip

class FarmingTipAdapterHome(
    private val onItemClick: (FarmingTip) -> Unit
) : RecyclerView.Adapter<FarmingTipAdapterHome.FarmingTipViewHolder>() {

    private val farmingTipsList = mutableListOf<FarmingTip>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmingTipViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tips, parent, false)
        return FarmingTipViewHolder(view)
    }

    override fun onBindViewHolder(holder: FarmingTipViewHolder, position: Int) {
        val farmingTip = farmingTipsList[position]
        holder.bind(farmingTip)
        holder.itemView.setOnClickListener {
            onItemClick(farmingTip)
        }
    }

    override fun getItemCount(): Int = farmingTipsList.size

    fun submitList(list: List<FarmingTip>) {
        farmingTipsList.clear()
        farmingTipsList.addAll(list)
        notifyDataSetChanged()
    }

    class FarmingTipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tvTitle)


        fun bind(farmingTip: FarmingTip) {
            titleTextView.text = farmingTip.title

        }
    }
}
