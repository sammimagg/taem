package `is`.hi.hbv601g.taem

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import `is`.hi.hbv601g.taem.Persistance.EmployeeRTI
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class RealTimeAdapter(private val context: Activity, private val arrayList: ArrayList<EmployeeRTI>) : ArrayAdapter<EmployeeRTI>(context,R.layout.real_time_item, arrayList){
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context);
        val view : View = inflater.inflate(R.layout.real_time_item,null);

        val fullName : TextView = view.findViewById(R.id.fullName);
        val clockIn : TextView = view.findViewById(R.id.transaction_date)
        val clockOut : TextView = view.findViewById(R.id.real_time_clock_out)
        val workedHour : TextView = view.findViewById(R.id.real_time_worked_hour);
        val status : TextView = view.findViewById(R.id.jobTitle);
        val name = arrayList[position].firstName + " " +  arrayList[position].lastName;
        // To trim to long names
        if (name.length <= 17) {
            fullName.text = arrayList[position].firstName + " " +  arrayList[position].lastName;
        }
        else {
            val missMatch = name.length - 17;
            var nameTrimmed = name.substring(0, name.length - missMatch);
            nameTrimmed = "$nameTrimmed.";
            fullName.text = nameTrimmed
        }

        if (!arrayList[position].clockInTime.isNullOrEmpty()) {
            val clockInTime = arrayList[position].clockInTime;
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            val clockInDateTime = LocalDateTime.parse(clockInTime, formatter)
            val currentTime = LocalDateTime.now()
            val duration = Duration.between(clockInDateTime, currentTime)
            val hours = duration.toHours()
            val minutes = duration.toMinutes() % 60
            val workedHours = hours.toString() + "H " + minutes.toString() + "m"
            workedHour.text = workedHours;


            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
            val time = dateFormat.parse(arrayList[position].clockInTime)
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            clockIn.text = timeFormat.format(time)
        }
        else {
            clockIn.text = arrayList[position].clockInTime
            workedHour.text = "";

        }
        clockOut.text = ""

        if (arrayList[position].clockIn) {
            status.text = "Active";
        }
        else {
            status.text = "Deactive";
            view.alpha = 0.5f
        }
        return view;
    }



}