package io.deanencoded.twedit.repo

import com.android.volley.VolleyError

import org.json.JSONObject

interface VolleyCallback {
    fun onSuccess(result: JSONObject)
    fun onError(result: VolleyError)
}