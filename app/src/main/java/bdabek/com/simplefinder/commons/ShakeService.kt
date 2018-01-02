package bdabek.com.simplefinder.commons

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager

class ShakeService constructor(private val activity: Activity) {

    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var mShakeDetector: ShakeDetector? = null

    fun startShakeListener() {
        val prefs = bgSharedPreferences(activity)

        mSensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mShakeDetector = ShakeDetector()
        mShakeDetector!!.setOnShakeListener(object : ShakeDetector.OnShakeListener {
            override fun onShake(count: Int) {
                changeBgTheme(prefs)
                activity.recreate()
            }
        })
    }

    fun resumeShakeListener() {
        mSensorManager?.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun pauseShakeListener() {
        mSensorManager?.unregisterListener(mShakeDetector)
    }
}