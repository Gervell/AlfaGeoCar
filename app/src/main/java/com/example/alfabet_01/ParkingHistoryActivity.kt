package com.example.alfabet_01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfabet_01.databinding.ActivityParkinHistoryBinding

class ParkingHistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityParkinHistoryBinding
    private var historyAdapter = DataHistoryAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParkinHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var modelList  : ArrayList<Model>? = null

        modelList = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("qwerty", ArrayList<Model>()::class.java)
        } else {
            intent.getSerializableExtra("qwerty") as ArrayList<Model>?
        }
        if (modelList.isNullOrEmpty()) {
            binding.recyclerHistory.visibility = View.INVISIBLE
            binding.textView8.visibility = View.VISIBLE
        } else {
            historyAdapter.addModel(modelList)
            init()
        }
    }

    private fun init() {
        binding.recyclerHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerHistory.adapter = historyAdapter
    }

    fun onClickBack(view: View) {
        finish()
    }
}