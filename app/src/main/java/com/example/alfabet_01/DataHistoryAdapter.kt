package com.example.alfabet_01

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfabet_01.databinding.ParkinHistoryItemBinding

class DataHistoryAdapter: RecyclerView.Adapter<DataHistoryAdapter.DataHistoryHolder>() {
    val modelList = ArrayList<Model>()
    class DataHistoryHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = ParkinHistoryItemBinding.bind(item)
        fun bind(DataHistory: Model) {
<<<<<<< Updated upstream
            binding.textView9.text = DataHistory.text
=======
            val date = Calendar.getInstance()
            date.time = DataHistory.date
            val time = DataHistory.text + date.get(Calendar.HOUR_OF_DAY).toString() + ":" + date.get(Calendar.MINUTE).toString()
            binding.textView9.text = time

            binding.cardView1.setOnClickListener {
                val intent = Intent(it.context, ParkinExtendedActivity::class.java)
                intent.putExtra("ParkingData", DataHistory)
                it.context.startActivity(intent)
            }

>>>>>>> Stashed changes
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