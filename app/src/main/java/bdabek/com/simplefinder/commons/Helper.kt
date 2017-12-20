package bdabek.com.simplefinder.commons

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 * Created by Bartek on 2017-12-20.
 */
fun checkPermissions(activity: Activity): Boolean {
    var result: Int
    val listPermissionsNeeded = ArrayList<String>()
    for (p in permissions) {
        result = ContextCompat.checkSelfPermission(activity, p)
        if (result != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(p)
        }
    }
    if (!listPermissionsNeeded.isEmpty()) {
        ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
        return false
    }
    return true
}
