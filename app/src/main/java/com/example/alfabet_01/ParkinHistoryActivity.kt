package com.example.alfabet_01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfabet_01.databinding.ActivityMainBinding
import com.example.alfabet_01.databinding.ActivityParkinHistoryBinding
import com.yandex.mapkit.geometry.Point

class ParkinHistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityParkinHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParkinHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var modelList  : ArrayList<Model>? = null
        val secondIntent = Intent(this, ParkinExtendedActivity::class.java)
        val historyAdapter = DataHistoryAdapter()
        modelList = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("qwerty", ArrayList<Model>()::class.java)
        } else {
            intent.getSerializableExtra("qwerty") as ArrayList<Model>?
        }
        if (modelList.isNullOrEmpty()) {
            binding.recyclerHistory.visibility = View.INVISIBLE
            binding.textView8.visibility = View.VISIBLE
        } else {
<<<<<<< Updated upstream:app/src/main/java/com/example/alfabet_01/ParkinHistoryActivity.kt
            historyAdapter.addModel(modelList!!)
            init()
=======
            historyAdapter.addModel(modelList)
            binding.recyclerHistory.layoutManager = LinearLayoutManager(this)
            binding.recyclerHistory.adapter = historyAdapter
>>>>>>> Stashed changes:app/src/main/java/com/example/alfabet_01/ParkingHistoryActivity.kt
        }
    }

    /*private fun init() {
        binding.recyclerHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerHistory.adapter = historyAdapter
    }*/

    fun onClickBack(view: View) {
        finish()
    }
}