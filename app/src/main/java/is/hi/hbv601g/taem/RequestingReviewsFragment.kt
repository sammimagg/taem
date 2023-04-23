package `is`.hi.hbv601g.taem

import `is`.hi.hbv601g.taem.Persistance.Transaction
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


/**
* A fragment that displays the details of a transaction and allows the user to request a review for it.
 */
class RequestingReviewsFragment : Fragment() {


    /**
    * Called to do initial creation of a fragment. This is called after onAttach and before onCreateView.
    * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    /**
    * Called to have the fragment instantiate its user interface view. This is optional, and non-graphical fragments can return null (which is the default implementation).
    * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
    * @param container If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
    * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
    * @return Return the View for the fragment's UI, or null.
     */
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

        /**
        * Creates a new instance of the RequestingReviewsFragment with a transaction as an argument.
        * @param transaction The transaction to display in the fragment.
        * @return The new instance of the RequestingReviewsFragment.
         */
        fun newInstance(transaction: Transaction): RequestingReviewsFragment {
            val fragment = RequestingReviewsFragment()
            val args = Bundle()
            args.putParcelable("transaction",transaction)
            fragment.arguments = args
            return fragment
        }
    }

}