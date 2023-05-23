package com.example.alfabet_01

import android.content.Intent
<<<<<<< Updated upstream:app/src/main/java/com/example/alfabet_01/ParkinHistoryActivity.kt
=======
import android.net.Uri
>>>>>>> Stashed changes:app/src/main/java/com/example/alfabet_01/ParkingHistoryActivity.kt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alfabet_01.databinding.ActivityMainBinding
import com.example.alfabet_01.databinding.ActivityParkinHistoryBinding
<<<<<<< Updated upstream:app/src/main/java/com/example/alfabet_01/ParkinHistoryActivity.kt
import com.yandex.mapkit.geometry.Point
=======
import java.io.File
>>>>>>> Stashed changes:app/src/main/java/com/example/alfabet_01/ParkingHistoryActivity.kt

class ParkinHistoryActivity : AppCompatActivity() {

<<<<<<< Updated upstream:app/src/main/java/com/example/alfabet_01/ParkinHistoryActivity.kt
    lateinit var binding: ActivityParkinHistoryBinding

=======
    private lateinit var binding: ActivityParkinHistoryBinding
    private val historyAdapter = DataHistoryAdapter()
    var modelListGL  : ArrayList<Model>? = null
>>>>>>> Stashed changes:app/src/main/java/com/example/alfabet_01/ParkingHistoryActivity.kt
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
        modelListGL = modelList
        if (modelList.isNullOrEmpty()) {
            setEmptyInvis()
        } else {
<<<<<<< Updated upstream:app/src/main/java/com/example/alfabet_01/ParkinHistoryActivity.kt
<<<<<<< Updated upstream:app/src/main/java/com/example/alfabet_01/ParkinHistoryActivity.kt
            historyAdapter.addModel(modelList!!)
            init()
=======
            historyAdapter.addModel(modelList)
            binding.recyclerHistory.layoutManager = LinearLayoutManager(this)
            binding.recyclerHistory.adapter = historyAdapter
>>>>>>> Stashed changes:app/src/main/java/com/example/alfabet_01/ParkingHistoryActivity.kt
=======
            modelList.reverse()
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
<<<<<<< Updated upstream:app/src/main/java/com/example/alfabet_01/ParkinHistoryActivity.kt
=======
    private fun setEmptyInvis() {
        binding.recyclerHistory.visibility = View.INVISIBLE
        binding.textView8.visibility = View.VISIBLE
    }
    fun onClickDeleteAll(view: View) {

        if (!modelListGL.isNullOrEmpty()) {
            historyAdapter.deleteAll()
            for (i in modelListGL!!.indices) {
                if (modelListGL!![i].imgPath != null) {
                    fileDelete(modelListGL!![i].imgPath!!)
                }
            }
            setEmptyInvis()
            modelListGL = null
        }
    }

    private fun fileDelete(uriString: String) {
        Log.d("Tag", uriString)
        val path = Uri.parse(uriString).path
        Log.d("Tag", path!!)
        val fdelete = File(path)
        if (fdelete.exists()) {
                if (fdelete.delete()) Log.d("Tag", "File delete")
                else Log.d("Tag", "File not delete")
        }
        else Log.d("Tag", "File not exist")

    }
>>>>>>> Stashed changes:app/src/main/java/com/example/alfabet_01/ParkingHistoryActivity.kt

    fun onClickBack(view: View) {
        val i = Intent()
        i.putExtra("modelListFromHistory", modelListGL)
        setResult(RESULT_OK, i)
        finish()
    }

    fun onClickAss(view: View) {
        val path = binding.editTextText.text.toString()
        Log.d("Tag", path)
        val fdelete = File(path)
        if (fdelete.exists()) {
            if (fdelete.delete()) Log.d("Tag", "File delete")
            else Log.d("Tag", "File not delete")
        }
        else Log.d("Tag", "File not exist")
    }
}