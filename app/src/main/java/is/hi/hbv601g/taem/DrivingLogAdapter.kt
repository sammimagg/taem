package `is`.hi.hbv601g.taem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import `is`.hi.hbv601g.taem.Persistance.Driving


/**
* The DrivingLogAdapter class is a RecyclerView adapter for displaying a list of driving sessions.
* @property drivingSessions A list of Driving objects representing the driving sessions to display.
 * */
class DrivingLogAdapter(var drivingSessions: List<Driving>) : RecyclerView.Adapter<DrivingLogAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val distanceTextView: TextView = view.findViewById(R.id.distanceTextView)
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)
    }

    /**
    * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
    * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
    * @param viewType The view type of the new View.
    * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.driving_log_item, parent, false)
        return ViewHolder(view)
    }

    /**
    * Called by RecyclerView to display the data at the specified position.
    * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
    * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drivingSession = drivingSessions[position]

        // Set the date, distance, and time
        holder.dateTextView.text = drivingSession.dags
        holder.distanceTextView.text = drivingSession.distanceDriven.toString()
        holder.timeTextView.text = "${drivingSession.odometerEnd} - ${drivingSession.odometerStart}"
    }

    /**
    * Returns the total number of items in the data set held by the adapter.
    * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = drivingSessions.size

    /**
    * Updates the data set and notifies the adapter that the data has changed.
    * @param drivingSessions The new list of driving sessions to display.
     */
    fun setData(drivingSessions: List<Driving>) {
        this.drivingSessions = drivingSessions
        notifyDataSetChanged()
    }

    /**
    * Adds a single Driving object to the data set and notifies the adapter that the data has changed.
    * @param drivingSession The Driving object to add to the data set.
     */

    fun addData(drivingSession: Driving) {
        drivingSessions = drivingSessions.plus(drivingSession)
        notifyDataSetChanged()
    }

    /**
    * Appends a single Driving object to the data set and notifies the adapter that a new item has been inserted.
    * @param drivingSession The Driving object to append to the data set.
     */
    fun appendDrivingSession(drivingSession: Driving) {
        drivingSessions += drivingSession
        notifyItemInserted(drivingSessions.lastIndex)

    }
}
