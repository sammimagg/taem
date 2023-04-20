package `is`.hi.hbv601g.taem.Networking

import android.content.Context
import android.util.Log
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import `is`.hi.hbv601g.taem.Persistance.*
import kotlinx.coroutines.CompletableDeferred
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

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

                deferred.complete(Pair(sessionUser, responseCode))
            },
            {error ->
                //Log.d("Reval: ", "Failed to fetch data")
                //Log.d("Error: ", error.toString())
                if (error.networkResponse != null) {
                    responseCode = error.networkResponse.statusCode
                }
                deferred.complete(Pair(sessionUser, responseCode))
            })

        queue.add(jsonObjectRequest)

        return deferred.await()
    }
    data class EmployeeResponse(
        @SerializedName("employee") val employeeRTI: EmployeeRTI?,
        @SerializedName("clock_in_time") val clock_in_time: String?,
        @SerializedName("clocked_in") val clocked_in: Boolean
    )

    suspend fun getRealTimeInsights(url: String, context: Context): ArrayList<EmployeeRTI> {
        val queue = Volley.newRequestQueue(context)
        val employeeRTIResponseDeferred = CompletableDeferred<ArrayList<EmployeeRTI>>()

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                Log.d("DEBUG", "Response received: $response")
                val gson = Gson()
                val type = object : TypeToken<ArrayList<EmployeeResponse>>() {}.type
                val employeeResponses = gson.fromJson<ArrayList<EmployeeResponse>>(response.toString(), type)
                Log.d("DEBUG", "EmployeeResponses parsed: $employeeResponses")

                val employeeRTIS = ArrayList<EmployeeRTI>()
                for (employeeResponse in employeeResponses) {
                    val employee = employeeResponse.employeeRTI

                    if (employee != null) {
                        employee.clockInTime = employeeResponse.clock_in_time
                        employee.clockIn = employeeResponse.clocked_in
                        employeeRTIS.add(employee)
                    }
                }

                employeeRTIResponseDeferred.complete(employeeRTIS)
            },
            { error ->
                Log.e("DEBUG", "Error in request: ${error.message}")
                employeeRTIResponseDeferred.completeExceptionally(error)
            }
        )

        queue.add(jsonArrayRequest)
        val result = employeeRTIResponseDeferred.await()
        Log.d("DEBUG", "Result: $result")
        return result
    }


    suspend fun fetchEmployeeProfile(url: String, ssn: String, context: Context) : Employee {
        val queue = Volley.newRequestQueue(context)
        val employeeProfileResponseDeferred = CompletableDeferred<Employee>()
        val jsonReq = JSONObject()
        jsonReq.put("ssn", ssn)
        //print(jsonReq)
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
        //Log.d("ég er hérna", "")
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
                //Log.d("Reval: ", response.toString())
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

    suspend fun fetchTransactions(url: String, ssn: String, dateFrom: String, dateTo: String, context: Context) : ViewTransactionUserDAO {
        val inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val queue = Volley.newRequestQueue(context)
        val employeeProfileResponseDeferred = CompletableDeferred<ViewTransactionUserDAO>()
        val json = JSONObject()
        json.put("ssn", ssn)
        if(dateFrom != null) json.put("dateFrom",
            outputFormatter.format(
                LocalDate.parse(dateFrom, inputFormatter)))
        if(dateTo != null) json.put("dateTo", outputFormatter.format(
            LocalDate.parse(dateTo, inputFormatter)))

        val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, url, json,
            {response ->
                Log.d("Reval: ", response.toString())
                val reval = Gson().fromJson(response.toString(), ViewTransactionUserDAO::class.java)
                employeeProfileResponseDeferred.complete(reval)
            },
            {error ->
                employeeProfileResponseDeferred.completeExceptionally(error)
            })

        queue.add(jsonObjectRequest)
        return employeeProfileResponseDeferred.await()
    }
    suspend fun getEmployeeList(url: String, context: Context): ArrayList<Employee> {
        val queue = Volley.newRequestQueue(context)
        val employeeResponseDeferred = CompletableDeferred<ArrayList<Employee>>()

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                Log.d("DEBUG", "Response received: $response")
                val gson = Gson()
                val type = object : TypeToken<ArrayList<Employee>>() {}.type
                val employees = gson.fromJson<ArrayList<Employee>>(response.toString(), type)
                employeeResponseDeferred.complete(employees)
            },
            { error ->
                Log.e("DEBUG", "Error in request: ${error.message}")
                employeeResponseDeferred.completeExceptionally(error)
            }
        )

        queue.add(jsonArrayRequest)
        return employeeResponseDeferred.await()
    }
    suspend fun isAuthenticated(accessToken: String, context: Context): String {
        val queue = Volley.newRequestQueue(context)
        val url = "https://www.hiv.is/auth/verify"
        val responseDeferred = CompletableDeferred<String>()

        val stringRequest = object : StringRequest(Method.POST, url,
            { response ->
                Log.d("DEBUG", "Response received: $response")
                responseDeferred.complete(response)
            },
            { error ->
                Log.e("DEBUG", "Error in request: ${error.message}")
                responseDeferred.completeExceptionally(error)
            }
        ) {
            override fun getBody(): ByteArray {
                return accessToken.toByteArray()
            }
        }

        queue.add(stringRequest)
        return responseDeferred.await()
    }
    suspend fun fetchDrivingLog(sessionUser: SessionUser, ssn: String, context: Context): List<Driving>? {
        val url = "hiv.is/api/driving/$ssn"

        val queue = Volley.newRequestQueue(context)
        val drivingLogDeferred = CompletableDeferred<List<Driving>?>()

        val jsonObjectRequest = object : JsonObjectRequest(Method.PUT, url, null,
            { response ->
                Log.d("DEBUG", "Response received: $response")
                val gson = Gson()
                val type = object : TypeToken<List<Driving>>() {}.type
                val drivingLog = gson.fromJson<List<Driving>>(response.toString(), type)
                Log.d("DEBUG", "Driving log parsed: $drivingLog")

                drivingLogDeferred.complete(drivingLog)
            },
            { error ->
                Log.e("DEBUG", "Error in request: ${error.message}")
                drivingLogDeferred.completeExceptionally(error)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer ${sessionUser.accessToken}"
                return headers
            }
        }



        queue.add(jsonObjectRequest)
        val result = drivingLogDeferred.await()
        Log.d("DEBUG", "Result: $result")
        return result
    }

    suspend fun fetchPendingReviews(url: String, context: Context) : List<MappedRequestUserDAO> {
        val queue = Volley.newRequestQueue(context)
        val employeeProfileResponseDeferred = CompletableDeferred<List<MappedRequestUserDAO>>()

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            {response ->
                Log.d("Reval: ", response.toString())
                val type = object : TypeToken<List<MappedRequestUserDAO>>() {}.type
                val reval = Gson().fromJson<List<MappedRequestUserDAO>>(response.toString(), type)
                employeeProfileResponseDeferred.complete(reval)
            },
            {error ->
                employeeProfileResponseDeferred.completeExceptionally(error)
            })

        queue.add(jsonArrayRequest)
        return employeeProfileResponseDeferred.await()
    }

}
