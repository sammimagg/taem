package `is`.hi.hbv601g.taem

import `is`.hi.hbv601g.taem.Persistance.Transaction
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class RequestingReviewsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_requesting_reviews,container,false)
        val newDateFrom = view.findViewById<TextView>(R.id.newTimeFrom);
        val newDateTo = view.findViewById<TextView>(R.id.newTimeTo)
        val oldDateFrom = view.findViewById<TextView>(R.id.oldTimeFrom)
        val oldDateTo = view.findViewById<TextView>(R.id.oldTimeTo)

        val transaction = arguments?.getParcelable<Transaction>("transaction");
        Log.d("transaction", transaction?.clockInDate.toString())
        if(transaction != null) {
            oldDateFrom.text =transaction.clockIn
            oldDateTo.text = transaction.clockOutTime
        }
        return inflater.inflate(R.layout.fragment_requesting_reviews, container, false)
    }
    companion object {
        fun newInstance(transaction: Transaction): RequestingReviewsFragment {
            val fragment = RequestingReviewsFragment()
            val args = Bundle()
            args.putParcelable("transaction",transaction)
            fragment.arguments = args
            return fragment
        }
    }

}