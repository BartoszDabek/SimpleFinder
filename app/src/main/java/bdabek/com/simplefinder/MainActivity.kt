package bdabek.com.simplefinder

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import bdabek.com.simplefinder.api.RestAPI
import bdabek.com.simplefinder.commons.LocationService
import bdabek.com.simplefinder.commons.REQUEST_ID_MULTIPLE_PERMISSIONS
import bdabek.com.simplefinder.commons.STARTING_DISTANCE
import bdabek.com.simplefinder.commons.checkPermissions
import bdabek.com.simplefinder.models.StationList
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    val location: LocationService = LocationService(this)
    var distance: Float = STARTING_DISTANCE
    lateinit var list: StationList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        distanceTxt.text = "$distance km"

        if (checkPermissions(this)) {
            startLocationUpdates()
        }

        findBtn.setOnClickListener {
            if (checkPermissions(this)) {
                showProgressBar()
                disableInteractionWithUser()
                Handler().postDelayed({
                    getResultsAndSwitchScreen()
                }, 1000)
            }
        }

        addDistanceSeekBarListener()
    }


    private fun startLocationUpdates() {
        location.startLocationRequests()
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

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun disableInteractionWithUser() {
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun getResultsAndSwitchScreen() {
        val api = RestAPI().getApiService()
        val call = api.getStations("52.74328,23.58122", (distance * 1000).toInt())

        call.enqueue(object : Callback<StationList> {
            override fun onResponse(call: Call<StationList>, response: Response<StationList>) {
                list = response.body()!!
                location.stopLocationUpdates()

                hideProgressBar()
                enableInteractionWithUser()
                switchScreen()
            }

            override fun onFailure(call: Call<StationList>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error occurred! Check your internet connection!", Toast.LENGTH_LONG).show()
                hideProgressBar()
                enableInteractionWithUser()
            }
        })
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun enableInteractionWithUser() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun switchScreen() {
        val intent = Intent(baseContext, ResultList::class.java)
        intent.putParcelableArrayListExtra("gas_station_list", ArrayList(list.results))

        startActivity(intent)
    }

    private fun addDistanceSeekBarListener() {
        distanceBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Log.d("Distance", "${progress.div(2f)}")
                distanceTxt.text = "${progress.div(2f)} km"
                distance = progress.div(2f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

}