package io.deanencoded.twedit.repo

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import io.deanencoded.twedit.TweditAPI
import org.json.JSONObject

// looks like we may need to template the volley request... TODO

class Tweets{

    fun all(c: Context, id: Int, accessToken: String, callback: VolleyCallback){
        // Form fields and values
        val params = HashMap<String,Any>()
        params["id"] = id
        params["access_token"] = accessToken
        val formValues = JSONObject(params)

        // Volley post request with parameters
        val request = JsonObjectRequest(
            Request.Method.POST,TweditAPI().tweets.all,formValues,
            Response.Listener { res ->
                callback.onSuccess(res)
            }, Response.ErrorListener{ error ->
                // Error in request
                callback.onError(error)
            })

        // Volley request policy, only one time request to avoid duplicate transaction
        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            // 0 means no retry
            0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
            1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        // Add the volley post request to the request queue
        Volley.newRequestQueue(c).add(request)
    }

}