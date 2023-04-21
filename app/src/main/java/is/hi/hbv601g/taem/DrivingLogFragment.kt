import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import java.util.*
import kotlin.collections.HashMap

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
        sessionUser = getSessionUser(requireContext())
            ?: throw IllegalStateException("SessionUser not found in SharedPreferences")
        println(sessionUser.ssn)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val drivingSessions =
                    fetchDrivingLog(sessionUser, sessionUser.ssn, requireContext())
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

        val addButton = view.findViewById<Button>(R.id.addDrivingLogButton)
        addButton.setOnClickListener {
            drivinglogappendHandler(view)
        }
    }
    fun drivinglogappendHandler(view: View) {
        val startText = view.findViewById<EditText>(R.id.OdometerStartText)
        val endText = view.findViewById<EditText>(R.id.OdometerEndText)
        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)

        val start = startText.text.toString().toIntOrNull() ?: 0
        val end = endText.text.toString().toIntOrNull() ?: 0

        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        val selectedDate = Date(year - 1900, month, day)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate)

        Log.d("DEBUG", "Selected date: $selectedDate")
        Log.d("DEBUG", "Formatted date: $formattedDate")

        val driving = Driving("", start, end, formattedDate.toString(), 0.0, 0, sessionUser.ssn)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (appendDrivingLog(sessionUser, sessionUser.ssn, driving, requireContext())) {
                    withContext(Dispatchers.Main) {
                        drivingLogAdapter.addData(driving)
                        startText.text = null
                        endText.text = null
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Failed to append driving session",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Failed to append driving session",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    }

    suspend fun fetchDrivingLog(sessionUser: SessionUser, ssn: String, context: Context): List<Driving>? {
        var url = "https://www.hiv.is/api/driving/"
        url += ssn;


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



    suspend fun appendDrivingLog(sessionUser: SessionUser, ssn: String, drivingSession: Driving, context: Context): Boolean {
        val url = "https://www.hiv.is/api/driving/new"

        val queue = Volley.newRequestQueue(context)
        val successDeferred = CompletableDeferred<Boolean>()

        val jsonObject = JSONObject()
        jsonObject.put("licencePlate", drivingSession.licencePlate)
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
