package bdabek.com.simplefinder.commons

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import bdabek.com.simplefinder.R

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

fun bgSharedPreferences(activity: Activity): SharedPreferences {
    return activity.getSharedPreferences(BG_THEME, Context.MODE_PRIVATE)
}

fun setBgTheme(activity: Activity) {
    val prefs = bgSharedPreferences(activity)
    val bgTheme : String? = prefs.getString(BG_THEME, null)

    if(bgTheme != null) {
        activity.setTheme(bgTheme.toInt())
    }
}

fun changeBgTheme(prefs: SharedPreferences) {
    val bgTheme : String? = prefs.getString(BG_THEME, null)

    if(bgTheme != null) {
        if(bgTheme == R.style.AppTheme.toString()) {
            prefs.edit().putString(BG_THEME, R.style.DarkTheme.toString()).apply()
        } else {
            prefs.edit().putString(BG_THEME, R.style.AppTheme.toString()).apply()
        }
    } else {
        prefs.edit().putString(BG_THEME, R.style.DarkTheme.toString()).apply()
    }
}