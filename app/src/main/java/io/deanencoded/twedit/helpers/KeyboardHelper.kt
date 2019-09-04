package com.deanencoded.ease.helpers

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager

class KeyboardHelper {

    fun hideKeyboard(c: Context, v: View) {
        val imm = c.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (isKeyboardShown(v.rootView)) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }
    }

    fun isKeyboardShown(rootView: View): Boolean {
        /* 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft keyboard */
        val softKeyboardHeightDpThreshold = 128
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        val dm = rootView.resources.displayMetrics
        /* heightDiff = rootView height - status bar height (r.top) - visible frame height (r.bottom - r.top) */
        val heightDiff = rootView.bottom - r.bottom
        /* Threshold size: dp to pixels, multiply with display density */
        val isKeyboardShown = heightDiff > softKeyboardHeightDpThreshold * dm.density
        Log.d("KBD", "isKeyboardShown ? " + isKeyboardShown + ", heightDiff:" + heightDiff + ", density:" + dm.density
                + "root view height:" + rootView.height + ", rect:" + r)
        return isKeyboardShown
    }
}