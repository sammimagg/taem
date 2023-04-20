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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import `is`.hi.hbv601g.taem.DrivingLogAdapter
import `is`.hi.hbv601g.taem.Networking.Driving
import `is`.hi.hbv601g.taem.R
import `is`.hi.hbv601g.taem.Storage.db

class DrivingLogFragment : Fragment() {
    private lateinit var ssn: String
    private lateinit var drivingSessions: List<Driving>
    private lateinit var drivingLogAdapter: DrivingLogAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_driving_log, container, false)

        // Retrieve the ssn from the session
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        ssn = sharedPref?.getString("ssn", "") ?: ""

        val recyclerView = view.findViewById<RecyclerView>(R.id.drivingLogRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        drivingLogAdapter = DrivingLogAdapter(emptyList())
        recyclerView.adapter = drivingLogAdapter

        // Fetch the driving log
        fetchDrivingLog()

        return view
    }


    private fun fetchDrivingLog() {
        val queue = Volley.newRequestQueue(activity)
        var url = "https://www.hiv.is/api/driving/"

        val db2 = db.SessionUserContract.DBHelper(requireContext()).readableDatabase
        val cursor = db2.query(
            `is`.hi.hbv601g.taem.Storage.db.SessionUserContract.SessionUserEntry.TABLE_NAME,   // The table to query
            null,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            BaseColumns._ID              // The sort order
        )
        with(cursor) {
            moveToLast()
        }
        var ssnToUse = cursor.getString(4)
        url += ssnToUse

        val request = JsonObjectRequest(
            Request.Method.PUT, url, null,
            { response ->
                // Handle the JSON response
                val gson = Gson()
                drivingSessions =
                    gson.fromJson(response.toString(), Array<Driving>::class.java).toList()
                Log.d("DrivingLogFragment", "drivingSessions: $drivingSessions")
                //drivingLogAdapter.drivingSessions = drivingSessions
                drivingLogAdapter.notifyDataSetChanged()
            },
            { error ->
                // Handle the error
                Toast.makeText(activity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        queue.add(request)
    }
}