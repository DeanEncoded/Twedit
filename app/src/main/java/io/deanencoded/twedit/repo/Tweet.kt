package io.deanencoded.twedit.repo

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import io.deanencoded.twedit.TweditAPI
import org.json.JSONObject

class Tweet{

    fun new(c: Context, id: Int, accessToken: String, tweetText: String, callback: VolleyCallback){
        // Form fields and values
        val params = HashMap<String,Any>()
        params["id"] = id
        params["access_token"] = accessToken
        params["tweet_text"] = tweetText
        val formValues = JSONObject(params)

        // Volley post request with parameters
        val request = JsonObjectRequest(
            Request.Method.POST,TweditAPI().tweet.new,formValues,
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

    fun edit(c: Context, id: Int, accessToken: String,tweetId: Int, tweetText: String, callback: VolleyCallback){
        // Form fields and values
        val params = HashMap<String,Any>()
        params["id"] = id
        params["access_token"] = accessToken
        params["tweet_id"] = tweetId
        params["tweet_text"] = tweetText
        val formValues = JSONObject(params)

        // Volley post request with parameters
        val request = JsonObjectRequest(
            Request.Method.PUT,TweditAPI().tweet.edit,formValues,
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