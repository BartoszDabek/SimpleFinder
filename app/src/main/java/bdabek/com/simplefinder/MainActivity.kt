package bdabek.com.simplefinder

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var hasAuthorization: Boolean = false
    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null

    var distance: Float = 3f
    var longtitude: Double? = null
    var latitude: Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        distanceTxt.text = "$distance km"

        checkPermissions()

        findBtn.setOnClickListener {
            if (hasAuthorization && (latitude !== null && longtitude !== null)) {
                Log.d("Distance In Meters", "${(distance * 1000).toInt()}")
                Log.d("Current Longtitude", "$longtitude")
                Log.d("Current Latitude", "$latitude")
                msgTxt.append("CURRENT LATTITUDE: $latitude \n")
            } else {
                askForPermissions()
            }
        }

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

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askForPermissions()
        } else {
            getLocation()
        }
    }

    private fun askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION),
                    10)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        hasAuthorization = true

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = MyLocationListener()
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, locationListener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            10 -> if (grantResults[0] == 0) getLocation()
        }
    }


    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("Current Longtitude", "${location.longitude}")
            Log.d("Current Latitude", "${location.latitude}")
            longtitude = location.longitude
            latitude = location.latitude
        }

        override fun onProviderDisabled(provider: String) {
            Toast.makeText(this@MainActivity, "GPS is OFF!", Toast.LENGTH_SHORT).show()
        }

        override fun onProviderEnabled(provider: String) {}

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

}
