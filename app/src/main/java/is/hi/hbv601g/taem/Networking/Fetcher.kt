package `is`.hi.hbv601g.taem.Networking

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `is`.hi.hbv601g.taem.Persistance.Employee
import `is`.hi.hbv601g.taem.Persistance.EmployeeRTI
import kotlinx.coroutines.CompletableDeferred
import org.json.JSONObject

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
    data class EmployeeResponse(val employeeRTI: EmployeeRTI, val clock_in_time: String?, val clocked_in: Boolean)
    suspend fun getRealTimeInsights(url: String, context: Context): ArrayList<EmployeeRTI> {
        val queue = Volley.newRequestQueue(context)
        val employeeRTIResponseDeferred = CompletableDeferred<ArrayList<EmployeeRTI>>()

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                val gson = Gson()
                val type = object : TypeToken<ArrayList<EmployeeResponse>>() {}.type
                val employeeResponses = gson.fromJson<ArrayList<EmployeeResponse>>(response.toString(), type)

                // extract the employees from the employeeResponses list
                val employeeRTIS = ArrayList<EmployeeRTI>()
                for (employeeResponse in employeeResponses) {
                    val employee = employeeResponse.employeeRTI
                    employee.clockInTime = employeeResponse.clock_in_time;
                    employee.clockIn = employeeResponse.clocked_in
                    employeeRTIS.add(employee)
                }

                // resolve the deferred object with the employees list
                employeeRTIResponseDeferred.complete(employeeRTIS)
            },
            { error ->
                // reject the deferred object with the error
                employeeRTIResponseDeferred.completeExceptionally(error)
            }
        )

        queue.add(jsonArrayRequest)
        return employeeRTIResponseDeferred.await()
    }

    suspend fun fetchEmployeeProfile(url: String, ssn: String, context: Context) : Employee {
        val queue = Volley.newRequestQueue(context)
        val employeeProfileResponseDeferred = CompletableDeferred<Employee>()
        val jsonReq = JSONObject()
        jsonReq.put("ssn", ssn)
        print(jsonReq)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, url, jsonReq,
            {response ->
                Log.d("Reval: ", response.toString())
                val employee = Gson().fromJson(response.toString(), Employee::class.java)
                employeeProfileResponseDeferred.complete(employee)
            },
            {error ->
                employeeProfileResponseDeferred.completeExceptionally(error)
            })

        queue.add(jsonObjectRequest)
        return employeeProfileResponseDeferred.await()
    }

    fun postEmployeeProfile(url : String, emp : Employee, context: Context) : String {
        val queue = Volley.newRequestQueue(context)
        // @TODO breyta fallinu í bakenda, bara uppfæra gildi sem eru ekki nöll
        Log.d("ég er hérna", "")
        val jsonReq = JSONObject()
        jsonReq.put("username", emp.username)
        jsonReq.put("ssn", emp.ssn)
        jsonReq.put("firstName", emp.firstName)
        jsonReq.put("lastName", emp.lastName)
        jsonReq.put("email", emp.email)
        jsonReq.put("jobTitle", emp.jobTitle)
        jsonReq.put("phoneNumber", emp.phoneNumber)
        /**
         *    response.email = email_field.text.toString()
        response.username = username_field.text.toString()
        response.lastName = last_name_field.toString()
        response.phoneNumber = phone_number_field.toString()
        response.jobTitle = job_title_field.toString()
         */
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonReq,
            { response ->
                Log.d("Reval: ", response.toString())
        },
            { error ->
                Log.d("eððoð: ", error.toString())
        })
        queue.add(jsonObjectRequest)
        return "null";
    }

    suspend fun registerRequest(url: String, username: String, password: String, email: String, ssn: String, context: Context) : Boolean {
        val queue = Volley.newRequestQueue(context)
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)
        json.put("email", email)
        json.put("ssn", ssn)

        var success = false

        val deferred = CompletableDeferred<Boolean>()

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, json,
            { response ->
                Log.d("Reval: ", response.toString())
                success = true
                deferred.complete(success)
            },
            { error ->
                Log.d("Reval: ", "Failed to fetch data")
                Log.d("Error: ", error.toString())
                deferred.complete(success)
            })

        queue.add(jsonObjectRequest)

        return deferred.await()
    }

}