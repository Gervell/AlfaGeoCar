package com.example.alfabet_01

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    private var modelList :  ArrayList<Model>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        modelList = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("qwerty", ArrayList<Model>()::class.java)
        } else {
            intent.getSerializableExtra("qwerty") as ArrayList<Model>?
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        Log.d("Tag", "Settings destroyed")
    }

    fun onClickBack(view: View) {
        finish()
    }

    fun onClickHistory(view: View) {
        val intent = Intent(this, ParkinHistoryActivity::class.java)
        if (!modelList.isNullOrEmpty()) intent.putExtra("qwerty", modelList)
        startActivity(intent)
    }


}