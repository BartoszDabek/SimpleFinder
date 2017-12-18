package bdabek.com.simplefinder

import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null

    var distance: Float = STARTING_DISTANCE
    var longtitude: Double? = null
    var latitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        distanceTxt.text = "$distance km"

        if (checkPermissions()) {
            createLocationRequest()
        }
        // because user accepted permissions while download from app store
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            createLocationRequest()
        }

        findBtn.setOnClickListener {
            if(checkPermissions()) {
                Log.d("LONGTITUDE: ", "$longtitude")
                Log.d("LATITUDE: ", "$latitude")
            }
        }

        mLocationCallback = MyLocationCallback()
        addDistanceSeekBarListener()
    }

    private fun checkPermissions(): Boolean {
        var result: Int
        val listPermissionsNeeded = ArrayList<String>()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(this, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                // grantResults[0] returns permission only for ACCESS_FINE_LOCATION!
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createLocationRequest()
                } else {
                    Toast.makeText(this, "The app will not work without these permissions!", Toast.LENGTH_LONG)
                            .show()
                }
                return
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun createLocationRequest() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        var builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener(this) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        }

        task.addOnFailureListener(this) { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this@MainActivity, REQUEST_ID_MULTIPLE_PERMISSIONS)
                } catch (sendEx: IntentSender.SendIntentException) {
                }

            }
        }
    }

    private fun addDistanceSeekBarListener() {
        distanceBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("Distance", "${progress.div(2f)}")
                distanceTxt.text = "${progress.div(2f)} km"
                distance = progress.div(2f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    inner class MyLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                longtitude = location.longitude
                latitude = location.latitude
            }
        }

    }

}
