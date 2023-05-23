package com.example.alfabet_01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ParkingExtendedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parkin_extended)

    }

    fun onClickBack(view: View) {
        finish()
    }
}