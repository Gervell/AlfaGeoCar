package com.example.alfabet_01

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
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
import com.yandex.mapkit.location.FilteringMode
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
import java.util.Calendar
import java.util.Date
import java.io.File

class MainActivity : AppCompatActivity() {

    lateinit var mapview: MapView
    private lateinit var mapKit: MapKit
    private var lastCarPoint = Point(0.0, 0.0)
    private var currentLocation = Point(0.0, 0.0)
    private var modelList = ArrayList<Model>()
    private var launcher : ActivityResultLauncher<Intent>? = null
    private var lastImgUri: Uri? = null
    private lateinit var tapLis: MapObjectTapListener
    private var rewriteDescription = false
    lateinit var car: PlacemarkMapObject
    var state = false

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
        else checkGpsOn()

        val location = mapKit.createUserLocationLayer(mapview.mapWindow)
        location.isVisible = true

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                findViewById<ImageView>(R.id.imageView7).setImageURI(lastImgUri)
                Log.d("Tag", "Image set")
            }
        }

        tapLis = object : MapObjectTapListener{
            override fun onMapObjectTap(p0: MapObject, p1: Point): Boolean {
                //Log.d("Tag", "TUPTUTPTUP!!!!!")
                findViewById<ConstraintLayout>(R.id.constrDescrInput).visibility = View.VISIBLE
                findViewById<Button>(R.id.button3).setText(R.string.apply_changes)
                rewriteDescription = true
                return true
            }

        }
        if (isDarkMode()) {
            Log.d("Tag", "Dark theme")
            mapview.map.isNightModeEnabled = true
        }
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


    private fun subscribeLocationUpdates () {
        var updateCounter = 0
        val time = System.currentTimeMillis() / 1000
        val locationListener = object : LocationListener {
            override fun onLocationUpdated(p0: Location) {
                currentLocation = p0.position
                updateCounter++
                Log.d("Tag", "Location updated!!! $updateCounter")
                Log.d("Tag", "Location time ${System.currentTimeMillis() / 1000 - time}")
            }

            override fun onLocationStatusUpdated(p0: LocationStatus) {

            }

        }
        mapKit.createLocationManager().subscribeForLocationUpdates(1.0, 10, 0.0, true, FilteringMode.OFF, locationListener)
    }

    private fun isDarkMode() : Boolean {

        return resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
    }



    private fun getLocation() {
        val locMan = mapKit.createLocationManager()
        val locLis: LocationListener = object : LocationListener{
            override fun onLocationUpdated(p0: Location) {
                Log.d("Tag", "onLocationUpdated")
                currentLocation = p0.position
                mapview.map.move(CameraPosition(p0.position, 20.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 3f), null)
            }

            override fun onLocationStatusUpdated(p0: LocationStatus) {
                Log.d("Tag", "onStatusUpdated")
            }
        }
        locMan.requestSingleUpdate(locLis)
    }

    private fun checkPerm() {
        var alertText = ""
        val permArray = arrayOf(arrayOf(android.Manifest.permission.CAMERA, getString(R.string.permission_camera_warning)),
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, getString(R.string.permission_location_warning)))
        for(perm in permArray) {
            val permCheck = ContextCompat.checkSelfPermission(this, perm[0]) != PackageManager.PERMISSION_GRANTED
            if (permCheck) alertText += perm[1]
        }

        if (alertText.length > 1) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.functional_is_limited_text))
            builder.setMessage(alertText)
            builder.setPositiveButton("OK"){ _, _ ->
                Log.d("Tag", "Positive")
                checkGpsOn()
            }
            builder.show()
        } else checkGpsOn()
    }

    private fun checkLocationOn(): Boolean {
        val ass = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val locMan = getSystemService(LOCATION_SERVICE) as LocationManager
        val dass = locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)
        Log.d("Tag", "$ass    $dass")
        return ass and dass
    }

    private fun checkGpsOn() {
        val locMan = getSystemService(LOCATION_SERVICE) as LocationManager
        if (locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("Tag", "Location is enable")
        }
        else {
            Log.d("Tag", "Location is disable")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Geolocation is disabled")
            builder.setMessage("Enable geolocation in your phone settings so that the app will show your location")
            builder.setPositiveButton("OK"){ _, _ ->
                Log.d("Tag", "Positive")
            }
            builder.show()
        }

    }






    fun onClickSettings(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        modelList.reverse()
        intent.putExtra("qwerty", modelList)
        modelList.reverse()
        startActivity(intent)
    }

    fun onClickHelp(view: View) {
        val intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }

    fun onClickGPS(view: View) {
        subscribeLocationUpdates()
        getLocation()
    }

    fun onClickParking(view: View) {
        setParkingImgVisibility(View.VISIBLE)
        if (checkLocationOn() and (currentLocation.latitude != 0.0)) mapview.map.move(CameraPosition(currentLocation, 20.0f, 0.0f, 0.0f), Animation(Animation.Type.SMOOTH, 3f), null)
    }

    fun onClickCompass(view: View) {
        if (lastCarPoint.latitude != 0.0) {
            mapview.map.move(CameraPosition(lastCarPoint, 20.0f, 0.0f, 0.0f), Animation(Animation.Type.SMOOTH, 3f), null)
        }
    }

    fun onClickConfirm(view: View) {
        lastCarPoint = mapview.map.cameraPosition.target

        setParkingImgVisibility(View.GONE)
        findViewById<ConstraintLayout>(R.id.constrDescrInput).visibility = View.VISIBLE
        findViewById<TextInputEditText>(R.id.descrTextInput).setText("")
        findViewById<Button>(R.id.button3).text = "save"
        findViewById<ImageView>(R.id.imageView7).setImageResource(R.drawable.img_default_photo)
        var carBitMap = BitmapFactory.decodeResource(resources, R.drawable.img_car_point)
        carBitMap = Bitmap.createScaledBitmap(carBitMap, 80, 80, false)
        if (state) {
            car.parent.remove(car)
        }
        val der = mapview.map.mapObjects.addPlacemark(lastCarPoint, ImageProvider.fromBitmap(carBitMap))
        der.addTapListener(tapLis)
        car = der
        state = true


    }

    fun onClickCancel(view: View) {
        setParkingImgVisibility(View.GONE)
    }

    private fun setParkingImgVisibility(visibility: Int) {
        findViewById<ImageView>(R.id.imageViewPin).visibility = visibility
        findViewById<ImageButton>(R.id.imageButtonConfirm).visibility = visibility
        findViewById<ImageButton>(R.id.imageButtonCancel).visibility = visibility
    }

    fun onClickSaveDescription(view: View) {
        val descriptionText = findViewById<TextInputEditText>(R.id.descrTextInput).text.toString()
        val lastImgPath = if (lastImgUri != null) lastImgUri!!.toString() else ""
        if (rewriteDescription) {
            val lastID = modelList.lastIndex
            modelList[lastID].text = descriptionText
            modelList[lastID].imgPath = lastImgPath
            findViewById<ConstraintLayout>(R.id.constrDescrInput).visibility = View.GONE
            rewriteDescription = false
        } else {
            modelList.add(Model(lastCarPoint.latitude, lastCarPoint.longitude, descriptionText, lastImgPath, Calendar.getInstance().time))
            findViewById<ConstraintLayout>(R.id.constrDescrInput).visibility = View.GONE
        }

    }

    fun onClickAddPhoto(view: View){
        val ass = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        if (ass) {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val milis = System.currentTimeMillis().toString()
            val file = File(filesDir, "picture_$milis.jpg")

            val extraOutput = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + "." + localClassName + ".provider",
                file
            )

            lastImgUri = extraOutput
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, extraOutput)

            launcher!!.launch(takePicture)
        }
    }

}