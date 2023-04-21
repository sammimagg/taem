package `is`.hi.hbv601g.taem

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import `is`.hi.hbv601g.taem.Persistance.Transaction
import android.util.Log
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeAndAttendanceAdapter (private val context: Activity,
                                private val arrayList: ArrayList<Transaction>) :
                                ArrayAdapter<Transaction>(context, R.layout.transaction_item, arrayList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context);
        val view : View = inflater.inflate(R.layout.transaction_item,null);

        val clockInDate : TextView = view.findViewById(R.id.transaction_date)
        val clockInTime : TextView = view.findViewById(R.id.transaction_clock_in)
        val clockOutTime : TextView = view.findViewById(R.id.transaction_clock_out)
        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSS][.SSSSS][.SSSS][.SSS][.SS][.S]")
            val raw_date_clock_in: LocalDateTime = LocalDateTime.parse(arrayList[position].clockIn, formatter)
            val raw_date_clock_out: LocalDateTime = LocalDateTime.parse(arrayList[position].clockOut, formatter)
            val raw_clock_in_date: LocalDate = raw_date_clock_in.toLocalDate()

            clockInDate.text = raw_clock_in_date.toString()
            clockInTime.text = raw_date_clock_in.toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString()
            clockOutTime.text = raw_date_clock_out.toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString()
        } catch (e: Exception) {
            Log.e("TimeAndAttendanceAdapter", "Error while parsing date and time values: ${e.message}")
        }


        return view;
    }
    override fun getItem(position: Int): Transaction? {
        return arrayList[position]
    }



}

