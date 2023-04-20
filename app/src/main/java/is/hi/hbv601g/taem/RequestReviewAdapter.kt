package `is`.hi.hbv601g.taem

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import `is`.hi.hbv601g.taem.Persistance.MappedRequestUserDAO

class RequestReviewAdapter (private val context: Activity,
                                private val arrayList: ArrayList<MappedRequestUserDAO>) :
    ArrayAdapter<MappedRequestUserDAO>(context, R.layout.request_item, arrayList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context);
        val view : View = inflater.inflate(R.layout.request_item,null);
        val reqId : TextView = view.findViewById(R.id.request_id)
        val userKennitala : TextView = view.findViewById(R.id.user_kennitala2)
        val userName : TextView = view.findViewById(R.id.user_first_name)
        val old_date : TextView = view.findViewById(R.id.old_transaction_date)
        val new_date : TextView = view.findViewById(R.id.transaction_date)
        val old_clock_in : TextView = view.findViewById(R.id.old_transaction_clock_in)
        val old_clock_out : TextView = view.findViewById(R.id.old_transaction_clock_out)
        val new_clock_in : TextView = view.findViewById(R.id.transaction_clock_in)
        val new_clock_out : TextView = view.findViewById(R.id.transaction_clock_out)

        // Bæta við ID
        reqId.text = arrayList[position].transactionReview.id.toString()
        userKennitala.text = arrayList[position].employee.ssn
        userName.text = arrayList[position].employee.firstName

        old_date.text = arrayList[position].transactionReview.clockInDate
        new_date.text = arrayList[position].transactionReview.clockInDate
        old_clock_in.text = arrayList[position].transactionReview.originalClockInTime
        old_clock_out.text = arrayList[position].transactionReview.originalClockOutTime
        new_clock_in.text = arrayList[position].transactionReview.changedClockInTime
        new_clock_out.text = arrayList[position].transactionReview.changedClockOutTime

        return view;
    }
}
