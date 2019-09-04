package io.deanencoded.twedit.helpers

import android.content.Context
import io.deanencoded.twedit.R

// themechecker for changing between light/dark mdoes

class ThemeChecker {
    fun darkModeChecker(c: Context) {
        if (c.getSharedPref("DARK_MODE_ON", true) as Boolean) {
            // change to dark mode
            c.setTheme(R.style.AppThemeDark)
        }
    }
    fun isDarkModeOn(c: Context): Boolean {
        return c.getSharedPref("DARK_MODE_ON", true) as Boolean
    }
}