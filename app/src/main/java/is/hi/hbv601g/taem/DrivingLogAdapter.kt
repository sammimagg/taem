package `is`.hi.hbv601g.taem

import android.content.Context
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import `is`.hi.hbv601g.taem.Networking.Driving
import `is`.hi.hbv601g.taem.R
import `is`.hi.hbv601g.taem.Storage.db

class DrivingLogAdapter(var drivingSessions: List<Driving>) :
    RecyclerView.Adapter<DrivingLogAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val startTimeTextView: TextView = itemView.findViewById(R.id.startTimeTextView)
        val endTimeTextView: TextView = itemView.findViewById(R.id.endTimeTextView)
        val distanceTextView: TextView = itemView.findViewById(R.id.distanceTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.driving_log_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drivingSession = drivingSessions[position]
        holder.dateTextView.text = drivingSession.date
        holder.startTimeTextView.text = drivingSession.startTime
        holder.endTimeTextView.text = drivingSession.endTime
        holder.distanceTextView.text = "${drivingSession.distance} km"
    }

    override fun getItemCount(): Int {
        return drivingSessions.size
    }

    fun updateData(drivingSessions: List<Driving>) {
        this.drivingSessions = drivingSessions
        notifyDataSetChanged()
    }

companion object {
    /** Hvað er þetta? */
        fun fetchDrivingLog(ssn: String, onSuccess: (List<Driving>) -> Unit, onError: (String) -> Unit, context: Context) {
            val queue = Volley.newRequestQueue(context)
            var url = "https://www.hiv.is/api/driving/"

            val db2 = db.SessionUserContract.DBHelper(context).readableDatabase
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

            println("Using URL: $url ********************")

            val request = JsonObjectRequest(
                Request.Method.PUT, url, null,
                { response ->
                    // Handle the JSON response
                    val gson = Gson()
                    val drivingSessions = gson.fromJson(response.toString(), Array<Driving>::class.java).toList()
                    onSuccess(drivingSessions)
                },
                { error ->
                    // Handle the error
                    onError("Error: ${error.message}")
                })

            queue.add(request)
        }
    }
}
