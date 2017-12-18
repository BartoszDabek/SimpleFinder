package bdabek.com.simplefinder

import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

/**
 * Created by Bartek on 2017-12-18.
 */
class LocationService constructor(private val activity: Activity) {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null

    var longitude: Double? = null
    var latitude: Double? = null

    init {
        mLocationCallback = MyLocationCallback()
    }

    @SuppressLint("MissingPermission")
    fun createLocationRequest() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
        val client = LocationServices.getSettingsClient(activity)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener(activity) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        }

        task.addOnFailureListener(activity) { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(activity, REQUEST_ID_MULTIPLE_PERMISSIONS)
                } catch (sendEx: IntentSender.SendIntentException) {
                }

            }
        }
    }

    fun stopLocationUpdates() {
        mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
    }

    inner class MyLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                longitude = location.longitude
                latitude = location.latitude
                Log.d("SERVICE LONGITUDE: ", "$longitude")
                Log.d("SERVICE LATITUDE: ", "$latitude")
            }
        }
    }

}