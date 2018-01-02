package bdabek.com.simplefinder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import bdabek.com.simplefinder.adapters.GasStationAdapter
import bdabek.com.simplefinder.commons.ShakeService
import bdabek.com.simplefinder.commons.setBgTheme
import bdabek.com.simplefinder.models.GasStation
import kotlinx.android.synthetic.main.activity_result_list.*
import java.util.*


class ResultList : AppCompatActivity() {

    private val shake: ShakeService = ShakeService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBgTheme(this)
        setContentView(R.layout.activity_result_list)

        val stationList = intent.getParcelableArrayListExtra<GasStation>("gas_station_list")
        Collections.sort(stationList) { lhs, rhs ->
            lhs.distanceInMeters.compareTo(rhs.distanceInMeters)
        }

        shake.startShakeListener()

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = GasStationAdapter(this, stationList)
    }

    public override fun onResume() {
        super.onResume()
        shake.resumeShakeListener()
    }

    public override fun onPause() {
        super.onPause()
        shake.pauseShakeListener()
    }
}
