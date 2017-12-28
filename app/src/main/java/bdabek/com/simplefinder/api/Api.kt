package bdabek.com.simplefinder.api

import bdabek.com.simplefinder.models.Distance
import bdabek.com.simplefinder.models.Rows
import bdabek.com.simplefinder.models.StationList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Bartek on 2017-12-19.
 */
interface Api {
    @GET("maps/api/place/nearbysearch/json")
    fun getStations(@Query("location") location: String,
                    @Query("radius") radius: Int,
                    @Query("type") type: String = "gas_station",
                    @Query("key") key: String = "API_KEY") : Call<StationList>

    @GET("maps/api/distancematrix/json")
    fun getDistance(@Query("origins") originLoc: String,
                    @Query("destinations") destinationLoc: String,
                    @Query("key") key: String = "API_KEY") : Call<Rows>

}