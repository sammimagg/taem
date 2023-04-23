package `is`.hi.hbv601g.taem.Networking

import android.content.Context
import android.util.Log
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import `is`.hi.hbv601g.taem.Persistance.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.serialization.json.Json
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

/*
* ATH: Það þarf eitthvað að yfirfæra þetta í Async call.
 */

/**
* A class that handles fetching data from an API using the Volley library and suspending functions.
 */
class Fetcher() {

    /**
    * A suspend function that makes an authentication request to the API.
    * @param username The username for the authentication request.
    * @param password The password for the authentication request.
    * @param context The context of the application.
    * @return A Pair containing a SessionUser object and an integer representing the response code.
     */
    suspend fun AuthenticationRequest( username: String, password: String, context: Context) : Pair<SessionUser?, Int> {
        val queue = Volley.newRequestQueue(context)
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)

        var sessionUser: SessionUser? = null
        var responseCode: Int = -1 // Default value if no response is received

        val deferred = CompletableDeferred<Pair<SessionUser?, Int>>()

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, Constants.API_URL+"/auth/login", json,
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

    /**
    * A suspend function that retrieves the real-time insights for all employees.
    * @param context The context of the application.
    * @return An ArrayList of EmployeeRTI objects representing the real-time insights for all employees.
     */
    suspend fun getRealTimeInsights( context: Context): ArrayList<EmployeeRTI> {
        val queue = Volley.newRequestQueue(context)
        val employeeRTIResponseDeferred = CompletableDeferred<ArrayList<EmployeeRTI>>()

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, Constants.API_URL+"/api/employee/rti", null,
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


    /**
     * Fetches an employee's profile data from the API using their SSN as a parameter.
     *
     * @param ssn the SSN of the employee whose profile data is to be fetched
     * @param context the Android context of the calling Activity or Fragment
     * @return an Employee object containing the employee's profile data
     * @throws ExecutionException if an error occurs while executing the request
     * @throws InterruptedException if the request is interrupted while waiting for a response
     */
    suspend fun fetchEmployeeProfile( ssn: String, context: Context) : Employee {
        val queue = Volley.newRequestQueue(context)
        val employeeProfileResponseDeferred = CompletableDeferred<Employee>()
        val jsonReq = JSONObject()
        jsonReq.put("ssn", ssn)
        //print(jsonReq)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, Constants.API_URL+"/api/employee/", jsonReq,
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

    /**
     * Posts an Employee object to the API to update the employee's profile data.
     *
     * @param emp an Employee object containing the updated profile data to be posted
     * @param context the Android context of the calling Activity or Fragment
     * @return a String containing the response from the API (in this implementation, always "null")
     */
    fun postEmployeeProfile(emp : Employee, context: Context) : String {
        val queue = Volley.newRequestQueue(context)
        // @TODO breyta fallinu í bakenda, bara uppfæra gildi sem eru ekki nöll

        val jsonReq = JSONObject()
        jsonReq.put("username", emp.username)
        jsonReq.put("ssn", emp.ssn)
        jsonReq.put("firstName", emp.firstName)
        jsonReq.put("lastName", emp.lastName)
        jsonReq.put("email", emp.email)
        jsonReq.put("jobTitle", emp.jobTitle)
        jsonReq.put("phoneNumber", emp.phoneNumber)
        jsonReq.put("account_type",emp.accountType)
        /**
         *    response.email = email_field.text.toString()
        response.username = username_field.text.toString()
        response.lastName = last_name_field.toString()
        response.phoneNumber = phone_number_field.toString()
        response.jobTitle = job_title_field.toString()
         */
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, Constants.API_URL+"/api/employee/", jsonReq,
            { response ->
                Log.d("Reval: ", response.toString())
        },
            { error ->
                Log.d("eððoð: ", error.toString())
        })
        queue.add(jsonObjectRequest)
        return "null";
    }

    /**
     * Sends a registration request to the API with the provided user data.
     *
     * @param username the desired username for the new user account
     * @param password the desired password for the new user account
     * @param email the email address associated with the new user account
     * @param ssn the SSN associated with the new user account
     * @param context the Android context of the calling Activity or Fragment
     * @return true if the registration was successful, false otherwise
     * @throws ExecutionException if an error occurs while executing the request
     * @throws InterruptedException if the request is interrupted while waiting for a response
     */
    suspend fun registerRequest( username: String, password: String, email: String, ssn: String, context: Context) : Boolean {
        val queue = Volley.newRequestQueue(context)
        val json = JSONObject()
        Log.d("ssn",ssn)
        Log.d("username",username)
        Log.d("email",email)
        Log.d("password",password)
        json.put("ssn", ssn)
        json.put("username", username)

        json.put("email", email)
        json.put("password", password)
        var success = true

        val deferred = CompletableDeferred<Boolean>()

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, Constants.API_URL+"/api/user/register", json,
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

    /**
     * Fetches transactions for a given social security number and date range.
     *
     * @param ssn The social security number to fetch transactions for.
     * @param dateFrom The start date of the date range to fetch transactions for, in the format "d/M/yyyy". Optional, can be null.
     * @param dateTo The end date of the date range to fetch transactions for, in the format "d/M/yyyy". Optional, can be null.
     * @param context The context of the calling activity.
     * @return A ViewTransactionUserDAO object representing the fetched transactions.
     */
    suspend fun fetchTransactions( ssn: String, dateFrom: String, dateTo: String, context: Context) : ViewTransactionUserDAO {
        val inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val queue = Volley.newRequestQueue(context)
        val employeeProfileResponseDeferred = CompletableDeferred<ViewTransactionUserDAO>()
        val json = JSONObject()
        json.put("ssn", ssn)
        if(dateFrom != null) json.put("dateFrom",outputFormatter.format(LocalDate.parse(dateFrom, inputFormatter)))

        if(dateTo != null) json.put("dateTo", outputFormatter.format(LocalDate.parse(dateTo, inputFormatter)))

        Log.d("Data", json.toString())
        val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, Constants.API_URL+"/api/transaction/list", json,
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
    /**
     * Fetches a list of employees.
     *
     * @param context The context of the calling activity.
     * @return An ArrayList of Employee objects representing the fetched employees.
     */
    suspend fun getEmployeeList( context: Context): ArrayList<Employee> {
        val queue = Volley.newRequestQueue(context)
        val employeeResponseDeferred = CompletableDeferred<ArrayList<Employee>>()

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, Constants.API_URL+"/api/employee/list", null,
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

    /**
     * Authenticates an access token.
     *
     * @param accessToken The access token to authenticate.
     * @param context The context of the calling activity.
     * @return A String representing the authenticated access token.
     */
    suspend fun isAuthenticated(accessToken: String, context: Context): String {
        val queue = Volley.newRequestQueue(context)
        val responseDeferred = CompletableDeferred<String>()

        val stringRequest = object : StringRequest(Method.POST, Constants.API_URL+"/auth/verify",
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

    /**
     * Fetches the driving log for a given social security number.
     *
     * @param sessionUser The SessionUser object representing the current user session.
     * @param ssn The social security number to fetch the driving log for.
     * @param context The context of the calling activity.
     * @return A List of Driving objects representing the fetched driving log.
     */
    suspend fun fetchDrivingLog(sessionUser: SessionUser, ssn: String, context: Context): List<Driving>? {

        val queue = Volley.newRequestQueue(context)
        val drivingLogDeferred = CompletableDeferred<List<Driving>?>()

        val jsonObjectRequest = object : JsonObjectRequest(Method.PUT, Constants.API_URL+"/api/driving/"+ssn, null,
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

    /**
     * Fetches a list of pending reviews.
     *
     * @param context The context of the calling activity.
     * @return A List of MappedRequestUserDAO objects representing the fetched pending reviews.
     */
    suspend fun fetchPendingReviews( context: Context) : List<MappedRequestUserDAO> {
        val queue = Volley.newRequestQueue(context)
        val employeeProfileResponseDeferred = CompletableDeferred<List<MappedRequestUserDAO>>()

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, Constants.API_URL+"/api/review", null,
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

    /**
    * Sends a review request for the specified requestId to the API server using Volley library.
    * @param requestId The ID of the review request to be sent.
    * @param context The context of the calling activity or fragment.
    * @param approved The approval status of the review request ("yes" or "no").
    * @return Void.
     */
    fun handleReviewRequest(requestId : Long, context: Context, approved : String) {
        val queue = Volley.newRequestQueue(context)
        //var sendit = url + requestId

        val json = JSONObject()
        json.put("approved", approved)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, Constants.API_URL+"/api/review/"+requestId, json,
            { response ->
                Log.d("Reval: ", response.toString())
            },
            { error ->
                Log.d("eððoð: ", error.toString())
            })

        queue.add(jsonObjectRequest)

        return
    }

}
