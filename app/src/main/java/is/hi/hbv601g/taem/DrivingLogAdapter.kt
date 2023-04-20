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




import java.time.format.DateTimeFormatter

class DrivingLogAdapter(private var drivingSessions: List<Driving>) : RecyclerView.Adapter<DrivingLogAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val distanceTextView: TextView = view.findViewById(R.id.distanceTextView)
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.driving_log_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drivingSession = drivingSessions[position]

        // Set the date, distance, and time
        holder.dateTextView.text = drivingSession.date
        holder.distanceTextView.text = drivingSession.distance.toString()
        holder.timeTextView.text = "${drivingSession.startTime} - ${drivingSession.endTime}"
    }

    override fun getItemCount(): Int = drivingSessions.size

    fun setData(drivingSessions: List<Driving>) {
        this.drivingSessions = drivingSessions
        notifyDataSetChanged()
    }

    fun appendDrivingSession(drivingSession: Driving) {
        drivingSessions += drivingSession
        notifyItemInserted(drivingSessions.lastIndex)
    }
}
