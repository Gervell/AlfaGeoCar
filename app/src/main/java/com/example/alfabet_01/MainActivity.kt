package com.example.alfabet_01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView


class MainActivity : AppCompatActivity() {

    lateinit var mapview: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("b11ae79f-43cf-49cd-849a-d8f84e060c8a")
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)
        mapview = findViewById(R.id.mapview)
        mapview.map.move(CameraPosition(Point(56.838011, 60.597474), 11.0f, 0.0f, 0.0f))
        Animation(Animation.Type.SMOOTH, 300f)
        var mapKit:MapKit = MapKitFactory.getInstance()
        requestPerm()
        var location = mapKit.createUserLocationLayer(mapview.mapWindow)
        location.isVisible = true
    }

    private fun requestPerm() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 102)
        // Запрос на использование местоположения
    }
    override fun onStop () {
        super.onStop()
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapview.onStart()
        MapKitFactory.getInstance().onStart()
    }
}