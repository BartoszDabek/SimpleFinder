package bdabek.com.simplefinder.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Bartek on 2017-12-19.
 */
class RestAPI {

    private val ROOT_URL: String = "https://maps.googleapis.com/"

    private fun getInstance() : Retrofit {
        return Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }

    fun getApiService() : Api {
        return getInstance().create(Api::class.java)
    }
}