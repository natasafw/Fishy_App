package com.example.FishyFinder.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.FishyFinder.R
import com.example.FishyFinder.data.lokal.HistoryItem

class HistoryAdapter(private val items: List<HistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgHistory: ImageView = view.findViewById(R.id.imgHistory)
        val tvNamaIkan: TextView = view.findViewById(R.id.tvNamaIkan)
        val tvHabitat: TextView = view.findViewById(R.id.tvHabitat)
        val txtDate: TextView = view.findViewById(R.id.txtDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.imgHistory.setImageResource(item.imageRes)  // Jika dari drawable
        holder.tvNamaIkan.text = item.namaIkan
        holder.tvHabitat.text = item.habitat
        holder.txtDate.text = item.date

    }

    override fun getItemCount() = items.size
}

