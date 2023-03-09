package `is`.hi.hbv601g.taem.Networking

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.coroutines.CompletableDeferred
import org.json.JSONObject
import java.util.concurrent.CountDownLatch

/*
* ATH: Það þarf eitthvað að yfirfæra þetta í Async call.
 */


class Fetcher(context: Context) {

    suspend fun AuthenticationRequest(url: String, username: String, password: String, context: Context) : Pair<SessionUser?, Int> {
        val queue = Volley.newRequestQueue(context)
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)

        var sessionUser: SessionUser? = null
        var responseCode: Int = -1 // Default value if no response is received

        val deferred = CompletableDeferred<Pair<SessionUser?, Int>>()

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, json,
            {response ->
                Log.d("Reval: ", response.toString())
                sessionUser = Gson().fromJson(response.toString(), SessionUser::class.java)
                println(sessionUser.toString())
                deferred.complete(Pair(sessionUser, responseCode))
            },
            {error ->
                Log.d("Reval: ", "Failed to fetch data")
                Log.d("Error: ", error.toString())
                if (error.networkResponse != null) {
                    responseCode = error.networkResponse.statusCode
                }
                deferred.complete(Pair(sessionUser, responseCode))
            })

        queue.add(jsonObjectRequest)

        return deferred.await()
    }
    suspend fun getRealTimeInsights() {
        // dóri ?
    }
}