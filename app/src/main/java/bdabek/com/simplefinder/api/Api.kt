package bdabek.com.simplefinder.api

import bdabek.com.simplefinder.commons.API_KEY
import bdabek.com.simplefinder.commons.GAS_TYPE
import bdabek.com.simplefinder.models.Rows
import bdabek.com.simplefinder.models.StationList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {
    @GET("maps/api/place/nearbysearch/json")
    fun getStations(@Query("location") location: String,
                    @Query("radius") radius: Int,
                    @Query("type") type: String = GAS_TYPE,
                    @Query("key") key: String = API_KEY) : Call<StationList>

    @GET("maps/api/distancematrix/json")
    fun getDistance(@Query("origins") originLoc: String,
                    @Query("destinations") destinationLoc: String,
                    @Query("key") key: String = API_KEY) : Call<Rows>

}