package bdabek.com.simplefinder.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


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