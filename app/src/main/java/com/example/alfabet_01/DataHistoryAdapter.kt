package com.example.alfabet_01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfabet_01.databinding.ActivityParkinHistoryBinding
import com.example.alfabet_01.databinding.ParkinHistoryItemBinding

class DataHistoryAdapter: RecyclerView.Adapter<DataHistoryAdapter.DataHistoryHolder>() {
    val modelList = ArrayList<Model>()
    class DataHistoryHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = ParkinHistoryItemBinding.bind(item)
        fun bind(DataHistory: Model) {
            binding.textView9.text = DataHistory.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHistoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.parkin_history_item, parent, false)
        return DataHistoryHolder(view)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    override fun onBindViewHolder(holder: DataHistoryHolder, position: Int) {
        holder.bind(modelList[position])
    }

    fun addModel (modArray: ArrayList<Model>) {
        modelList.addAll(modArray)
        notifyDataSetChanged()
    }
}