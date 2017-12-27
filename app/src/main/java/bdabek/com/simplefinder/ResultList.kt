package bdabek.com.simplefinder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import bdabek.com.simplefinder.adapters.GasStationAdapter
import bdabek.com.simplefinder.models.GasStation
import kotlinx.android.synthetic.main.activity_result_list.*


class ResultList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_list)

        val stationList = intent.getParcelableArrayListExtra<GasStation>("gas_station_list")

        Log.d("2 PARCELABLE LISTA", stationList.toString())

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = GasStationAdapter(this, stationList)
    }
}
