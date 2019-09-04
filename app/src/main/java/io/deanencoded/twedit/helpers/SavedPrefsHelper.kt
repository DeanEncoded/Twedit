package io.deanencoded.twedit.helpers

import android.content.Context
import android.preference.PreferenceManager


fun Context.setSharedPref(key: String, value: Any, isBoolean: Boolean = false) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(this)
    val editor = preferences.edit()
    if (isBoolean) {
        editor.putBoolean(key, value as Boolean)
    } else {
        // its a string
        editor.putString(key, value as String)
    }
    editor.apply()
}

// also have this return ints please
fun Context.getSharedPref(key: String, isBoolean: Boolean = false, defaultValue: Boolean = false): Any? {
    val preferences = PreferenceManager.getDefaultSharedPreferences(this)
    if (isBoolean) return preferences.getBoolean(key, defaultValue)
    return preferences.getString(key, null)
}

fun Context.clearSharedPrefs(): Boolean {
    val preferences = PreferenceManager.getDefaultSharedPreferences(this)
    preferences.edit().clear().apply()
    return true
}