package `is`.hi.hbv601g.taem.Networking

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import `is`.hi.hbv601g.taem.Employee
import kotlinx.coroutines.CompletableDeferred
import org.json.JSONObject
import java.util.concurrent.CountDownLatch

/*
* ATH: Það þarf eitthvað að yfirfæra þetta í Async call.
 */


class Fetcher() {

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
    data class EmployeeResponse(val employee: Employee, val clock_in_time: String?, val clocked_in: Boolean)
    suspend fun getRealTimeInsights(url: String, context: Context): ArrayList<Employee> {
        val queue = Volley.newRequestQueue(context)
        val employeeResponseDeferred = CompletableDeferred<ArrayList<Employee>>()

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                val gson = Gson()
                val type = object : TypeToken<ArrayList<EmployeeResponse>>() {}.type
                val employeeResponses = gson.fromJson<ArrayList<EmployeeResponse>>(response.toString(), type)

                // extract the employees from the employeeResponses list
                val employees = ArrayList<Employee>()
                for (employeeResponse in employeeResponses) {
                    val employee = employeeResponse.employee
                    employees.add(employee)
                }

                // resolve the deferred object with the employees list
                employeeResponseDeferred.complete(employees)
            },
            { error ->
                // reject the deferred object with the error
                employeeResponseDeferred.completeExceptionally(error)
            }
        )

        queue.add(jsonArrayRequest)
        return employeeResponseDeferred.await()
    }



}