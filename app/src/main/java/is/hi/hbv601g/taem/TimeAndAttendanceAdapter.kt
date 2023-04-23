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

/**
* This class is an ArrayAdapter used to display a list of time and attendance transactions in a ListView.
* The adapter takes an ArrayList of Transaction objects and displays the clock-in and clock-out times, as well as the date, for each transaction in the list.
* The clock-in and clock-out times are parsed from their raw string format and formatted to display only the time portion in HH:mm:ss format.
* The date is also parsed from its raw string format and displayed in yyyy-MM-dd format.
* If there is an error while parsing the date and time values, an error message is logged to the console.
* @property context The Activity context in which the adapter is used.
* @property arrayList The ArrayList of Transaction objects to be displayed in the ListView.
 */
class TimeAndAttendanceAdapter (private val context: Activity,
                                private val arrayList: ArrayList<Transaction>) :
                                ArrayAdapter<Transaction>(context, R.layout.transaction_item, arrayList){

    /**
    *This method is called when each item in the ListView is created.
    * It inflates the layout for each item and sets the clock-in time, clock-out time, and date for the transaction.
    * If there is an error while parsing the date and time values, an error message is logged to the console.
    * @param position The position of the item in the ListView.
    * @param convertView The old view to reuse, if possible.
    * @param parent The parent viewgroup that the item belongs to.
    * @return The new view for each item in the ListView.
     */
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
    /**
    * This method returns the Transaction object at the specified position in the ArrayList.
    * @param position The position of the Transaction object to retrieve.
    * @return The Transaction object at the specified position in the ArrayList.
     */
    override fun getItem(position: Int): Transaction? {
        return arrayList[position]
    }



}

