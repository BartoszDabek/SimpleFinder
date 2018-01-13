package bdabek.com.simplefinder

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import bdabek.com.simplefinder.api.RestAPI
import bdabek.com.simplefinder.commons.*
import bdabek.com.simplefinder.models.GasStation
import bdabek.com.simplefinder.models.StationList
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    val location: LocationService = LocationService(this)
    var distance: Float = STARTING_DISTANCE

    private val shake: ShakeService = ShakeService(this)
    private lateinit var prefs: SharedPreferences
    private var bgTheme: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = bgSharedPreferences(this)
        bgTheme = prefs.getString(BG_THEME, null)

        setBgTheme(this)
        setContentView(R.layout.activity_main)

        kmTxt.text = distance.toString()

        if (checkPermissions(this)) {
            startLocationUpdates()
        }

        findBtn.setOnClickListener {
            if (checkPermissions(this)) {
                showProgressBar()
                disableInteractionWithUser()
                getResultsAndSwitchScreen()
            }
        }

        addDistanceSeekBarListener()
        shake.startShakeListener()
    }

    override fun onResume() {
        super.onResume()
        shake.resumeShakeListener()
        if (checkPermissions(this)) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        location.stopLocationUpdates()
        shake.pauseShakeListener()
    }

    override fun onStart() {
        super.onStart()

        val bgTheme2: String? = prefs.getString(BG_THEME, null)

        if (bgTheme2 != bgTheme) {
            setTheme(bgTheme2!!.toInt())
            Handler().postDelayed({
                recreate()
            }, 0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates()
                } else {
                    Toast.makeText(this, getString(R.string.main_permissions_denied), Toast.LENGTH_LONG)
                            .show()
                }
                return
            }
        }
    }

    private fun startLocationUpdates() {
        location.startLocationRequests()
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
        val call = api.getStations("${location.latitude},${location.longitude}", (distance * 1000).toInt())

        call.enqueue(object : Callback<StationList> {
            override fun onResponse(call: Call<StationList>, response: Response<StationList>) {
                NetworkCall().execute(response.body()?.results)
            }

            override fun onFailure(call: Call<StationList>, t: Throwable) {
                Toast.makeText(this@MainActivity, getString(R.string.main_error_internet_connection), Toast.LENGTH_LONG).show()
                hideProgressBar()
                enableInteractionWithUser()
            }
        })
    }

    inner class NetworkCall : AsyncTask<List<GasStation>, Void, Unit>() {
        private var gasStationList: ArrayList<GasStation> = ArrayList()
        override fun doInBackground(vararg params: List<GasStation>) {
            val api = RestAPI().getApiService()
            val stationList = params[0]
            stationList.forEach { item ->
                val stationDistance = api.getDistance("${location.latitude},${location.longitude}",
                        "${item.geometry.location.lat},${item.geometry.location.lng}")
                        .execute()
                val distance = stationDistance.body()?.rows?.get(0)?.elements?.get(0)?.distance
                item.distance = distance?.text
                item.distanceInMeters = distance!!.value
                gasStationList.add(item)
            }
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            val gasStations = gasStationList.filter { it.distanceInMeters < distance * 1000 }
            if (gasStations.isEmpty()) {
                Toast.makeText(this@MainActivity, getString(R.string.main_no_results_found), Toast.LENGTH_SHORT).show()
                hideProgressBar()
                enableInteractionWithUser()
            } else {
                hideProgressBar()
                enableInteractionWithUser()
                switchScreen(gasStations)
            }
        }
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun enableInteractionWithUser() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun switchScreen(gasStations: List<GasStation>) {
        val intent = Intent(baseContext, ResultList::class.java)
        intent.putParcelableArrayListExtra(STATION_LIST_NAME, ArrayList(gasStations))
        startActivity(intent)
    }

    private fun addDistanceSeekBarListener() {
        distanceBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Log.d("Distance", "${(progress / 2f)}")
                kmTxt.text = (progress / 2f).toString()
                distance = progress / 2f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

}