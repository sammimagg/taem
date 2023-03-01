package `is`.hi.hbv601g.taem.Networking

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONObject

/*
* ATH: Það þarf eitthvað að yfirfæra þetta í Async call.
 */

class Fetcher(context: Context) {
    // Returna sessionUser
    fun AuthenticationRequest(url: String, username: String,
                              password: String, context: Context) : Any {
        val queue = Volley.newRequestQueue(context)
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)
        println(json)

        var sessionUser = SessionUser("", "")

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, json,
            {response ->
                Log.d("Reval: ", response.toString())
                sessionUser = Gson().fromJson(response.toString(), SessionUser::class.java)
            },
            {error ->
                Log.d("Reval: ", "Failed to fetch data")
                Log.d("Error: ", error.toString())
            })
        Log.d("Req:", jsonObjectRequest.toString())
        queue.add(jsonObjectRequest)

        return sessionUser
    }
}