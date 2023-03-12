package `is`.hi.hbv601g.taem

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class RealTimeAdapter(private val context: Activity, private val arrayList: ArrayList<Employee>) : ArrayAdapter<Employee>(context,R.layout.real_time_item, arrayList){
    override fun getView(position: Int, convertView: View?,parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context);
        val view : View = inflater.inflate(R.layout.real_time_item,null);

        val fullName : TextView = view.findViewById(R.id.fullName);
        val clockIn : TextView = view.findViewById(R.id.real_time_clock_in)
        val clockOut : TextView = view.findViewById(R.id.real_time_clock_out)
        val workedHour : TextView = view.findViewById(R.id.real_time_worked_hour);
        val status : TextView = view.findViewById(R.id.real_time_status);

        fullName.text = arrayList[position].firstName + " " +  arrayList[position].lastName;
        clockIn.text = arrayList[position].clockIn;
        clockOut.text = arrayList[position].clockOut;
        workedHour.text = arrayList[position].workedHours
        if (clockOut.text.isNullOrEmpty() && !clockIn.text.isNullOrEmpty()) {
            status.text = "Active";
        }
        else {

            status.text = "Deactive";
            view.alpha = 0.5f

        }



        return view;
    }

}