import android.content.Context
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `is`.hi.hbv601g.taem.DrivingLogAdapter
import `is`.hi.hbv601g.taem.Persistance.Driving
import `is`.hi.hbv601g.taem.Networking.SessionUser
import `is`.hi.hbv601g.taem.Networking.getSessionUser
import `is`.hi.hbv601g.taem.R
import `is`.hi.hbv601g.taem.Storage.db
import kotlinx.coroutines.*
import org.json.JSONObject

class DrivingLogFragment : Fragment() {

    //private lateinit var ssn: String
    private lateinit var sessionUser: SessionUser
    private lateinit var drivingLogAdapter: DrivingLogAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_driving_log, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.drivingLogRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        drivingLogAdapter = DrivingLogAdapter(emptyList())
        recyclerView.adapter = drivingLogAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionUser = getSessionUser(requireContext()) ?: throw IllegalStateException("SessionUser not found in SharedPreferences")
        println(sessionUser.ssn)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val drivingSessions = fetchDrivingLog(sessionUser, sessionUser.ssn, requireContext())
                withContext(Dispatchers.Main) {
                    if (drivingSessions != null) {
                        drivingLogAdapter.setData(drivingSessions)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to fetch driving sessions",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Failed to fetch driving sessions",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    suspend fun fetchDrivingLog(sessionUser: SessionUser, ssn: String, context: Context): List<Driving>? {
        var url = "https://www.hiv.is/api/driving/"
        url += ssn;

        println(url);
        val queue = Volley.newRequestQueue(context)
        val drivingLogDeferred = CompletableDeferred<List<Driving>?>()

        val jsonArrayRequest = object : JsonArrayRequest(Method.PUT, url, null,
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



        queue.add(jsonArrayRequest)
        val result = drivingLogDeferred.await()
        Log.d("DEBUG", "Result: $result")
        return result
    }



    suspend fun appendDrivingLog(sessionUser: SessionUser, drivingSession: Driving, context: Context): Boolean {
        val url = "https://hiv.is/api/driving"

        val queue = Volley.newRequestQueue(context)
        val successDeferred = CompletableDeferred<Boolean>()

        val jsonObject = JSONObject()
        jsonObject.put("ssn", drivingSession.ssn)
        jsonObject.put("dags", drivingSession.dags)
        jsonObject.put("odometerStart", drivingSession.odometerStart)
        jsonObject.put("odometerEnd", drivingSession.odometerEnd)
        jsonObject.put("distanceDriven", drivingSession.distanceDriven)

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonObject,
            { response ->
                Log.d("DEBUG", "Response received: $response")
                successDeferred.complete(true)
            },
            { error ->
                Log.e("DEBUG", "Error in request: ${error.message}")
                successDeferred.complete(false)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer ${sessionUser.accessToken}"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        queue.add(jsonObjectRequest)
        return successDeferred.await()
    }
}