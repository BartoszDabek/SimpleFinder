package bdabek.com.simplefinder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import bdabek.com.simplefinder.models.GasStation

class ResultList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_list)

        val i = intent
        val lista2 = i.getParcelableArrayListExtra<GasStation>("gas_station_list")

        Log.d("2 PARCELABLE LISTA", lista2.toString())
    }
}
