package bdabek.com.simplefinder.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bdabek.com.simplefinder.R
import bdabek.com.simplefinder.models.GasStation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.gas_station.view.*

/**
 * Created by Bartek on 2017-12-27.
 */
class GasStationAdapter(private val context : Context, private val stationList: List<GasStation>) : RecyclerView.Adapter<GasStationAdapter.GasStationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GasStationViewHolder {
        return GasStationViewHolder(LayoutInflater.from(context).inflate(R.layout.gas_station, null))
    }

    override fun onBindViewHolder(holder: GasStationViewHolder?, position: Int) {
        val station = stationList[position]

        holder?.stationName?.text = station.name
        holder?.stationVicinity?.text = station.vicinity
        Picasso.with(context)
                .load(station.icon)
                .into(holder?.stationIcon)

        holder?.constraintLayout?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" +
                            "${station.geometry.location.lat}," +
                            "${station.geometry.location.lng}"))
            intent.`package` = "com.google.android.apps.maps"
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return stationList.size
    }


    inner class GasStationViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val stationName = itemView?.tvstation
        val stationVicinity = itemView?.tvvicinity
        val stationIcon = itemView?.icon
        val constraintLayout = itemView?.constraintLayout
    }
}