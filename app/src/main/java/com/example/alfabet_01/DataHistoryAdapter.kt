package com.example.alfabet_01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfabet_01.databinding.ActivityParkinHistoryBinding
import com.example.alfabet_01.databinding.ParkinHistoryItemBinding
import java.util.Calendar


class DataHistoryAdapter: RecyclerView.Adapter<DataHistoryAdapter.DataHistoryHolder>() {
    private val modelList = ArrayList<Model>()
    class DataHistoryHolder(item: View): RecyclerView.ViewHolder(item){
        private val binding = ParkinHistoryItemBinding.bind(item)
        fun bind(DataHistory: Model) {
            val date = Calendar.getInstance()
            date.time = DataHistory.date
            val time = DataHistory.text + date.get(Calendar.HOUR_OF_DAY).toString() + ":" + date.get(Calendar.MINUTE).toString()
            binding.textView9.text = time

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