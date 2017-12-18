package bdabek.com.simplefinder

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var location : LocationService? = null
    var distance: Float = STARTING_DISTANCE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        distanceTxt.text = "$distance km"

        if (checkPermissions()) {
            startLocationUpdates()
        }

        findBtn.setOnClickListener {
            if(checkPermissions()) {
                Log.d("LONGTITUDE: ", "${location?.longitude}")
                Log.d("LATITUDE: ", "${location?.latitude}")
                location?.stopLocationUpdates()
            }
        }

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

    private fun startLocationUpdates() {
        if(location === null) {
            location = LocationService(this)
        }
        location?.createLocationRequest()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                // grantResults[0] returns permission only for ACCESS_FINE_LOCATION!
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates()

                } else {
                    Toast.makeText(this, "App will not work without permissions allowed!", Toast.LENGTH_LONG)
                            .show()
                }
                return
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

}