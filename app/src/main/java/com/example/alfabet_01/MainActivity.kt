package com.example.alfabet_01

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher

import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.textfield.TextInputEditText
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationStatus
import com.yandex.runtime.image.ImageProvider
import java.io.File

class MainActivity : AppCompatActivity() {

    lateinit var mapview: MapView
    private lateinit var mapKit: MapKit
    var lastCarPoint = Point(0.0, 0.0)
    var modelList = ArrayList<Model>()
    private var launcher : ActivityResultLauncher<Intent>? = null
    private var lastImgUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("b11ae79f-43cf-49cd-849a-d8f84e060c8a")
        MapKitFactory.initialize(this)
        val isFirstLaunch = intent.getBooleanExtra("key", false)
        setContentView(R.layout.activity_main)
        mapview = findViewById(R.id.mapview)
        mapview.map.move(CameraPosition(Point(56.838011, 60.597474), 11.0f, 0.0f, 0.0f))
        mapKit = MapKitFactory.getInstance()
        if (isFirstLaunch) checkPerm()
        val location = mapKit.createUserLocationLayer(mapview.mapWindow)
        location.isVisible = true


        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                findViewById<ImageView>(R.id.imageView7).setImageURI(lastImgUri)
            }
        }

        checkGpsOn()


    }

    private fun getLocation() {
        val locMan = mapKit.createLocationManager()
        val locLis: LocationListener = object : LocationListener{
            override fun onLocationUpdated(p0: Location) {
                Log.d("Tag", "onLocationUpdated")
                mapview.map.move(CameraPosition(p0.position, 20.0f, 0.0f, 0.0f), Animation(Animation.Type.SMOOTH, 3f), null)
            }

            override fun onLocationStatusUpdated(p0: LocationStatus) {
                Log.d("Tag", "onStatusUpdated")
            }
        }
        locMan.requestSingleUpdate(locLis)
    }

    private fun checkPerm() {
        var alertText = ""
        val permArray = arrayOf(arrayOf(android.Manifest.permission.CAMERA, "The camera is not available. There is no way to use the camera to create a photo.\n"), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, "Current geolocation is not available."))
        for(perm in permArray) {
            val permCheck = ContextCompat.checkSelfPermission(this, perm[0]) != PackageManager.PERMISSION_GRANTED
            if (permCheck) alertText += perm[1]
        }

        if (alertText.length > 1) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("The functionality is limited.")
            builder.setMessage(alertText)
            builder.setPositiveButton("OK"){ dialogInterface, i ->
                Log.d("Tag", "Positive")
            }
            builder.show()
        }
    }

    private fun checkGpsOn() {
        val locMan = getSystemService(LOCATION_SERVICE) as LocationManager
        if (locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) Log.d("Tag", "Location is enable")
        else Log.d("Tag", "Location is disable")

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

    fun onClickSettings(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        intent.putExtra("qwerty", modelList)
        startActivity(intent)
    }

    fun onClickHelp(view: View) {
        val intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }

    fun onClickGPS(view: View) {
        getLocation()
    }

    fun onClickParking(view: View) {
        findViewById<ImageView>(R.id.imageViewPin).visibility = View.VISIBLE
        findViewById<ImageButton>(R.id.imageButtonConfirm).visibility = View.VISIBLE
        findViewById<ImageButton>(R.id.imageButtonCancel).visibility = View.VISIBLE
        mapview.map.mapObjects.isVisible = false
    }

    fun onClickCompass(view: View) {
        mapview.map.move(CameraPosition(lastCarPoint, 20.0f, 0.0f, 0.0f), Animation(Animation.Type.SMOOTH, 3f), null)
    }

    fun onClickConfirm(view: View) {
        lastCarPoint = mapview.map.cameraPosition.target
        findViewById<ImageView>(R.id.imageViewPin).visibility = View.GONE
        findViewById<ImageButton>(R.id.imageButtonConfirm).visibility = View.GONE
        findViewById<ImageButton>(R.id.imageButtonCancel).visibility = View.GONE
        findViewById<ConstraintLayout>(R.id.constrDescrInput).visibility = View.VISIBLE
        val carBitMap = BitmapFactory.decodeResource(resources, R.drawable.img_car_point)
        mapview.map.mapObjects.addPlacemark(lastCarPoint, ImageProvider.fromBitmap(carBitMap))
        /*
        val ess = object : MapObjectTapListener {
            override fun onMapObjectTap(p0: MapObject, p1: Point): Boolean {
                Log.d("Tag", "Tap on car")
                return true
            }

        }
        mapview.map.mapObjects.addTapListener(ess)
         */


    }

    fun onClickCancel(view: View) {
        findViewById<ImageView>(R.id.imageViewPin).visibility = View.GONE
        findViewById<ImageButton>(R.id.imageButtonConfirm).visibility = View.GONE
        findViewById<ImageButton>(R.id.imageButtonCancel).visibility = View.GONE
    }

    fun onClickSaveDecr(view: View) {
        val descrText = findViewById<TextInputEditText>(R.id.descrTextInput).text.toString()
        modelList.add(Model(lastCarPoint.latitude, lastCarPoint.longitude, descrText, lastImgUri?.encodedPath))
        findViewById<ConstraintLayout>(R.id.constrDescrInput).visibility = View.GONE
    }

    fun onClickAddPhoto(view: View){
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var file = File(filesDir, "picture.jpg")

        val extraOutput = FileProvider.getUriForFile(this,
            BuildConfig.APPLICATION_ID + "." + getLocalClassName() + ".provider",
            file)
        lastImgUri = extraOutput
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, extraOutput)



        launcher!!.launch(takePicture)
    }

}